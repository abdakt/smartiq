package com.aaktas.controller;

import com.aaktas.common.ResponseEnum;
import com.aaktas.common.ResponsePayload;
import com.aaktas.entity.Category;
import com.aaktas.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@Slf4j
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/list")
    public ResponsePayload listCategories(@RequestParam("description") String description){

        try {
            List<Category> categoryList = categoryService.search(description);
            if(categoryList == null){
                return new ResponsePayload(ResponseEnum.NOTFOUND, "Category not found");
            }
            return new ResponsePayload(ResponseEnum.OK, categoryList);
        } catch (Exception e) {
            log.error("Exception occurred while calling listCategories:" + e.getMessage(), e);
            return new ResponsePayload(ResponseEnum.INTERNAL_ERROR);
        }
    }

    @PostMapping("/")
    public ResponsePayload saveCategory(@RequestBody Category category){

        try {
            return new ResponsePayload(ResponseEnum.OK, categoryService.saveCategory(category));
        } catch (Exception e) {
            log.error("Exception occurred while calling saveCategory:" + e.getMessage(), e);
            return new ResponsePayload(ResponseEnum.INTERNAL_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponsePayload findByCategoryId(@PathVariable("id") Long categoryId){

        try {
            Category category = categoryService.findById(categoryId);
            if(category == null){
                return new ResponsePayload(ResponseEnum.NOTFOUND, "Category not found");
            }
            return new ResponsePayload(ResponseEnum.OK, category);
        } catch (Exception e) {
            log.error("Exception occurred while calling findByCategoryId:" + e.getMessage(), e);
            return new ResponsePayload(ResponseEnum.INTERNAL_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponsePayload updateCategory(@PathVariable("id") Long categoryId, @RequestBody Category category){

        try {
            Category categoryUpdated = categoryService.updateCategory(categoryId, category);
            if(categoryUpdated == null){
                return new ResponsePayload(ResponseEnum.NOTFOUND, "Category not found");
            }
            return new ResponsePayload(ResponseEnum.OK, "Updated successfully.", categoryUpdated);
        } catch (Exception e) {
            log.error("Exception occurred while calling updateCategory:" + e.getMessage(), e);
            return new ResponsePayload(ResponseEnum.INTERNAL_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponsePayload deleteCategory(@PathVariable("id") Long categoryId){

        try {
            categoryService.delete(categoryId);
            return new ResponsePayload(ResponseEnum.OK);
        } catch (Exception e) {
            log.error("Exception occurred while calling deleteCategory:" + e.getMessage(), e);
            return new ResponsePayload(ResponseEnum.INTERNAL_ERROR);
        }
    }
}
