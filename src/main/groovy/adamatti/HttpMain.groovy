package adamatti

import groovy.util.logging.Slf4j
import io.vertx.core.Vertx
import io.vertx.ext.web.Router

@Slf4j
class HttpMain {
    static void main(String [] args){
        log.info "Starting"
        new HttpMain().run()
        log.info "Started"
    }

    void run(){
        def vertx = Vertx.vertx()
        Router router = Router.router(vertx)

        router.get("/").handler{req ->
            req.response()
                .putHeader("content-type", "text/plain")
                .end("Hello from Vert.x!")
        }

        vertx.createHttpServer().requestHandler(router.&accept).listen(8080)
    }
}
