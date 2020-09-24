package com.bhhan.mongo.repository;

import com.bhhan.mongo.model.Action;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by hbh5274@gmail.com on 2020-09-24
 * Github : http://github.com/bhhan5274
 */
public interface ActionRepository extends MongoRepository<Action, String> {
}
