package com.devendra.poc.opentelemetry.external;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.metrics.DoubleHistogram;
import io.opentelemetry.api.metrics.LongCounter;
import io.opentelemetry.api.metrics.Meter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Random;

@Service
public class ExternalServiceOne {
    private static RestTemplate restTemplate = new RestTemplate();

    private static final Meter meter = GlobalOpenTelemetry.getMeter("myservice");
    private static final LongCounter callCounter = meter.counterBuilder("app.external.requests").build();
    private static final DoubleHistogram callTime = meter.histogramBuilder("app.external.requests.time")
            .setUnit("ms")
            .build();

    private static final AttributeKey<String> key = AttributeKey.stringKey("type");

    public String call() {
        Attributes attributes = Attributes.of(key, new Random().nextBoolean() ? "Type1" : "Type2");
        callCounter.add(1, attributes);

        return restCall(attributes);
    }

    private static String restCall(Attributes attributes) {
        long startTime = System.currentTimeMillis();
        String result = restTemplate.getForObject("https://ron-swanson-quotes.herokuapp.com/v2/quotes", String.class);
        callTime.record(System.currentTimeMillis() - startTime, attributes);
        return result;
    }
}
