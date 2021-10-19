package com.aaktas.order.service;

import com.aaktas.order.VO.Product;
import com.aaktas.order.VO.OrderVO;
import com.aaktas.order.common.OrderStatus;
import com.aaktas.order.common.ResponsePayload;
import com.aaktas.order.common.ValidationMessage;
import com.aaktas.order.entity.Order;
import com.aaktas.order.entity.OrderDetail;
import com.aaktas.order.repository.OrderRepository;
import com.aaktas.order.repository.OrderDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private static int PAGE_SIZE = 100;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ProductService productService;

    @Transactional
    public Order createOrder(Order order) {
        order.setStatus(OrderStatus.RECEIVED);
        return saveOrder(order);
    }

    @Transactional
    public Order updateOrder(Long orderId, Order orderToBeSaved) {
        Order orderSaved = orderRepository.findById(orderId).get();
        orderSaved.getOrderDetailList().forEach(orderDetail -> orderDetailRepository.delete(orderDetail));  //TODO:return stock values in product mngmnt service
        orderSaved.getOrderDetailList().clear();
        orderToBeSaved.getOrderDetailList().forEach(orderSaved.getOrderDetailList()::add);
        orderSaved.setStatus(orderToBeSaved.getStatus());
        orderSaved.setDeliveryAddress(orderToBeSaved.getDeliveryAddress());
        orderSaved.getOrderDetailList().forEach(d -> d.setOrder(orderSaved));
        return saveOrder(orderSaved);
    }

    public Order saveOrder(Order order) {
        List<Product> lstProductsToRollback = new ArrayList<>();
        order.getOrderDetailList().stream().forEach(item ->{
            lstProductsToRollback.add(getProductById(item.getProductId()));
        });
        boolean rollBack = false;
        for (OrderDetail orderDetail : order.getOrderDetailList()) {
            Product product = lstProductsToRollback.stream().filter(p -> p.getId().equals(orderDetail.getProductId())).findAny().get();
            ResponsePayload responsePayload = productService.updateProductQuantity(orderDetail.getProductId(),
                    product.getStockQuantity() - orderDetail.getQuantity());    //TODO:check excessive decrease
            if(responsePayload.getData() == null){
                rollBack = true;
                break;
            }
        }
        if(rollBack){
            rollBackStockQuantities(lstProductsToRollback);
            return null;
        }
        order.getOrderDetailList().forEach(d -> d.setOrder(order));
        return orderRepository.save(order);
    }

    private void rollBackStockQuantities(List<Product> lstProducts) {
        //TODO: rollback by calling updateProductQuantity with original values
    }

    public OrderVO getOrderWithProduct(Long orderId) {
        Optional<Order> optOrder = orderRepository.findById(orderId);
        if(optOrder.isEmpty()){
            return null;
        }
        Order order = optOrder.get();
        OrderVO orderVO = new OrderVO();
        orderVO.setOrderId(orderId);
        orderVO.setDeliveryAddress(order.getDeliveryAddress());
        orderVO.setStatus(order.getStatus());
        List<Product> productList = new ArrayList<>();
        order.getOrderDetailList().stream().forEach(orderDetail -> {
            Product product = getProductById(orderDetail.getProductId());
            product.setOrderQuantity(orderDetail.getQuantity());
            productList.add(product);
        });
        orderVO.setProduct(productList);

        return orderVO;
    }

    public Product getProductById(Long productId){
        ResponsePayload responsePayload = productService.findProductById(productId);
        Product product =  null;
        if(responsePayload.getData() != null){
            product = new Product();
            product.setId(Long.valueOf(((LinkedHashMap) responsePayload.getData()).get("id").toString()));
            product.setTitle((String) ((LinkedHashMap) responsePayload.getData()).get("title"));
            product.setStockQuantity(Long.valueOf(((LinkedHashMap) responsePayload.getData()).get("stockQuantity").toString()));
        }
        return product;
    }

    public ValidationMessage validateOrderCreate(Order order) {
        if(order.getOrderDetailList() == null || order.getOrderDetailList().isEmpty()
            || order.getOrderDetailList().get(0).getProductId() == null){
            return new ValidationMessage("At least one product must be provided.", "Validation Failed!", false);
        }

        if(order.getOrderDetailList().get(0).getQuantity() == null){
            return new ValidationMessage("Quantity must be specified", "Validation Failed!", false);
        }

        if(getProductById(order.getOrderDetailList().get(0).getProductId()) == null){
            return new ValidationMessage("Product not found:" + order.getOrderDetailList().get(0).getProductId(), "Validation Failed!", false);
        }

        return new ValidationMessage(true);
    }

    public ValidationMessage validateOrderUpdate(Long orderId, Order order) {

        if(orderRepository.findById(orderId).isEmpty()){
            return new ValidationMessage("Order not found in the database.", "Validation Failed!", false);
        }
        if(order.getStatus() == null){
            return new ValidationMessage("Order status must be satisfied.", "Validation Failed!", false);
        }

        ValidationMessage validationMessage = validateOrderCreate(order);
        if(!validationMessage.isValid()){
            return validationMessage;
        }

        return new ValidationMessage(true);
    }

    public List<Order> list(Integer page) {
        Pageable pageable = PageRequest.of(page.intValue() - 1, PAGE_SIZE);
        return orderRepository.findAll(pageable).getContent();
    }
}
