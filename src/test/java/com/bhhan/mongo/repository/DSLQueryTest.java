package com.bhhan.mongo.repository;

import com.bhhan.mongo.model.QUser;
import com.bhhan.mongo.model.User;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created by hbh5274@gmail.com on 2020-09-24
 * Github : http://github.com/bhhan5274
 */

@DataMongoTest
class DSLQueryTest extends BaseQueryTest {

    private QUser qUser = QUser.user;

    @Test
    void givenUsersExist_whenFindingUsersByName_thenUserAreFound() {
        makeUser("Eric", 45);
        makeUser("Antony", 55);

        BooleanExpression predicate = qUser.name.eq("Eric");
        List<User> users = (List<User>) userRepository.findAll(predicate);

        assertEquals(1, users.size());
    }

    @Test
    void givenUsersExist_whenFindingUsersWithAgeCreaterThanAndLessThan_thenUsersAreFound() {
        makeUser("Jon", 20);
        makeUser("Jon", 50);
        makeUser("Jim", 33);

        BooleanExpression predicate = qUser.age.between(26, 40);
        List<User> users = (List<User>) userRepository.findAll(predicate);
        assertEquals(1, users.size());
    }

    @Test
    void givenUsersExist_whenFindingUserWithNameStartWithA_thenUsersAreFound() {
        makeUser("Eric", 45);
        makeUser("Antony", 55);
        makeUser("Alice", 35);

        BooleanExpression predicate = qUser.name.startsWith("A");
        List<User> users = (List<User>) userRepository.findAll(predicate);
        assertEquals(2, users.size());
    }

    @Test
    void givenUsersExist_whenFindingUserWithNameEndWithC_thenUsersAreFound() {
        makeUser("Eric", 45);
        makeUser("Antony", 55);
        makeUser("Alice", 35);

        BooleanExpression predicate = qUser.name.endsWith("c");
        List<User> users = (List<User>) userRepository.findAll(predicate);
        assertEquals(1, users.size());
    }
}
