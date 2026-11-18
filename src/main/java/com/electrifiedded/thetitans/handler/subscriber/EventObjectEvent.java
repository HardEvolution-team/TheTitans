package com.electrifiedded.thetitans.handler.subscriber;

import com.electrifiedded.thetitans.handler.EventEntry;

import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

public class EventObjectEvent extends Event {
    EventEntry objEvent;

    public EventObjectEvent(EventEntry objEvent) {
        this.objEvent = objEvent;
    }

    @Cancelable
    public static class EventDead extends EventObjectEvent {
        public EventDead(EventEntry objEvent) {
            super(objEvent);
        }
    }
}
