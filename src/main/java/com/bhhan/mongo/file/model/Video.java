package com.bhhan.mongo.file.model;

import lombok.Getter;
import lombok.Setter;

import java.io.InputStream;

/**
 * Created by hbh5274@gmail.com on 2020-09-27
 * Github : http://github.com/bhhan5274
 */

@Getter
@Setter
public class Video {
    private String title;
    private InputStream stream;

    @Override
    public String toString() {
        return "Video{" +
                "title='" + title + '\'' +
                '}';
    }
}
