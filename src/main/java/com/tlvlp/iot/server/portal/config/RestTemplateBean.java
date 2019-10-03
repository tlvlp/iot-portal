package com.tlvlp.iot.server.portal.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateBean {

    private Properties properties;

    public RestTemplateBean(Properties properties) {
        this.properties = properties;
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
//                .basicAuthentication(
//                        properties.getAPI_GATEWAY_SECURITY_USER_BACKEND(),
//                        properties.getAPI_GATEWAY_SECURITY_PASS_BACKEND())
                .build();
    }
}
