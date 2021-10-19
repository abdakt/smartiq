package com.aaktas.entity;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.mongodb.lang.NonNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;


@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "className")
public class Product {

    @Transient
    public static final String SEQUENCE_NAME = "product_sequence";

    @Id
    private Long id;

    private String title;

    private List<Long> categoryList;

    private Long stockQuantity;
}
