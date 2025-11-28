package com.ccblog.disruptor.readEvent;

import com.ccblog.event.ReadEvent;
import com.lmax.disruptor.EventFactory;

public class ReadEventFactory implements EventFactory<ReadEvent> {
    @Override
    public ReadEvent newInstance() {
        return new ReadEvent();
    }
}