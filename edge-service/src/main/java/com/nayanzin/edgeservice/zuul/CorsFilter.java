package com.nayanzin.edgeservice.zuul;

import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Profile;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.*;

@Profile("cors")
@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 10)
@RequiredArgsConstructor
public class CorsFilter implements Filter {

    private final DiscoveryClient discoveryClient;

    private final Log logger = LogFactory.getLog(getClass());

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        // Cast request and response
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        // Check if the request came from the allowed origin
        String originHeaderValue = originFor(request);
        if (isClientAllowed(originHeaderValue)) {
            response.setHeader(ACCESS_CONTROL_ALLOW_ORIGIN, originHeaderValue);
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void init(FilterConfig filterConfig) {
        logger.info("Initializing CorsFilter.");
    }

    @Override
    public void destroy() {
        logger.info("Destroying CorsFilter.");
    }

    private String originFor(HttpServletRequest request) {
        logger.debug("Extracting Origin header value from the request: " + request);
        return StringUtils.hasText(request.getHeader(ORIGIN))
                ? request.getHeader(ORIGIN)
                : request.getHeader(REFERER);
    }


    private boolean isClientAllowed(String origin) {
        logger.debug("Checking if origin: {" + origin + "} is allowed");
        if (!StringUtils.hasText(origin)) {
            logger.debug("Not allowed. Origin: {" + origin + "} does not contain text.");
            return false;
        }

        URI originUri = URI.create(origin);
        int port = originUri.getPort() <= 0 ? 80 : originUri.getPort();
        String match = originUri.getHost() + ':' + port;

        boolean allowed = discoveryClient.getServices()
                .stream()
                .flatMap(service -> discoveryClient.getInstances(service).stream())
                .map(serviceInstance -> serviceInstance.getHost() + ':' + serviceInstance.getPort())
                .anyMatch(instance -> instance.equalsIgnoreCase(match));

        if (allowed) {
            logger.debug("Origin: {" + origin + "} is Allowed.");
            return true;
        } else {
            List<String> serviceInstances = discoveryClient.getServices()
                    .stream()
                    .flatMap(service -> discoveryClient.getInstances(service).stream())
                    .map(serviceInstance -> serviceInstance.getHost() + ':' + serviceInstance.getPort())
                    .collect(Collectors.toList());
            logger.debug("Not allowed. Origin: {" + origin + "} does not match any registered serviceInstances: " + serviceInstances);
            return false;
        }
    }
}
