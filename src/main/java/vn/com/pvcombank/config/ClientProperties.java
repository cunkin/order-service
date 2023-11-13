package vn.com.pvcombank.config;

import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.net.URI;

@ConfigurationProperties(prefix = "pvcombank")
public record ClientProperties (

        @NotNull
        URI catalogServiceUri
) {}
