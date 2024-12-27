package com.codingshuttle.SecurityApp.SecurityApplication.filters;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.List;
import java.util.Map;


@Component
@Order(1)
public class LoggingFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(LoggingFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        long startTime = System.currentTimeMillis();
        String requestURI = request.getRequestURI();
        String method = request.getMethod();

        // Wrap request and response for content capture
        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);

        try {


            // Proceed with the filter chain
            filterChain.doFilter(wrappedRequest, wrappedResponse);

             // Log the incoming request details
            logRequestDetails(wrappedRequest);

        } catch (Exception e) {
            logger.error("Error processing request: {} {}", method, requestURI, e);
            throw e;
        } finally {
            long elapsedTime = System.currentTimeMillis() - startTime;

            // Log the outgoing response details
            logResponseDetails(wrappedResponse, elapsedTime);

            //response body is copied back
            wrappedResponse.copyBodyToResponse();
        }
    }

    private void logRequestDetails(ContentCachingRequestWrapper request) throws UnsupportedEncodingException {
        request.getParameterMap();
        String body = getRequestBody(request);

        logger.info("=== Incoming Request ===");
        logger.info("HTTP Method: {}", request.getMethod());
        logger.info("Request URI: {}", request.getRequestURI());
        logger.info("Query Parameters: {}", getQueryParams(request));
        getHeaders(request);
        logger.info("Body: {}",body);
    }

    private void logResponseDetails(ContentCachingResponseWrapper response, long elapsedTime) throws UnsupportedEncodingException {
        logger.info("=== Outgoing Response ===");
        logger.info("HTTP Status: {}", response.getStatus());
        getHeaders(response);
        logger.info("Body: {}", getResponseBody(response));
        logger.info("Processing Time: {} ms", elapsedTime);
    }

    private String getQueryParams(HttpServletRequest request) {
        StringBuilder params = new StringBuilder();
        Map<String, String[]> parameterMap = request.getParameterMap();
        parameterMap.forEach((key, values) -> {
            params.append(key).append("=").append(String.join(",", values)).append("&");
        });
        return params.toString().isEmpty() ? "None" : params.toString();
    }


    private void getHeaders(HttpServletRequest request){
        List<String> headerNames= Collections.list(request.getHeaderNames());
        headerNames.forEach( headerName ->{
           String headerValue=request.getHeader(headerName);
           logger.info("Header: {} = {}", headerName, headerValue);
        });

    }

   private void getHeaders(HttpServletResponse response){

        response.getHeaderNames().forEach(headerName->{
            response.getHeaders(headerName).forEach(headerValue ->
                logger.info("Header: {} = {}", headerName, headerValue)
            );
        });
   }



    private String getRequestBody(ContentCachingRequestWrapper request) throws UnsupportedEncodingException {
        byte[] content = request.getContentAsByteArray();
        return content.length > 0 ? new String(content, request.getCharacterEncoding()) : "No body content";
    }

    private String getResponseBody(ContentCachingResponseWrapper response) throws UnsupportedEncodingException {
        byte[] content = response.getContentAsByteArray();
        return content.length > 0 ? new String(content, response.getCharacterEncoding()) : "No body content";
    }
}













/*
@Component
@Order(1)
public class LoggingFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(LoggingFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Create a wrapper to allow multiple reads of the request body
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

        logger.info("=== Incoming Request ===");
        logger.info("HTTP Method: {}", requestWrapper.getMethod());
        logger.info("Request URI: {}", requestWrapper.getRequestURI());

        // Log Query Parameters
        String queryString = requestWrapper.getQueryString();
        logger.info("Query Parameters: {}", queryString != null ? queryString : "None");

        // Log Request Headers
        logger.info("=== Request Headers ===");
        Collections.list(requestWrapper.getHeaderNames()).forEach(headerName ->
                logger.info("Header: {} = {}", headerName, requestWrapper.getHeader(headerName))
        );

        // Proceed with the filter chain first
        filterChain.doFilter(requestWrapper, responseWrapper);

        // Log Request Body
        String requestBody = new String(requestWrapper.getContentAsByteArray(), requestWrapper.getCharacterEncoding());
        logger.info("Request Body: {}", StringUtils.isBlank(requestBody) ? "No body content" : requestBody);

        // Log Response Headers
        logger.info("=== Response Headers ===");
        responseWrapper.getHeaderNames().forEach(headerName ->
                logger.info("Header: {} = {}", headerName, responseWrapper.getHeader(headerName))
        );

        // Log Response Status
        logger.info("Response Status: {}", responseWrapper.getStatus());

        // Log Response Body
        byte[] responseArray = responseWrapper.getContentAsByteArray();
        String responseBody = new String(responseArray, responseWrapper.getCharacterEncoding());
        logger.info("Response Body: {}", StringUtils.isBlank(responseBody) ? "No response body" : responseBody);

        // Copy content to actual response - this is crucial!
        responseWrapper.copyBodyToResponse();
    }
}
*/
