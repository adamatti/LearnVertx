package adamatti

import groovy.util.logging.Slf4j
import io.vertx.core.Handler
import io.vertx.core.Vertx
import io.vertx.core.json.JsonObject
import io.vertx.kafka.client.consumer.KafkaConsumer
import io.vertx.kafka.client.consumer.KafkaConsumerRecord
import io.vertx.kafka.client.consumer.KafkaReadStream
import io.vertx.kafka.client.producer.KafkaProducer
import io.vertx.kafka.client.producer.KafkaProducerRecord
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer

@Slf4j
class KafkaMain {
    static void main(String [] args){
        log.info "Starting"
        new KafkaMain().run()
        log.info "Started"
    }

    private String topic = "Topic1"

    private void run(){
        Vertx vertx = Vertx.vertx()
        createConsumer(vertx)
        produceMessage(vertx)
    }

    private void createConsumer(Vertx vertx){
        def config = buildConfig()

        KafkaConsumer consumer = KafkaConsumer.create(vertx, config)

        Handler<KafkaConsumerRecord> handler = { KafkaConsumerRecord record ->
            log.info "Received - ${record.value()}"
        }

        consumer.subscribe(topic).handler(handler)
    }

    private void produceMessage(Vertx vertx){
        def config = buildConfig()

        KafkaProducer<String, String> producer = KafkaProducer.create(vertx, config)
        String payload = new Date().toGMTString()
        KafkaProducerRecord<String, String> record = KafkaProducerRecord.create(topic, payload)
        producer.write(record)
    }

    private Properties buildConfig(){
        [
            (ProducerConfig.BOOTSTRAP_SERVERS_CONFIG)     : "localhost:9092",
            (ProducerConfig.BATCH_SIZE_CONFIG)            : "50000",
            (ProducerConfig.LINGER_MS_CONFIG)             : "25",
            (ProducerConfig.COMPRESSION_TYPE_CONFIG)      : "snappy",
            (ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG)  : StringSerializer.class,
            (ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG): StringSerializer.class,
            (ConsumerConfig.GROUP_ID_CONFIG)              : "Test",
            (ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG): StringDeserializer.class,
            (ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG): StringDeserializer.class
        ]
    }
}
