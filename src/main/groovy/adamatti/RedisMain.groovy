package adamatti

import groovy.util.logging.Slf4j
import static io.vertx.core.Vertx.vertx
import io.vertx.redis.RedisClient

@Slf4j
class RedisMain {
    static void main(String [] args){
        log.info "Staring"
        new RedisMain().run()
        log.info "Started"
    }

    private void run(){
        def client = RedisClient.create(vertx(), [
            host:"localhost"
        ])

        setKey(client)
    }

    private void setKey(RedisClient client){
        client.set("key", "value", { r ->
            if (r.succeeded()) {
                log.info("key stored")
                readKey(client)
            } else {
                log.info("Connection or Operation Failed ${r.cause()}")
            }
        })
    }

    private void readKey(RedisClient client){
        client.get("key", { s ->
            log.info("Retrieved value: ${s.result()}")
        })
    }
}
