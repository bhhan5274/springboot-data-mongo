package com.bhhan.mongo.service;

import com.bhhan.mongo.model.EmailAddress;
import com.bhhan.mongo.model.User;
import com.bhhan.mongo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by hbh5274@gmail.com on 2020-09-24
 * Github : http://github.com/bhhan5274
 */

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public User addUserSuccess(String name, Integer age){
        return makeUser(name, age);
    }

    @Transactional
    public User addUserFail(String name, Integer age){
        makeUser(name, age);
        throw new IllegalArgumentException();
    }

    @Transactional
    public User updateUserAgeSuccess(String name, Integer age){
        List<User> users = userRepository.findByName(name);
        return userRepository.save(users.get(0).modifyAge(age));
    }

    @Transactional
    public User updateUserAgeFail(String name, Integer age){
        List<User> users = userRepository.findByName(name);
        userRepository.save(users.get(0).modifyAge(age));
        throw new IllegalArgumentException();
    }

    private User makeUser(String name, Integer age){
        return userRepository.save(User.builder()
                .name(name)
                .age(age)
                .emailAddress(EmailAddress.builder()
                        .value(String.format("test%d@email.com", age))
                        .build())
                .build());
    }
}
