package com.rollingstone.spring.controller;

import com.rollingstone.exceptions.HTTP400Exception;
import com.rollingstone.exceptions.HTTP404Exception;
import com.rollingstone.exceptions.RestAPIExceptionInfo;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Metrics;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

public abstract class AbstractController
		implements ApplicationEventPublisherAware {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    ApplicationEventPublisher eventPublisher;

    static final String DEFAULT_PAGE_NUMBER = "0";
    static final String DEFAULT_PAGE_SIZE   = "20";

    private final Counter http400ExceptionCounter = Metrics.counter("com.rollingstone.ProductController.HTTP400");
    private final Counter http404ExceptionCounter = Metrics.counter("com.rollingstone.ProductController.HTTP404");

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HTTP400Exception.class)
    @ResponseBody
    public RestAPIExceptionInfo handleBadRequestException(HTTP400Exception ex, WebRequest request, HttpServletResponse response) {
	log.info("Received Bad Request Exception" + ex.getLocalizedMessage());
	http400ExceptionCounter.increment();
	return new RestAPIExceptionInfo(ex.getLocalizedMessage(), "The Request did not have the correct paramaters");
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(HTTP404Exception.class)
    @ResponseBody
    public RestAPIExceptionInfo handleResourceNotFoundException(HTTP404Exception ex, WebRequest request, HttpServletResponse response) {
	log.info("Received Recource Not Found Exception: " + ex.getLocalizedMessage());
	http404ExceptionCounter.increment();
	return new RestAPIExceptionInfo(ex.getLocalizedMessage(), "The Requested Resource was not found");
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher eventPublisher) {
	this.eventPublisher = eventPublisher;
    }

    static <T> void checkResourceFound(final T resource) {
	if (resource == null) {
	    throw new HTTP404Exception("Resource Not Found");
	}
    }

}
