package com.nayanzin.edgeservice.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.netflix.zuul.filters.RouteLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableZuulProxy
public class ZuulConfiguration {

    @Bean
    CommandLineRunner commandLineRunner(RouteLocator zuulRouteLocator) {
        Log logger = LogFactory.getLog(getClass());
        logger.info("Zuul RouteLocator routes: ");
        return args -> zuulRouteLocator.getRoutes().forEach(
                route -> logger.info(String.format("%s (%s) %s", route.getId(), route.getLocation(), route.getFullPath())));
    }
}
