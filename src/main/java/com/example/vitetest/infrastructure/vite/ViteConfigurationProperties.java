package com.example.vitetest.infrastructure.vite;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

@ConfigurationProperties("thvite")
public record ViteConfigurationProperties(
    @DefaultValue("build") Mode mode
) {

  public enum Mode {
    DEV,
    BUILD
  }
}
