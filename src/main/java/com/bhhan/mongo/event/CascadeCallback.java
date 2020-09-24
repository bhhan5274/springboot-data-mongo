package com.bhhan.mongo.event;

import com.bhhan.mongo.annotation.CascadeSave;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mapping.MappingException;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;

/**
 * Created by hbh5274@gmail.com on 2020-09-24
 * Github : http://github.com/bhhan5274
 */

@Getter
@Setter
public class CascadeCallback implements ReflectionUtils.FieldCallback {

    private Object source;
    private MongoOperations mongoOperations;

    public CascadeCallback(Object source, MongoOperations mongoOperations) {
        this.source = source;
        this.mongoOperations = mongoOperations;
    }

    @Override
    public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
        ReflectionUtils.makeAccessible(field);

        if(field.isAnnotationPresent(DBRef.class)
            && field.isAnnotationPresent(CascadeSave.class)){
            Object fieldValue = field.get(source);

            if(fieldValue != null){
                FieldCallback callback = new FieldCallback();
                ReflectionUtils.doWithFields(fieldValue.getClass(), callback);

                if(!callback.isIdFound()){
                    throw new MappingException("Cannot perform cascade save on child object without id set");
                }

                mongoOperations.save(fieldValue);
            }
        }
    }
}
