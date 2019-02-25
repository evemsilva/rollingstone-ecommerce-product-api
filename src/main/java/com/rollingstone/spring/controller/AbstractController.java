package com.rollingstone.spring.controller;

import com.rollingstone.exceptions.HTTP404Exception;
import java.util.Optional;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;

public abstract class AbstractController
		implements ApplicationEventPublisherAware {

    ApplicationEventPublisher eventPublisher;

    static final String DEFAULT_PAGE_NUMBER = "0";
    static final String DEFAULT_PAGE_SIZE   = "20";

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher eventPublisher) {
	this.eventPublisher = eventPublisher;
    }

    static <T> void checkResourceFound(final T resource) {
	Optional.ofNullable(resource).orElseThrow(() -> new HTTP404Exception("Resource Not Found"));
    }

}
