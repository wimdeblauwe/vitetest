package com.example.vitetest.infrastructure.vite;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({ViteConfigurationProperties.class,
    ViteServerConfigurationProperties.class})
public class ViteConfiguration {

  @Bean
  public ViteDialect viteDialect(ViteServerConfigurationProperties serverProperties) {
    return new ViteDialect(serverProperties);
  }
}
