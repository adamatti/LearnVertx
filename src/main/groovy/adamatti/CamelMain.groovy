package adamatti

import groovy.util.logging.Slf4j
import io.vertx.camel.CamelBridge
import io.vertx.camel.CamelBridgeOptions
import io.vertx.camel.InboundMapping
import io.vertx.camel.OutboundMapping
import io.vertx.core.Vertx
import io.vertx.core.eventbus.Message
import org.apache.camel.CamelContext
import org.apache.camel.Exchange
import org.apache.camel.ProducerTemplate
import org.apache.camel.builder.RouteBuilder
import org.apache.camel.impl.DefaultCamelContext
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class CamelMain extends RouteBuilder {
    private static final Logger log = LoggerFactory.getLogger(CamelMain.class)

    static void main(String [] args){
        log.info "Starting"
        new CamelMain().run()
        log.info "Started"
    }

    private CamelContext camelContext = new DefaultCamelContext()
    private ProducerTemplate producerTemplate = camelContext.createProducerTemplate()

    void configure() throws Exception {
        from("direct:event1").process { Exchange exchange ->
            log.info "Msg received - event1"
            producerTemplate.sendBody("direct:event2", "abc")
        }
    }

    void run(){
        camelContext.addRoutes(this)
        camelContext.start()

        Vertx vertx = Vertx.vertx()

        vertx.eventBus().consumer("direct:event2") { Message message ->
            log.info "Msg received - event2"
        }

        CamelBridge.create(vertx, new CamelBridgeOptions(camelContext)
            .addOutboundMapping(fromVertxToCamel("direct:event1"))
            .addInboundMapping(fromCamelToVertx("direct:event2"))
        ).start()

        vertx.eventBus().send("direct:event1", "abc")
    }

    private InboundMapping fromCamelToVertx(String endpoint){
        InboundMapping
            .fromCamel(endpoint)
            .toVertx(endpoint)
    }

    private OutboundMapping fromVertxToCamel(String endpoint){
        OutboundMapping
            .fromVertx(endpoint)
            .toCamel(endpoint)
    }

}
