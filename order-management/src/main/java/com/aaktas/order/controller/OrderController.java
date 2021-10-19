package com.aaktas.order.controller;

import com.aaktas.order.common.ResponseEnum;
import com.aaktas.order.common.ResponsePayload;
import com.aaktas.order.common.ValidationMessage;
import com.aaktas.order.entity.Order;
import com.aaktas.order.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@Slf4j
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/")
    public ResponsePayload createOrder(@RequestBody Order order){

        try {
            ValidationMessage validation = orderService.validateOrderCreate(order);
            if(validation.isValid()){
                return new ResponsePayload(ResponseEnum.OK, orderService.createOrder(order));
            }
            else {
                return new ResponsePayload(ResponseEnum.BADREQUEST, validation.getMessage());
            }

        } catch (Exception e) {
            log.error("Exception occurred while calling createOrder:" + e.getMessage(), e);
            return new ResponsePayload(ResponseEnum.INTERNAL_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponsePayload getOrderWithProduct(@PathVariable("id") Long orderId){
        try {
            return new ResponsePayload(ResponseEnum.OK, orderService.getOrderWithProduct(orderId));
        } catch (Exception e) {
            log.error("Exception occurred while calling getOrderWithProduct:" + e.getMessage(), e);
            return new ResponsePayload(ResponseEnum.INTERNAL_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponsePayload updateOrder(@PathVariable("id") Long orderId, @RequestBody Order order) {

        try {
            ValidationMessage validation = orderService.validateOrderUpdate(orderId, order);
            if(validation.isValid()){
                return new ResponsePayload(ResponseEnum.OK, orderService.updateOrder(orderId, order));
            }
            else {
                return new ResponsePayload(ResponseEnum.BADREQUEST, validation.getMessage());
            }

        } catch (Exception e) {
            log.error("Exception occurred while calling updateOrder:" + e.getMessage(), e);
            return new ResponsePayload(ResponseEnum.INTERNAL_ERROR);
        }
    }

    @GetMapping("/list")
    public ResponsePayload listOrders(@RequestParam("page") Integer page){

        try {
            List<Order> orderList = orderService.list(page);
            if(orderList == null){
                return new ResponsePayload(ResponseEnum.NOTFOUND, "Orders not found");
            }
            return new ResponsePayload(ResponseEnum.OK, orderList);
        } catch (Exception e) {
            log.error("Exception occurred while calling listOrders:" + e.getMessage(), e);
            return new ResponsePayload(ResponseEnum.INTERNAL_ERROR);
        }
    }
}
