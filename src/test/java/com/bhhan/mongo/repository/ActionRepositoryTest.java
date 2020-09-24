package com.bhhan.mongo.repository;

import com.bhhan.mongo.config.MongoConfig;
import com.bhhan.mongo.model.Action;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by hbh5274@gmail.com on 2020-09-24
 * Github : http://github.com/bhhan5274
 */

@SpringBootTest(classes = MongoConfig.class)
class ActionRepositoryTest extends BaseQueryTest {
    @Test
    void givenSavedAction_TimeIsRetrievedCorrectly() {
        ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);

        Action action = Action.builder()
                .description("click-action")
                .time(now)
                .build();

        Action savedAction = actionRepository.save(action);

        Action foundAction = actionRepository.findById(savedAction.getId())
                .orElseThrow(IllegalArgumentException::new);

        assertEquals(now.withNano(0), foundAction.getTime().withNano(0));
    }
}