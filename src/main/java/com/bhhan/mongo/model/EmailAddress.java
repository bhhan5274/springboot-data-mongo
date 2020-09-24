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
public class EmailAddress {
    @Id
    private String id;
    private String value;

    @Builder
    public EmailAddress(String id, String value){
        this.id = id;
        this.value = value;
    }

    @Override
    public String toString() {
        return "EmailAddress{" +
                "id='" + id + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
