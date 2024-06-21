package com.despani.x2.core.config;

import com.despani.x2.contents.mongo.events.CascadeSaveMongoEventListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;

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
