package hr.rba.test;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
public class ValidatorConfig {

    @Bean
    public Validator validator() {
        return new LocalValidatorFactoryBean();
    }
}