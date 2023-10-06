package com.devendra.poc.opentelemetry.api;

import com.devendra.poc.opentelemetry.service.MyServiceUsingThreadPool;
import io.opentelemetry.instrumentation.annotations.WithSpan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class MyRestApi {

    private static final Logger logger = LoggerFactory.getLogger(MyRestApi.class);

    @Autowired
    private MyServiceUsingThreadPool myServiceUsingThreadPool;

    @GetMapping("/hello")
    public String sayHello() {
        logger.info("Test");
        return getMessage();
    }

    @PostMapping("/add")
    public String add(@RequestParam int num1, @RequestParam int num2) {
        int sum = num1 + num2;
        return "Sum: " + sum;
    }

    public String getMessage() {
        return generateMessage();
    }

    private String generateMessage() {
        return myServiceUsingThreadPool.generateRandomMessage();
    }
}
