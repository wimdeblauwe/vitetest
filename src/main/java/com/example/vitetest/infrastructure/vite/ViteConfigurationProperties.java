package com.example.vitetest.infrastructure.vite;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("vite")
public record ViteConfigurationProperties(

) {

}
