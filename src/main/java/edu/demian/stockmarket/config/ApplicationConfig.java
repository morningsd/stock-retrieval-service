package edu.demian.stockmarket.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ApplicationConfig {

  @Bean
  public RestTemplate restTemplate(RestTemplateBuilder builder) {
    // Do any additional configuration here
    return builder.build();
  }

}
