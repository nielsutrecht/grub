package com.nibado.project.grub.configuration;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.io.IOException;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class ApplicationConfiguration extends WebMvcConfigurerAdapter {
    @Autowired
    private AuthInterceptor authInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor);
    }

    @Bean
    public Jackson2ObjectMapperBuilder jacksonBuilder() {
        Jackson2ObjectMapperBuilder b = new Jackson2ObjectMapperBuilder();

        b.serializerByType(Duration.class, durationSerializer());
        b.serializerByType(ZonedDateTime.class, zonedDateTimeSerializer());
        return b;
    }

    public static JsonSerializer<Duration> durationSerializer() {
        return new JsonSerializer<Duration>() {
            @Override
            public void serialize(Duration value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                gen.writeNumber(value.getSeconds());
            }
        };
    }

    public static JsonSerializer<ZonedDateTime> zonedDateTimeSerializer() {
        return new JsonSerializer<ZonedDateTime>() {
            @Override
            public void serialize(ZonedDateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                gen.writeString(value.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
            }
        };
    }
}
