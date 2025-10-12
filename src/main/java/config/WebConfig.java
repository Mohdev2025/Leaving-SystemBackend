package config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.context.annotation.Bean;
 import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

 
@Configuration
public class WebConfig implements WebMvcConfigurer {
    
    // CORS configuration
    @Override
    public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/api/**")
    .allowedOrigins("http://localhost:4200")
    .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
    .allowedHeaders("*")
    .allowCredentials(true)
    .maxAge(3600);
}

 


@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {
        HttpComponentsClientHttpRequestFactory requestFactory = 
            new HttpComponentsClientHttpRequestFactory();
        
        requestFactory.setConnectTimeout(5000);
        requestFactory.setConnectionRequestTimeout(5000);
        
        return new RestTemplate(requestFactory);
    }
}
 
}