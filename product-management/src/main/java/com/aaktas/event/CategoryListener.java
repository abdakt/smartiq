package com.aaktas.event;

import com.aaktas.entity.Category;
import com.aaktas.service.SequenceGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;


@Component
public class CategoryListener extends AbstractMongoEventListener<Category> {

    private SequenceGeneratorService sequenceGenerator;

    @Autowired
    public CategoryListener(SequenceGeneratorService sequenceGenerator) {
        this.sequenceGenerator = sequenceGenerator;
    }

    @Override
    public void onBeforeConvert(BeforeConvertEvent<Category> event) {
        Long categoryId ;
        if (event.getSource().getId() == null) {
            categoryId = sequenceGenerator.generateSequence(Category.SEQUENCE_NAME);
            event.getSource().setId(categoryId);
        } else {
            categoryId = event.getSource().getId();
        }

    }


}
