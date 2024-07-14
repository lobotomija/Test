package hr.rba.test;

import hr.rba.test.component.InitData;
import hr.rba.test.kafka.KafkaStatusConsumer;
import hr.rba.test.kafka.KafkaStatusProducer;
import hr.rba.test.kafka.KafkaTopicCreator;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@Slf4j
@EnableJpaRepositories("hr.rba.test.repository")
@SpringBootApplication(scanBasePackages = {"hr.rba.test"})
@OpenAPIDefinition(info = @Info(title = "Card creation API", version = "1.0.0"))
public class RbaTest {

    public static void main(String[] args) {
        ApplicationContext context = new SpringApplicationBuilder()
                .sources(RbaTest.class)
                .run(args);

        if (!context.getEnvironment().acceptsProfiles("test")) {
            new KafkaTopicCreator();
            new KafkaStatusConsumer();
            new KafkaStatusProducer();
        }
        openHomePage();
    }

    private static void openHomePage() {
        try {
            System.setProperty("java.awt.headless", "false");
            URI homepage = new URI("http://localhost:8081/v1/swagger-ui.html");
            Desktop.getDesktop().browse(homepage);
        } catch (URISyntaxException | IOException e) {
            LOGGER.error(e.getStackTrace().toString());
        }
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {
            InitData initData = ctx.getBean(InitData.class);
            initData.initData();
        };
    }
}
