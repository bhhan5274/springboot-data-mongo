package com.bhhan.mongo.repository;

import com.bhhan.mongo.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

/**
 * Created by hbh5274@gmail.com on 2020-09-23
 * Github : http://github.com/bhhan5274
 */
public interface UserRepository extends MongoRepository<User, String>, QuerydslPredicateExecutor<User> {
    List<User> findByName(String name);
    List<User> findByNameStartingWith(String name);
    List<User> findByNameEndingWith(String name);
    List<User> findByAgeBetween(int ageGT, int ageLT);
    List<User> findByNameLikeOrderByAgeAsc(String name);
}
