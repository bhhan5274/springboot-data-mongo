package com.bhhan.mongo.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by hbh5274@gmail.com on 2020-09-24
 * Github : http://github.com/bhhan5274
 */

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Document
public class Person {
    @Id
    private String id;
    private String name;

    @Builder
    public Person(String id, String name){
        this.id = id;
        this.name = name;
    }
}
