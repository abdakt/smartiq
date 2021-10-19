package com.aaktas.event;

import com.aaktas.entity.Product;
import com.aaktas.service.SequenceGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;


@Component
public class ProductListener extends AbstractMongoEventListener<Product> {

    private SequenceGeneratorService sequenceGenerator;

    @Autowired
    public ProductListener(SequenceGeneratorService sequenceGenerator) {
        this.sequenceGenerator = sequenceGenerator;
    }

    @Override
    public void onBeforeConvert(BeforeConvertEvent<Product> event) {
        Long categoryId ;
        if (event.getSource().getId() == null) {
            categoryId = sequenceGenerator.generateSequence(Product.SEQUENCE_NAME);
            event.getSource().setId(categoryId);
        } else {
            categoryId = event.getSource().getId();
        }

    }


}
