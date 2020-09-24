package com.bhhan.mongo.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.ZonedDateTime;

/**
 * Created by hbh5274@gmail.com on 2020-09-24
 * Github : http://github.com/bhhan5274
 */

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@Document
public class Action {
    @Id
    private String id;

    private String description;
    private ZonedDateTime time;

    @Builder
    public Action(String id, String description, ZonedDateTime time){
        this.id = id;
        this.description = description;
        this.time = time;
    }

    @Override
    public String toString() {
        return "Action{" +
                "id='" + id + '\'' +
                ", description='" + description + '\'' +
                ", time=" + time +
                '}';
    }
}
