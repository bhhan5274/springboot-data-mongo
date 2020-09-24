package com.bhhan.mongo.repository;

import com.bhhan.mongo.model.Person;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created by hbh5274@gmail.com on 2020-09-24
 * Github : http://github.com/bhhan5274
 */
public interface PersonRepository extends MongoRepository<Person, String> {
    List<Person> findByName(String name);
}
