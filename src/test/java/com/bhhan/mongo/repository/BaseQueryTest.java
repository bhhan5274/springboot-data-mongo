package com.bhhan.mongo.repository;

import com.bhhan.mongo.model.Action;
import com.bhhan.mongo.model.EmailAddress;
import com.bhhan.mongo.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;

/**
 * Created by hbh5274@gmail.com on 2020-09-23
 * Github : http://github.com/bhhan5274
 */
public abstract class BaseQueryTest {
    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected MongoOperations mongoOperations;

    @Autowired
    protected PersonRepository personRepository;

    @Autowired
    protected ActionRepository actionRepository;

    @BeforeEach
    void init(){
        initDropCollection(User.class);
        initDropCollection(EmailAddress.class);
        initDropCollection(Action.class);
    }

    @AfterEach
    void clear(){
        afterDropCollection(User.class);
        afterDropCollection(EmailAddress.class);
        afterDropCollection(Action.class);
    }

    private void initDropCollection(Class aClass) {
        if(!mongoOperations.collectionExists(aClass)){
            mongoOperations.dropCollection(aClass);
        }
    }

    private void afterDropCollection(Class aClass) {
        mongoOperations.dropCollection(aClass);
    }

    protected User makeUser(String name, int age) {
        User user = User.builder()
                .name(name)
                .age(age)
                .build();
        return userRepository.save(user);
    }
}
