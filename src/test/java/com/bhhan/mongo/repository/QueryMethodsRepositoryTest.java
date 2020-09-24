package com.bhhan.mongo.repository;

import com.bhhan.mongo.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created by hbh5274@gmail.com on 2020-09-23
 * Github : http://github.com/bhhan5274
 */

@DataMongoTest
class QueryMethodsRepositoryTest extends BaseQueryTest{
    @Test
    void givenUsersExist_whenFindingUsersByName_thenUsersAreFound() {
        makeUser("Eric", 45);
        makeUser("Antony", 55);

        List<User> users = userRepository.findByName("Eric");
        assertEquals(1, users.size());
    }

    @Test
    void givenUsersExist_whenFindingUsersWithAgeCreaterThanAndLessThan_thenUsersAreFound() {
        makeUser("Jon", 20);
        makeUser("Jon", 50);
        makeUser("Jim", 33);

        List<User> users = userRepository.findByAgeBetween(26, 40);
        assertEquals(1, users.size());
    }

    @Test
    void givenUsersExist_whenFindingUserWithNameStartWithA_thenUsersAreFound() {
        makeUser("Eric", 45);
        makeUser("Antony", 33);
        makeUser("Alice", 35);

        List<User> users = userRepository.findByNameStartingWith("A");
        assertEquals(2, users.size());
    }

    @Test
    void givenUsersExist_whenFindingUserWithNameEndWithC_thenUsersAreFound() {
        makeUser("Eric", 45);
        makeUser("Antony", 33);
        makeUser("Alice", 35);

        List<User> users = userRepository.findByNameEndingWith("c");
        assertEquals(1, users.size());
    }

    @Test
    void givenUsersExist_whenFindingUsersAndSortThem_thenUsersAreFoundAndSorted() {
        makeUser("Eric", 45);
        makeUser("Antony", 33);
        makeUser("Alice", 35);

        List<User> users = userRepository.findByNameLikeOrderByAgeAsc("A");
        assertEquals(33, users.get(0).getAge());
        assertEquals(35, users.get(1).getAge());
    }
}
