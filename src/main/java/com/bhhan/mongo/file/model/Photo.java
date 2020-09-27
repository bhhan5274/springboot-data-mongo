package com.bhhan.mongo.file.model;

import lombok.Getter;
import lombok.Setter;
import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by hbh5274@gmail.com on 2020-09-27
 * Github : http://github.com/bhhan5274
 */

@Document(collection = "photos")
@Getter
@Setter
public class Photo {
    @Id
    private String id;

    private String title;
    private Binary image;

    public Photo(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Photo{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", image=" + image +
                '}';
    }
}
