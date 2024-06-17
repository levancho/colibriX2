package com.despani.core.mongo.converters;

import com.despani.core.beans.mongo.DespComment;
import com.despani.core.beans.mongo.DespaniMongoUser;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CommentWriterConverter implements Converter<DespComment, DBObject> {

    @Override
    public DBObject convert(final DespComment comment) {
        final DBObject dbObject = new BasicDBObject();
        dbObject.put("comment", comment.getComment());
        if (comment.getUser() != null) {
            final DBObject userObject = new BasicDBObject();
            DespaniMongoUser user = comment.getUser();
            userObject.put("username", user.getUsername());
        }
        dbObject.removeField("_class");
        return dbObject;
    }

}
