package com.akkademy;

import akka.actor.AbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.akkademy.messages.SetRequest;

import java.util.HashMap;
import java.util.Map;

public class AkkademyDb extends AbstractActor {
    protected final LoggingAdapter log = Logging.getLogger(context().system(), this);
    protected final Map<String, Object> map = new HashMap<>();

    private AkkademyDb() {

    }

    @Override
    public Receive createReceive() {
        return receiveBuilder().match(SetRequest.class, message -> {
            log.info("Received set request - key: {} vale: {}", message.getKey(), message.getValue());
            map.put(message.getKey(), message.getValue());

        }).matchAny(object -> log.info("Received unknown message {}", 0)).build();
    }
}
