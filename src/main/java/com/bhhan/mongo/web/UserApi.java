package com.bhhan.mongo.web;

import com.bhhan.mongo.model.User;
import com.bhhan.mongo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * Created by hbh5274@gmail.com on 2020-09-24
 * Github : http://github.com/bhhan5274
 */

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserApi {
    private final UserService userService;

    @PostMapping("/success/{name}/{age}")
    @ResponseStatus(HttpStatus.CREATED)
    public User addUserSuccess(@PathVariable String name, @PathVariable Integer age){
        return userService.addUserSuccess(name, age);
    }

    @PostMapping("/fail/{name}/{age}")
    @ResponseStatus(HttpStatus.CREATED)
    public User addUserFail(@PathVariable String name, @PathVariable Integer age){
        try {
            return userService.addUserFail(name, age);
        }catch(Exception e){
            return User.builder()
                    .name("Fail")
                    .build();
        }
    }
}
