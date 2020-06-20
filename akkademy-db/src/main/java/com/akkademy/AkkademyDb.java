package com.akkademy;

import akka.actor.AbstractActor;
import akka.actor.Status;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.pf.ReceiveBuilder;
import com.akkademy.exceptions.KeyNotFoundException;
import com.akkademy.messages.GetRequest;
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
        Receive receive;

        receive = receiveBuilder().match(SetRequest.class, message -> {
            log.info("Received set request - key: {} vale: {}", message.getKey(), message.getValue());
            map.put(message.getKey(), message.getValue());

        }).match(GetRequest.class, message -> {
            log.info("Received get request - {}", message);
            String value = (String)map.get(message.key);
            Object response = value != null ? value : new Status.Failure(new KeyNotFoundException(message.key));
            sender().tell(response, self());
        }).matchAny(object -> sender().tell(new Status.Failure(new ClassNotFoundException()), self())).build();

        return receive;
    }
}
