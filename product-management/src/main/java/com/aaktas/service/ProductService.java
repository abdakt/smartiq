package com.aaktas.service;

import com.aaktas.common.ValidationMessage;
import com.aaktas.entity.Product;
import com.aaktas.repository.CategoryRepository;
import com.aaktas.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    public Product saveProduct(Product product) {

        return productRepository.save(product);
    }

    public Product findById(Long id) {
        Optional<Product> product = productRepository.findById(id);
        if(product.isPresent()){
            return productRepository.findById(id).get();
        }
        return null;
    }

    public Product updateProductQuantity(Long id, Long quantity){
        Product product = findById(id);
        int prim = 0;
        if(product != null){
            synchronized (product){
                product.setStockQuantity(quantity);
            }
        }
        saveProduct(product);
        return product;
    }

    public ValidationMessage validateProductSave(Product product){
        if(product.getCategoryList() == null || product.getCategoryList().isEmpty()){
            return new ValidationMessage("At least one category must be provided.", "Validation Failed!", false);
        }

        for (Long categoryId : product.getCategoryList()){
            if(categoryRepository.findById(categoryId) == null){
                return new ValidationMessage("Category id not found:" + categoryId, "Validation Failed!", false);
            }
        }
        return new ValidationMessage(true);
    }

    public ValidationMessage validateProductUpdate(Long productId, Product product){
        if(findById(productId) == null){
            return new ValidationMessage("Product to be updated not found.", "Validation Failed!", false);
        }
        ValidationMessage validationMessage = validateProductSave(product);
        if(!validationMessage.isValid()){
            return validationMessage;
        }

        return new ValidationMessage(true);
    }

    public List<Product> search(String title) {
        return productRepository.search(title);
    }

    public void delete(Long productId) {
        productRepository.deleteById(productId);
    }
}
