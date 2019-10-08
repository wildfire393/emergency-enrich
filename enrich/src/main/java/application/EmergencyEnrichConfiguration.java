package application;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Configures the 911 Enrichment REST Web Service
 * and data
 */
@ComponentScan(basePackages = "rest")
@Configuration
@EnableAutoConfiguration
@PropertySource("classpath:enrich.properties")
public class EmergencyEnrichConfiguration {

    /**
     * Creates a {@link ObjectMapper} used to convert objects to JSON and back.
     * @return Never (@code null}
     */
    @Bean
    public ObjectMapper objectMapper(){
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        // Enable annotation free (i.e. Jackson's {@code @JsonPropery}) constructor arguments in POJOs.
        // NOTE: Requires "-parameters" compiler option available in Java 8+.
        mapper.registerModule(new ParameterNamesModule());
        // Write dates as text strings, standard format is ISO-8601
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.registerModule(new Jdk8Module());
        return mapper;
    }
}
