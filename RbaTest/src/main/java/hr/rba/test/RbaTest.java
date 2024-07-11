package hr.rba.test;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@Slf4j
@EnableJpaRepositories("hr.rba.test.repository")
@EnableScheduling
@SpringBootApplication(scanBasePackages = {"hr.rba.test"})
@OpenAPIDefinition(info = @Info(title = "API Documentation", version = "1.0.0"))
public class RbaTest {

    @Autowired
    private Environment env;

    public static void main(String[] args) {
        new SpringApplicationBuilder()
                .sources(RbaTest.class)
                .run(args);
        openHomePage();
    }

    private static void openHomePage() {
        try {
            System.setProperty("java.awt.headless", "false");
            URI homepage = new URI("http://localhost:8081/api/v1/swagger-ui.html");
            Desktop.getDesktop().browse(homepage);
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }
    }
}
