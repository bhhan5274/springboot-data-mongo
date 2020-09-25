package com.bhhan.mongo.model;

import com.bhhan.mongo.annotation.CascadeSave;
import com.querydsl.core.annotations.QueryEntity;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

/**
 * Created by hbh5274@gmail.com on 2020-09-23
 * Github : http://github.com/bhhan5274
 */


@QueryEntity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Document
public class User {
    @Id
    private String id;

    @Indexed(direction = IndexDirection.ASCENDING)
    private String name;

    @Indexed(direction = IndexDirection.ASCENDING)
    private Integer age;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime modifiedAt;

    @DBRef
    @Field("email")
    @CascadeSave
    private EmailAddress emailAddress;

    @Builder
    public User(String id, String name, Integer age, EmailAddress emailAddress){
        this.id = id;
        this.name = name;
        this.age = age;
        this.emailAddress = emailAddress;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User modifyAge(Integer age){
        this.age = age;
        return this;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", emailAddress=" + emailAddress +
                '}';
    }
}
