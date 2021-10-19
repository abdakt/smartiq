package com.aaktas.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Category {

    @org.springframework.data.annotation.Transient
    public static final String SEQUENCE_NAME = "category_sequence";

    @Id
    private Long id;

    private String description;

    private Long parentCategoryId;

    private List<Long> ancestorsIdList;

}
