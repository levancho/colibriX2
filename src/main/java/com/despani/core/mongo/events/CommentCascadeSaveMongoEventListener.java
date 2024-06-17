package com.despani.core.mongo.events;

import com.despani.core.beans.mongo.DespComment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;

public class CommentCascadeSaveMongoEventListener extends AbstractMongoEventListener<Object> {
    @Autowired
    private MongoOperations mongoOperations;

    @Override
    public void onBeforeConvert(final BeforeConvertEvent<Object> event) {
        final Object source = event.getSource();
        if ((source instanceof DespComment) && (((DespComment) source).getUser() != null)) {
            mongoOperations.save(((DespComment) source).getUser());
        }
    }
}
