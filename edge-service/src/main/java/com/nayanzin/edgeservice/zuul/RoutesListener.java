package com.nayanzin.edgeservice.zuul;

import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.event.HeartbeatEvent;
import org.springframework.cloud.netflix.zuul.RoutesRefreshedEvent;
import org.springframework.cloud.netflix.zuul.filters.RouteLocator;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoutesListener {

    private final RouteLocator routeLocator;

    private final DiscoveryClient discoveryClient;

    private final Log logger = LogFactory.getLog(getClass());

    @EventListener(HeartbeatEvent.class)
    public void onHeartbeatEvent(HeartbeatEvent heartbeatEvent) {
        logger.debug("onHeartBeatEvent: " + heartbeatEvent);
        discoveryClient.getServices().forEach(logger::info);
    }

    @EventListener(RoutesRefreshedEvent.class)
    public void onRouteRefreshedEvent(RoutesRefreshedEvent routesRefreshedEvent) {
        logger.info("onRouteRefreshedEvent: " + routesRefreshedEvent);
        routeLocator.getRoutes().forEach(logger::info);
    }
}
