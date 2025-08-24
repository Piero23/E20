package com.example.donut;

import brave.Tracing;
import brave.sampler.Sampler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import zipkin2.reporter.AsyncReporter;
import zipkin2.reporter.Sender;
import zipkin2.reporter.urlconnection.URLConnectionSender;

// docker run -d -p 9411:9411 openzipkin/zipkin
@Configuration
public class BraveConfig {

    @Bean
    public Sender zipkinSender() {
        return URLConnectionSender.create("http://localhost:9411/api/v2/spans");
    }

    @Bean
    public AsyncReporter<zipkin2.Span> spanReporter(Sender sender) {
        return AsyncReporter.create(sender);
    }

    @Bean
    public Tracing tracing(AsyncReporter<zipkin2.Span> reporter) {
        return Tracing.newBuilder()
                .localServiceName("donut-backend")
                .spanReporter(reporter)
                .sampler(Sampler.ALWAYS_SAMPLE)
                .build();
    }
}