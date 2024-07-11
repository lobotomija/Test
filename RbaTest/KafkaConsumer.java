import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class KafkaConsumer {

    private static final String TOPIC = "card-status";
    private static final String BOOTSTRAP_SERVERS = "localhost:9092";
    private static final String GROUP_ID = "rba-group";

    public static void main(String[] args) {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Collections.singletonList(TOPIC));

        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(1));
            for (ConsumerRecord<String, String> record : records) {
                System.out.printf("Received record(key=%s value=%s) meta(partition=%d, offset=%d)\n",
                        record.key(), record.value(), record.partition(), record.offset());
                // URL Swagger API-a
                String apiUrl = "http://localhost:8081/api/v1/card-request/status";

                // Kreiraj HTTP klijent
                HttpClient client = HttpClient.newHttpClient();

                // Kreiraj GET zahtjev
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(apiUrl);
                        .PUT()
                        .build();

                // Pošalji zahtjev i obradi odgovor
                try {
                    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString(record.value()));
                    if (response.statusCode() == 200) {
                        System.out.println("Odgovor: " + response.body());
                    } else {
                        System.out.println("Zahtjev nije bio uspješan: " + response.statusCode());
                    }
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}