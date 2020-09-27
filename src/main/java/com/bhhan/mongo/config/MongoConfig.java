package com.bhhan.mongo.config;

import com.bhhan.mongo.converter.ZonedDateTimeReadConverter;
import com.bhhan.mongo.converter.ZonedDateTimeWriteConverter;
import com.bhhan.mongo.event.CascadeSaveMongoEventListener;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.CustomConversions;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by hbh5274@gmail.com on 2020-09-23
 * Github : http://github.com/bhhan5274
 */

@Configuration
@EnableMongoAuditing
@EnableMongoRepositories(basePackages = {"com.bhhan.mongo.repository", "com.bhhan.mongo.file.model"})
public class MongoConfig extends AbstractMongoClientConfiguration {
    @Override
    protected String getDatabaseName() {
        return "test";
    }

    @Override
    public MongoClient mongoClient() {
        ConnectionString connectionString = new ConnectionString("mongodb://localhost:27020/test");
        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .build();

        return MongoClients.create(mongoClientSettings);
    }

    @Override
    protected Collection<String> getMappingBasePackages() {
        return Collections.singleton("com.bhhan.mongo");
    }

    @Override
    public CustomConversions customConversions() {
        List<Converter<?, ?>> converters = new ArrayList<>();

        converters.add(new ZonedDateTimeReadConverter());
        converters.add(new ZonedDateTimeWriteConverter());
        return new MongoCustomConversions(converters);
    }

    @Bean
    public CascadeSaveMongoEventListener cascadeSaveMongoEventListener(MongoOperations mongoOperations){
        return new CascadeSaveMongoEventListener(mongoOperations);
    }

    @Bean
    public MongoTransactionManager transactionManager(MongoDbFactory mongoDbFactory){
        return new MongoTransactionManager(mongoDbFactory);
    }

    @Bean
    public GridFsTemplate gridFsTemplate() throws Exception {
        return new GridFsTemplate(mongoDbFactory(), mappingMongoConverter());
    }
}
