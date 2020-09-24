package com.bhhan.mongo.event;

import org.springframework.data.annotation.Id;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;

/**
 * Created by hbh5274@gmail.com on 2020-09-24
 * Github : http://github.com/bhhan5274
 */
public class FieldCallback implements ReflectionUtils.FieldCallback{
    private boolean idFound;

    @Override
    public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
        ReflectionUtils.makeAccessible(field);

        if(field.isAnnotationPresent(Id.class)){
            idFound = true;
        }
    }

    public boolean isIdFound(){
        return idFound;
    }
}
