package com.aaktas.controller;

import com.aaktas.common.ResponseEnum;
import com.aaktas.common.ResponsePayload;
import com.aaktas.common.ValidationMessage;
import com.aaktas.entity.Product;
import com.aaktas.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@Slf4j
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/list")
    public ResponsePayload listProducts(@RequestParam("title") String title){

        try {
            List<Product> productList = productService.search(title);
            if(productList == null){
                return new ResponsePayload(ResponseEnum.NOTFOUND, "Product not found");
            }
            return new ResponsePayload(ResponseEnum.OK, productList);
        } catch (Exception e) {
            log.error("Exception occurred while calling listProducts:" + e.getMessage(), e);
            return new ResponsePayload(ResponseEnum.INTERNAL_ERROR);
        }
    }

    @PostMapping("/")
    public ResponsePayload saveProduct(@RequestBody Product product){

        try {
            ValidationMessage validation = productService.validateProductSave(product);
            if(validation.isValid()){
                return new ResponsePayload(ResponseEnum.OK, productService.saveProduct(product));
            }
            else {
                return new ResponsePayload(ResponseEnum.BADREQUEST, validation.getMessage());
            }

        } catch (Exception e) {
            log.error("Exception occurred while calling saveProduct:" + e.getMessage(), e);
            return new ResponsePayload(ResponseEnum.INTERNAL_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponsePayload findProductById(@PathVariable("id") Long productId){

        try {
            return new ResponsePayload(ResponseEnum.OK, productService.findById(productId));
        } catch (Exception e) {
            log.error("Exception occurred while calling findByProductId:" + e.getMessage(), e);
            return new ResponsePayload(ResponseEnum.INTERNAL_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponsePayload updateProduct(@PathVariable("id") Long productId, @RequestBody Product product) {

        try {
            ValidationMessage validation = productService.validateProductUpdate(productId, product);
            if(validation.isValid()){
                product.setId(productId);
                return new ResponsePayload(ResponseEnum.OK, productService.saveProduct(product));
            }
            else {
                return new ResponsePayload(ResponseEnum.BADREQUEST, validation.getMessage());
            }

        } catch (Exception e) {
            log.error("Exception occurred while calling updateProduct:" + e.getMessage(), e);
            return new ResponsePayload(ResponseEnum.INTERNAL_ERROR);
        }
    }

    @PutMapping("/stockupdate/{id}/{quantity}")
    public ResponsePayload updateProductQuantity(@PathVariable("id") Long productId, @PathVariable("quantity") Long quantity) {

        try {
            return new ResponsePayload(ResponseEnum.OK, productService.updateProductQuantity(productId, quantity));
        } catch (Exception e) {
            log.error("Exception occurred while calling updateProductQuantity:" + e.getMessage(), e);
            return new ResponsePayload(ResponseEnum.INTERNAL_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponsePayload deleteProduct(@PathVariable("id") Long productId){

        try {
            productService.delete(productId);
            return new ResponsePayload(ResponseEnum.OK);
        } catch (Exception e) {
            log.error("Exception occurred while calling deleteProduct:" + e.getMessage(), e);
            return new ResponsePayload(ResponseEnum.INTERNAL_ERROR);
        }
    }
}
