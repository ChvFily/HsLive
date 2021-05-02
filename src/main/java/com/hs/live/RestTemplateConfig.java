package com.hs.live;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;


@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate(ClientHttpRequestFactory clientHttpRequestFactory){
        return new RestTemplate(clientHttpRequestFactory);
    }

    @Bean
    public ClientHttpRequestFactory clientHttpRequestFactory() {
    	System.out.println("ClientHttpRequestFactory");
        SSL factory = new SSL();
        factory.setReadTimeout(5000);
        factory.setConnectTimeout(15000);
        return factory;
    }
    
    

}
