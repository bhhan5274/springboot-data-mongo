package com.bhhan.mongo.converter;

import org.springframework.core.convert.converter.Converter;

import java.time.ZonedDateTime;
import java.util.Date;

/**
 * Created by hbh5274@gmail.com on 2020-09-24
 * Github : http://github.com/bhhan5274
 */
public class ZonedDateTimeWriteConverter implements Converter<ZonedDateTime, Date> {
    @Override
    public Date convert(ZonedDateTime zonedDateTime) {
        return Date.from(zonedDateTime.toInstant());
    }
}
