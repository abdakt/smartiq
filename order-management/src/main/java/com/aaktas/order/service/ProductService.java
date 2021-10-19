package com.aaktas.order.service;

import com.aaktas.order.VO.Product;
import com.aaktas.order.common.ResponsePayload;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(value = "product-service", url = "http://localhost:9001/products/")
public interface ProductService {

        @GetMapping("/{id}")
        ResponsePayload findProductById(@PathVariable("id") Long productId);

        @PutMapping("/stockupdate/{id}/{quantity}")
        public ResponsePayload updateProductQuantity(@PathVariable("id") Long productId, @PathVariable("quantity") Long quantity);
}
