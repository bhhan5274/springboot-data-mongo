package com.bhhan.mongo.transaction;

import com.bhhan.mongo.config.MongoConfig;
import com.bhhan.mongo.model.EmailAddress;
import com.bhhan.mongo.model.User;
import com.bhhan.mongo.repository.UserRepository;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.MongoTransactionException;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Created by hbh5274@gmail.com on 2020-09-24
 * Github : http://github.com/bhhan5274
 */

@SpringBootTest(classes = MongoConfig.class)
class MongoTransactionalTest {

    @Autowired
    private MongoOperations mongoOperations;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private UserRepository userRepository;

    @Test
    @Transactional
    void whenPerformMongoTransaction_thenSuccess() {
        makeUser("John", 30);
        makeUser("Ringo", 35);

        List<User> users = userRepository.findAll();
        assertEquals(2, users.size());
    }

    @Test
    @Transactional
    void whenListCollectionDuringMongoTransaction_thenException() {
        assertThrows(MongoTransactionException.class, () -> {
            if(mongoOperations.collectionExists(User.class)){
                makeUser("John", 30);
                makeUser("Ringo", 35);
            }
        });
    }

    private void makeUser(String name, Integer age){
        userRepository.save(User.builder()
                .name(name)
                .age(age)
                .emailAddress(EmailAddress.builder()
                        .value(String.format("test%d@email.com", age))
                        .build())
                .build());
    }

    @Test
    void setup() {
        if (!mongoTemplate.collectionExists(User.class)) {
            mongoTemplate.createCollection(User.class);
        }
    }

    @Test
    void tearDown() {
        mongoTemplate.dropCollection(User.class);
        mongoTemplate.dropCollection(EmailAddress.class);
    }
}
