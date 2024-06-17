package com.despani.core.mongo.events;

import com.despani.core.mongo.annotation.CascadeSave;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.util.ReflectionUtils;

public class CascadeSaveMongoEventListener extends AbstractMongoEventListener<Object> {

    @Autowired
    private MongoOperations mongoOperations;

    @Override
    public void onBeforeConvert(final BeforeConvertEvent<Object> event) {
        final Object source = event.getSource();

//        ReflectionUtils.doWithFields(source.getClass(), new CascadeCallback(source, mongoOperations));


        ReflectionUtils.doWithFields(source.getClass(), (field)->{
            ReflectionUtils.makeAccessible(field);

//
            if (field.isAnnotationPresent(DBRef.class)  && field.isAnnotationPresent(CascadeSave.class) ) {
                final Object fieldValue = field.get(source);

                if (fieldValue != null) {
                    final FieldCallback callback = new FieldCallback();
                    ReflectionUtils.doWithFields(fieldValue.getClass(), callback);
                    mongoOperations.save(fieldValue);
                }
            }
        });
    }
}