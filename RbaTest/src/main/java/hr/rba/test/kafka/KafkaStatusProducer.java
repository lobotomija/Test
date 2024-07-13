package hr.rba.test.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

@Slf4j
public class KafkaStatusProducer implements Runnable{

    private static final String TOPIC = "card-status";
    private static final String BOOTSTRAP_SERVERS = "localhost:9092";

    public KafkaStatusProducer()
    {
        Thread thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        try {
            Thread.sleep(10000);
        } catch (Exception e) {

        }
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        KafkaProducer<String, String> producer = new KafkaProducer<>(props);

        while (true) {
            try {
                ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC, "status", "{\"oib\": \"12345678901\", \"status\": \"U koverti\"}");
                RecordMetadata metadata = producer.send(record).get();
                LOGGER.info("Sent record(key=%s value=%s) meta(partition=%d, offset=%d)\n",
                        record.key(), record.value(), metadata.partition(), metadata.offset());
                Thread.sleep(60000);
            } catch (InterruptedException | ExecutionException e) {
                LOGGER.error(e.getStackTrace().toString());
            }
        }
    }
}