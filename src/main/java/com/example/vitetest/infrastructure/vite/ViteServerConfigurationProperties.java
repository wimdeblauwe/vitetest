package com.example.vitetest.infrastructure.vite;

import static com.example.vitetest.infrastructure.vite.ViteServerConfigurationProperties.PREFIX;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = PREFIX)
public record ViteServerConfigurationProperties(String host, int port) {

  public static final String PREFIX = "thvite-server-config";

  public String baseUrl() {
    return "//" + host + ":" + port;
  }
}
