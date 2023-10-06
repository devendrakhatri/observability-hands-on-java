package com.devendra.poc.opentelemetry.service;

import com.devendra.poc.opentelemetry.external.ExternalServiceOne;
import io.opentelemetry.instrumentation.annotations.SpanAttribute;
import io.opentelemetry.instrumentation.annotations.WithSpan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.*;

@Service
public class MyServiceUsingThreadPool {
    private static final Logger logger = LoggerFactory.getLogger(MyServiceUsingThreadPool.class);
    private static ExecutorService executorService = Executors.newFixedThreadPool(5);

    @Autowired
    private ExternalServiceOne externalServiceOne;

    public String generateRandomMessage() {

        Callable<String> task = this::generate;

        Future<String> future = executorService.submit(task);
        try {
            return future.get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    private String generate() {
        logger.info("Another thread");
        return magicValue(UUID.randomUUID().toString());
    }

    @WithSpan
    private String magicValue(@SpanAttribute String uuid) {
        return externalServiceOne.call();
    }
}
