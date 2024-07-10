package com.example.vitetest.infrastructure.vite;

import static com.example.vitetest.infrastructure.vite.ViteDevServerConfigurationProperties.PREFIX;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = PREFIX)
public record ViteDevServerConfigurationProperties(String host, int port) {

  public static final String PREFIX = "thvite-dev-server-config";

  public String baseUrl() {
    return "//" + host + ":" + port;
  }
}
