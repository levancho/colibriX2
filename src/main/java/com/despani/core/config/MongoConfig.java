package com.despani.core.config;

import com.despani.core.mongo.converters.CommentWriterConverter;
import com.despani.core.mongo.converters.ZonedDateTimeReadConverter;
import com.despani.core.mongo.converters.ZonedDateTimeWriteConverter;
import com.despani.core.mongo.events.CascadeSaveMongoEventListener;
import com.despani.core.mongo.events.CommentCascadeSaveMongoEventListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class MongoConfig   {

    private final List<Converter<?, ?>> converters = new ArrayList<Converter<?, ?>>();

//    @Bean
//    public CommentCascadeSaveMongoEventListener userCascadingMongoEventListener() {
//        return new CommentCascadeSaveMongoEventListener();
//    }

    @Bean
    public CascadeSaveMongoEventListener cascadingMongoEventListener() {
        return new CascadeSaveMongoEventListener();
    }


//    @Bean
//    public MongoCustomConversions customConversions() {
//        converters.add(new CommentWriterConverter());
//        converters.add(new ZonedDateTimeReadConverter());
//        converters.add(new ZonedDateTimeWriteConverter());
//        return new MongoCustomConversions(converters);
//    }

//    @Bean
//    public MappingMongoConverter mappingMongoConverter(MongoDbFactory factory,
//                                                       MongoMappingContext context, BeanFactory beanFactory) {
//        DbRefResolver dbRefResolver = new DefaultDbRefResolver(factory);
//        MappingMongoConverter mappingConverter = new MappingMongoConverter(dbRefResolver,
//                context);
//
//
//        try {
//            CustomConversions cc = beanFactory.getBean(CustomConversions.class);
//
//            mappingConverter
//                    .setCustomConversions(cc);
//        }
//        catch (NoSuchBeanDefinitionException ex) {
//            // Ignore
//        }
//        return mappingConverter;
//    }
}
