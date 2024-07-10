package com.example.vitetest.infrastructure.vite;

import static com.example.vitetest.infrastructure.vite.ViteServerConfigurationProperties.PREFIX;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;

public class ViteServerConfigurationPropertiesContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

  private static final String PROPERTY_FILE_PREFX = PREFIX + ".";

  @Override
  public void initialize(ConfigurableApplicationContext applicationContext) {
    try {
      Path path = Path.of("target/thvite/server-config.json");
      ObjectMapper objectMapper = new ObjectMapper();
      Map map = objectMapper.readValue(path.toFile(), Map.class);

      MapPropertySource hostPropertySource = new MapPropertySource(PROPERTY_FILE_PREFX + "host",
          Map.of(PROPERTY_FILE_PREFX + "host", map.get("host")));
      MapPropertySource portPropertySource = new MapPropertySource(PROPERTY_FILE_PREFX + "port",
          Map.of(PROPERTY_FILE_PREFX + "port", map.get("port")));
      MutablePropertySources propertySources = applicationContext.getEnvironment().getPropertySources();
      propertySources.addFirst(hostPropertySource);
      propertySources.addFirst(portPropertySource);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
