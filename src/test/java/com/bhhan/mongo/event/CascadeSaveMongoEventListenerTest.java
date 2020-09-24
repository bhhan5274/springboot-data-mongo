package com.bhhan.mongo.event;

import com.bhhan.mongo.config.MongoConfig;
import com.bhhan.mongo.model.EmailAddress;
import com.bhhan.mongo.model.User;
import com.bhhan.mongo.repository.BaseQueryTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created by hbh5274@gmail.com on 2020-09-24
 * Github : http://github.com/bhhan5274
 */

@Slf4j
@SpringBootTest(classes = MongoConfig.class)
class CascadeSaveMongoEventListenerTest extends BaseQueryTest {
    @Test
    void cascade_test(){
        User user = User.builder()
                .name("Bhhan")
                .age(33)
                .emailAddress(EmailAddress.builder()
                        .value("test@email.com")
                        .build())
                .build();
        userRepository.save(user);
    }

    @Test
    void write_read_cascade_test(){
        User user = User.builder()
                .name("Bhhan")
                .age(33)
                .emailAddress(EmailAddress.builder()
                        .value("test@email.com")
                        .build())
                .build();

        userRepository.save(user);

        List<User> users = userRepository.findAll();
        assertEquals(1, users.size());
        log.info(users.get(0).toString());

        List<EmailAddress> emails = mongoOperations.findAll(EmailAddress.class);
        assertEquals(1, emails.size());
        log.info(emails.get(0).toString());
    }
}