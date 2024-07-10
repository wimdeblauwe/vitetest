package com.example.vitetest.infrastructure.vite;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;

@Configuration
@EnableConfigurationProperties({ViteConfigurationProperties.class,
    ViteDevServerConfigurationProperties.class})
public class ViteConfiguration {

  @Bean
  public ViteDialect viteDialect(
      ViteConfigurationProperties properties,
      ViteDevServerConfigurationProperties serverProperties,
      ViteManifestReader manifestReader) {
    return new ViteDialect(properties, serverProperties, manifestReader);
  }

  @Bean
  public ViteManifestReader viteManifestReader(ResourceLoader resourceLoader, ObjectMapper objectMapper) {
    return new ViteManifestReader(resourceLoader, objectMapper);
  }
}
