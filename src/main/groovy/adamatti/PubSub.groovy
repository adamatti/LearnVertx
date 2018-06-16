package adamatti

import groovy.util.logging.Slf4j
import io.vertx.core.Vertx
import io.vertx.core.eventbus.EventBus
import io.vertx.core.eventbus.Message
import io.vertx.core.impl.VertxImpl

@Slf4j
class PubSub {
    static void main(String [] args){
        log.info "Starting"
        new PubSub().run()
        log.info "Started"
    }

    void run(){
        Vertx vertx = Vertx.vertx()
        EventBus eventBus = vertx.eventBus()

        eventBus.consumer("sample.event"){ Message message ->
            log.info "Msg received: ${message.body()}"
        }

        vertx.setPeriodic(1000, { v ->
            eventBus.publish("sample.event", "Some news!")
        })
    }
}
