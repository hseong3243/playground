package com.seong.playground.testdouble.stub;

import com.seong.playground.testdouble.Event;
import com.seong.playground.testdouble.EventPublisher;

public class SpyEventPublisher implements EventPublisher {

    public int count = 0;

    @Override
    public void publish(Event event) {
        count++;
    }
}
