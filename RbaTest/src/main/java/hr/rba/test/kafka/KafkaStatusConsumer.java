package hr.rba.test.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.io.IOException;
import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

@Slf4j
public class KafkaStatusConsumer implements Runnable {

    private static final String TOPIC = "card-status";


    public KafkaStatusConsumer()
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
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "consumer-rba-group-1");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "10000");
        props.put(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG, "3000");
        props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, "300000");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Collections.singletonList(TOPIC));

        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(1));
            for (ConsumerRecord<String, String> record : records) {
                System.out.printf("Received record(key=%s value=%s) meta(partition=%d, offset=%d)\n",
                        record.key(), record.value(), record.partition(), record.offset());
                String apiUrl = "http://localhost:8081/api/v1/card-request/status";
                CloseableHttpClient httpClient = HttpClients.createDefault();

                try {
                    HttpPut putRequest = new HttpPut(apiUrl);
                    putRequest.setHeader("Content-Type", "application/json");

                    String json = record.value();
                    StringEntity entity = new StringEntity(json);
                    putRequest.setEntity(entity);

                    CloseableHttpResponse response = httpClient.execute(putRequest);

                    try {
                        System.out.println(response.getStatusLine());
                        HttpEntity responseEntity = response.getEntity();
                        if (responseEntity != null) {
                            String result = EntityUtils.toString(responseEntity);
                            LOGGER.info(result);
                        }
                    } finally {
                        response.close();
                    }
                } catch (IOException e) {
                    LOGGER.error(e.getStackTrace().toString());
                } finally {
                    try {
                        httpClient.close();
                    } catch (IOException e) {
                        LOGGER.error(e.getStackTrace().toString());
                    }
                }
            }
        }
    }
}