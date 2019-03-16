package com.nayanzin.edgeservice.zuul;

import com.google.common.util.concurrent.RateLimiter;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import static org.springframework.http.HttpStatus.TOO_MANY_REQUESTS;


/**
 * Quick Zuul filtering notes:
 * <p>
 * There are 4 types of Zuul filters:
 * pre filters are executed before the request is routed
 * routing filters can handle actual routing of the request
 * post filters are executed after the request has been routed
 * error filters execute if an error occurs in the course of handling the request
 * <p>
 * SpringCloud registers useful Zuul filters out of the box:
 * AuthenticationHeaderFilter (pre) - looks for requests that are being proxied and removes the authorization header
 * before it's sen downstream.
 * OAuth2TokenRelayFilter (pre) - a filter that propagates an OAuth access token if it's available in the incoming request.
 * ServletDetectionFilter (pre) - detects whether an HTTP servlet request has already run through the Zuul filter pipeline.
 * Servlet30WrapperFilter (pre) - wraps an incoming HTTP request with a Servlet 3.0 decorator.
 * FormBodyWrapperFilter (pre) - wraps an incoming HTTP request with a decorator containing metadata about the multipart file upload.
 * DebugFilter (pre) - contributes parameters to support debugging ig the request contains an Archaius property.
 * <p>
 * SendResponseFilter (post) - commits a response to the output.
 * SendErrorFilter (post) - if the reply contains error, this filter forwards it to a configurable endpoint.
 * SendForwardFilter (post) - if hte reply contains a forward, this filter sends the requisite forward.
 * <p>
 * SimpleHostRoutingFilter (route) - a filter tat takes the incoming request and decides, based on the URL, which node to route the proxied request to.
 * RibbonRoutingFilter (route) - a filter that takes the incoming request and decides, based on the URL, to which node to route the proxied request.
 * It does this using configurable routing and client-site load balancing powered by Netflix Ribbon.
 */
@Profile("throttled")
@Component
@RequiredArgsConstructor
public class ThrottlingZuulFilter extends ZuulFilter {

    @SuppressWarnings("UnstableApiUsage")
    private final RateLimiter rateLimiter;

    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return FilterConstants.PRE_DECORATION_FILTER_ORDER + 1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {

        RequestContext currentContext = RequestContext.getCurrentContext();

        if (!rateLimiter.tryAcquire()) {

            // One way
            currentContext.setSendZuulResponse(false);
            currentContext.setResponseStatusCode(TOO_MANY_REQUESTS.value());
            currentContext.setResponseBody(TOO_MANY_REQUESTS.getReasonPhrase());

            // And another way
            throw new ZuulException(TOO_MANY_REQUESTS.getReasonPhrase(), TOO_MANY_REQUESTS.value(), TOO_MANY_REQUESTS.getReasonPhrase());
        }

        return null;
    }
}
