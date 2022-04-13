package io.github.viakiba.hinx.eventbus.vertx;

import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;

public class VertxEventBus {

    // https://vertx.io/docs/vertx-core/java/#_the_event_bus_api
    public static void main(String[] args) {
        EventBus eventBus = Vertx.vertx().eventBus();
        eventBus.localConsumer("test", msg -> {
            System.out.println("receive msg: " + msg.body());
        });
        eventBus.localConsumer("test", msg -> {
            System.out.println("receive msg1: " + msg.body());
        });
        // 1 对多 发布消息
        eventBus.publish("test", "hello world1");
        // 一对一 发布消息
        eventBus.send("test", "hello world");
    }

}
