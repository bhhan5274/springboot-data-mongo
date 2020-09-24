package com.bhhan.mongo.aggregation.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

/**
 * Created by hbh5274@gmail.com on 2020-09-24
 * Github : http://github.com/bhhan5274
 */

@Getter
@Setter
public class StatePopulation {
    @Id
    private String state;
    private Integer statePop;

    @Override
    public String toString() {
        return "StatePopulation{" +
                "state='" + state + '\'' +
                ", statePop=" + statePop +
                '}';
    }
}
