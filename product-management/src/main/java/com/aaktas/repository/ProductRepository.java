package com.aaktas.repository;

import com.aaktas.entity.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface ProductRepository extends MongoRepository<Product, Long> {

    public List<Product> findByTitle(String title);

    @Query("{'title' : { '$regex' : ?0, '$options' : 'i'}}")
    List<Product> search(String title);
}
