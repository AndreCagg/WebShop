package it.webshop.conf;

import org.springframework.context.annotation.Configuration;

import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;

@Configuration
public class ResponseConf implements WebFluxConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry corsRegistry) {
        corsRegistry.addMapping("/**")
          .allowedOrigins("http://localhost:9090")
          .allowedMethods("PUT", "POST", "GET", "PATCH", "DELETE", "OPTIONS")
          .maxAge(3600);
    }

}
