package com.bhhan.mongo.repository;

import com.bhhan.mongo.config.MongoConfig;
import com.bhhan.mongo.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Created by hbh5274@gmail.com on 2020-09-23
 * Github : http://github.com/bhhan5274
 */

@SpringBootTest(classes = MongoConfig.class)
class UserRepositoryTest extends BaseQueryTest{
    @Test
    void whenInsertingUser_thenUserIsInserted(){
        String userName = "Jon";
        User user = User.builder()
                .name(userName)
                .age(33)
                .build();

        userRepository.insert(user);
        assertEquals(userName, mongoOperations.findOne(Query.query(Criteria.where("name").is(userName)), User.class).getName());
    }

    @Test
    void whenSavingUser_thenUserIsSaved(){
        String userName = "Save";
        User user = User.builder()
                .name(userName)
                .build();
        userRepository.save(user);
        assertEquals(userName, mongoOperations.findOne(Query.query(Criteria.where("name").is(userName)), User.class).getName());
    }

    @Test
    void givenUserExists_whenSavingExistUser_thenUserIsUpdated(){
        User user = User.builder()
                .name("Jack")
                .build();

        userRepository.insert(user);

        user = mongoOperations.findOne(Query.query(Criteria.where("name").is("Jack")), User.class);
        user.setName("Jim");
        userRepository.save(user);
        assertEquals(1, userRepository.findAll().size());
    }

    @Test
    void givenUserExists_whenDeletingUser_thenUserIsDeleted(){
        User user = User.builder()
                .name("Benn")
                .build();
        userRepository.insert(user);

        userRepository.delete(user);

        assertEquals(0, userRepository.findAll().size());
    }

    @Test
    void givenUserExists_whenFindingUser_thenUserIsFound(){
        User user = User.builder()
                .name("Chris")
                .build();
        userRepository.insert(user);

        user = mongoOperations.findOne(Query.query(Criteria.where("name").is("Chris")), User.class);
        User foundUser = userRepository.findById(user.getId())
                .orElseThrow(IllegalArgumentException::new);

        assertEquals(user.getName(), foundUser.getName());
    }

    @Test
    void givenUserExists_whenCheckingDoesUserExist_thenUserIsExist(){
        User user = User.builder()
                .name("Harris")
                .build();
        userRepository.insert(user);

        user = mongoOperations.findOne(Query.query(Criteria.where("name").is("Harris")), User.class);
        boolean isExists = userRepository.existsById(user.getId());

        assertTrue(isExists);
    }

    @Test
    void givenUserExist_whenFindingAllUserWithSorting_thenUsersAreFoundAndSorted(){
        User user1 = User.builder()
                .name("Brendan")
                .build();
        User user2 = User.builder()
                .name("Adam")
                .build();
        userRepository.insert(user1);
        userRepository.insert(user2);

        List<User> users = userRepository.findAll(Sort.by(Sort.Direction.ASC, "name"));

        assertEquals(2, users.size());
        assertEquals("Adam", users.get(0).getName());
        assertEquals("Brendan", users.get(1).getName());
    }

    @Test
    void givenUsersExist_whenFindingAllUsersWithPagination_thenUsersAreFoundAndOrderedOnPage() {
        User user1 = User.builder()
                .name("Brendan")
                .build();
        User user2 = User.builder()
                .name("Adam")
                .build();
        userRepository.insert(user1);
        userRepository.insert(user2);

        PageRequest pageRequest = PageRequest.of(0, 1);

        Page<User> page = userRepository.findAll(pageRequest);
        List<User> users = page.getContent();

        assertEquals(1, users.size());
        assertEquals(2, page.getTotalPages());
    }
}