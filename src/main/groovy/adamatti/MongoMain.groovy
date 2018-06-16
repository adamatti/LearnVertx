package adamatti

import groovy.util.logging.Slf4j
import static io.vertx.core.Vertx.vertx
import io.vertx.ext.mongo.MongoClient

@Slf4j
class MongoMain {
    static void main(String [] args){
        log.info "Starting"
        new MongoMain().run()
        log.info "Started"
        System.in.read()
    }

    private void run(){
        def mongoClient = MongoClient.createShared(vertx(), [
            connection_string:"mongodb://localhost:27017",
            db_name:"test"
        ])
        insert(mongoClient)
    }

    private void insert(MongoClient mongoClient){
        def product1 = [
            itemId:"12345",
            name:"Cooler",
            price:"100.0"
        ]

        mongoClient.save("products", product1, { id ->
            log.info("Inserted id: ${id.result()}")
            find(mongoClient)
        })
    }

    private void find(MongoClient mongoClient){
        mongoClient.find("products", [
            itemId:"12345"
        ], { res ->
            log.info("Name is ${res.result()[0].name}")

            remove(mongoClient)
        })
    }

    private void remove(MongoClient mongoClient){
        mongoClient.remove("products", [
            itemId:"12345"
        ], { rs ->
            if (rs.succeeded()) {
                log.info("Product removed ")
            }
        })
    }
}
