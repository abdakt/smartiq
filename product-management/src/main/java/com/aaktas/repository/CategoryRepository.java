package com.aaktas.repository;

import com.aaktas.entity.Category;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;


import java.util.List;

public interface CategoryRepository extends MongoRepository<Category, Long> {

    public List<Category> findByDescription(String description);

    @Query("{'description' : { '$regex' : ?0, '$options' : 'i'}}")
    public List<Category> search(String description);

}
