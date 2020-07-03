package com.akkademy;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.cluster.Cluster;
import akka.cluster.ClusterEvent;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import java.util.ArrayList;
import java.util.List;

public class ClusterController extends AbstractActor {
  protected final LoggingAdapter log = Logging.getLogger(context().system(), this);
  Cluster cluster = Cluster.get(getContext().system());
  List<ActorRef> workers = new ArrayList<>();

  @Override
  public void preStart() throws Exception, Exception {
    cluster.subscribe(
        self(),
        (ClusterEvent.SubscriptionInitialStateMode) ClusterEvent.initialStateAsEvents(),
        ClusterEvent.MemberEvent.class,
        ClusterEvent.UnreachableMember.class);
  }

  @Override
  public void postStop() throws Exception, Exception {
    cluster.unsubscribe(self());
  }

  @Override
  public Receive createReceive() {
    return receiveBuilder()
        .match(
            ClusterEvent.MemberEvent.class,
            message -> {
              if (message.getClass() == ClusterEvent.MemberUp.class) {
                System.out.println("member up: " + message.member().address());
              }
              log.info("MemberEvent: {}", message);
            })
        .match(
            ClusterEvent.UnreachableMember.class,
            message -> {
              log.info("UnreachableMember: {}", message);
            })
        .build();
  }
}
