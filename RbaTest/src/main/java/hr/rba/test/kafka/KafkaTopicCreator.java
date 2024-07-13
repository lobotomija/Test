package hr.rba.test.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;

import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

@Slf4j
public class KafkaTopicCreator implements Runnable {

    public KafkaTopicCreator() {
        Thread thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run()
    {
        try {
            Thread.sleep(5000);
        } catch (Exception e) {

        }

        // Kafka broker properties
        Properties props = new Properties();
        props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");

        // Create AdminClient
        try (AdminClient adminClient = AdminClient.create(props)) {
            // Check if the topic exists
            if (!topicExists(adminClient, "card-status")) {
                // Create a new topic
                createTopic(adminClient, "card-status", 1, (short) 1);
                LOGGER.info("Topic 'card-status' created successfully.");
            } else {
                LOGGER.info("Topic 'card-status' already exists.");
            }
        } catch (Exception e) {
            LOGGER.error(e.getStackTrace().toString());
        }
    }

    private static boolean topicExists(AdminClient adminClient, String topicName) throws ExecutionException, InterruptedException {
        return adminClient.listTopics().names().get().contains(topicName);
    }

    private static void createTopic(AdminClient adminClient, String topicName, int partitions, short replicationFactor) throws ExecutionException, InterruptedException {
        NewTopic newTopic = new NewTopic(topicName, partitions, replicationFactor);
        adminClient.createTopics(Collections.singleton(newTopic)).all().get();
    }
}