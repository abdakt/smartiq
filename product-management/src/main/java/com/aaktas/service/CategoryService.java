package com.aaktas.service;

import com.aaktas.entity.Category;
import com.aaktas.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public Category updateCategory(Long id, Category category) {
        Category categoryEntity = findById(id);
        if(categoryEntity != null){
            categoryEntity.setParentCategoryId(category.getParentCategoryId());
            categoryEntity.setDescription(category.getDescription());
            categoryEntity.setAncestorsIdList(category.getAncestorsIdList());
            return categoryRepository.save(categoryEntity);
        }
        return null;
    }

    public Category saveCategory(Category category) {
        return  categoryRepository.save(category);
    }

    public Category findById(Long id) {
        Optional<Category> category = categoryRepository.findById(id);
        if(category.isPresent()){
            return categoryRepository.findById(id).get();
        }
        return null;
    }

    public List<Category> search(String title) {
        return categoryRepository.search(title);
    }

    public void delete(Long categoryId) {
        categoryRepository.deleteById(categoryId);
    }
}
