package com.example.vitetest.infrastructure.vite;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class ViteEnvironment {

  @Bean
  ViteEnvironmentInfo environmentInfo() {
    return new ViteEnvironmentInfo(false);
  }

  public record ViteEnvironmentInfo(boolean isRunningHot) {

  }
}
