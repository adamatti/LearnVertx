package adamatti

import groovy.util.logging.Slf4j
import io.vertx.core.buffer.Buffer
import io.vertx.core.http.HttpClientResponse

import static io.vertx.core.Vertx.vertx

// based on https://stackoverflow.com/questions/36417923/calling-invoking-consuming-rest-apis-using-vertx
@Slf4j
class HttpClientMain {
    static void main(String [] args){
        log.info "Starting"
        new HttpClientMain().run()
        log.info "Started"
    }

    private void run(){
        String url = "http://services.groupkt.com/country/get/iso2code/BR"

        def client = vertx().createHttpClient()

        client.getAbs(url){ HttpClientResponse response ->
            response.bodyHandler { Buffer b ->
                log.info b.toString()
            }
        }.end()
    }
}
