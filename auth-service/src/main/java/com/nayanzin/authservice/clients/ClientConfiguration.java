package com.nayanzin.authservice.clients;

import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.Optional;
import java.util.function.Function;

import static java.lang.String.format;

@Configuration
@RequiredArgsConstructor
public class ClientConfiguration {

    private final Log logger = LogFactory.getLog(getClass());
    private final LoadBalancerClient loadBalancerClient;
    private Function<Client, ClientDetails> mapClientToClientDetails;

    @Bean(name = "customClientDetails")
    ClientDetailsService customClientDetails(ClientJpaRepository clientJpaRepository) {
        return clientId -> clientJpaRepository.findByClientId(clientId)
                .map(mapClientToClientDetails)
                .orElseThrow(() -> new ClientRegistrationException(format("No client with clientId: {%s} registered", clientId)));
    }

    @PostConstruct
    public void postConstruct() {
        mapClientToClientDetails = client -> {
            // Compose client details.
            BaseClientDetails details = new BaseClientDetails(client.getClientId(),
                    null, client.getScopes(), client.getAuthorizedGrantTypes(),
                    client.getAuthorities());
            details.setClientSecret(client.getSecret());

            // Get load balanced greeting-client ip.
            String greetingsClientRedirectUri =
                    Optional.ofNullable(this.loadBalancerClient.choose("greeting-client"))
                            .map(si -> "http://" + si.getHost() + ":" + si.getPort() + "/")
                            .orElseThrow(() -> new ClientRegistrationException("Couldn't find and bind a greeting-client IP."));

            details.setRegisteredRedirectUri(Collections.singleton(greetingsClientRedirectUri));

            logger.info(format("clientDetailsService mapped Client:{%s} to ClientDetails:{%s}.", client, details));

            return details;
        };
    }
}
