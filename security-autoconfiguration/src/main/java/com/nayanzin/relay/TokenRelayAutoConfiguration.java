package com.nayanzin.relay;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoRestTemplateFactory;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.web.client.RestTemplate;

import static org.springframework.cloud.security.oauth2.client.feign.OAuth2FeignRequestInterceptor.AUTHORIZATION;

@Configuration
@ConditionalOnWebApplication
@ConditionalOnClass(EnableResourceServer.class)
public class TokenRelayAutoConfiguration {

    private static final String SECURE_PROFILE = "secure";

    private TokenRelayAutoConfiguration() {
        throw new IllegalStateException("Utility class.");
    }

    /**
     * Plain load-balanced RestTemplate in the non secure profile.
     */
    @Configuration
    @Profile("!" + SECURE_PROFILE)
    public static class RestTemplateConfiguration {

        @Bean
        @LoadBalanced
        public RestTemplate simpleRestTemplate() {
            return new RestTemplate();
        }
    }


    /**
     * Load-balanced RestTemplate that will propagate an OAuth token in the secure profile.
     */
    @Configuration
    @Profile(SECURE_PROFILE)
    public static class SecureRestTemplateConfiguration {

        @LoadBalanced
        @Bean
        public OAuth2RestTemplate anOAuth2RestTemplate(UserInfoRestTemplateFactory factory) {
            return factory.getUserInfoRestTemplate();
        }
    }


    /**
     * Propagate OAuth access tokens across Feign invocations.
     */
    @Configuration
    @Profile(SECURE_PROFILE)
    @ConditionalOnClass(RequestInterceptor.class)
    @ConditionalOnBean(OAuth2ClientContextFilter.class)
    public static class FeignAutoConfiguration {

        @Bean
        public RequestInterceptor requestInterceptor(@Qualifier("oauth2ClientContext") OAuth2ClientContext clientContext) {
            return requestTemplate -> requestTemplate
                    .header(AUTHORIZATION, clientContext.getAccessToken().getTokenType() + ' ' + clientContext.getAccessToken().getValue());
        }
    }

}
