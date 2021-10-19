package com.aaktas.order.VO;

import com.aaktas.order.common.OrderStatus;
import com.aaktas.order.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderVO {

    private Long orderId;
    private List<Product> product;
    private String deliveryAddress;
    private OrderStatus status;
}
