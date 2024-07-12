package com.example.vitetest.infrastructure.vite;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

public class ViteManifestReader {

  private static final Logger LOGGER = LoggerFactory.getLogger(ViteManifestReader.class);

  private final ResourceLoader resourceLoader;
  private final ObjectMapper objectMapper;
  private Map<String, ManifestEntry> manifest;

  public ViteManifestReader(ResourceLoader resourceLoader, ObjectMapper objectMapper) {
    this.resourceLoader = resourceLoader;
    this.objectMapper = objectMapper;
  }

  @PostConstruct
  public void init() throws IOException {
    Resource resource = resourceLoader.getResource("classpath:/static/.vite/manifest.json");
    if (resource.exists()) {
      try (InputStream inputStream = resource.getInputStream()) {
        MapType type = objectMapper.getTypeFactory().constructMapType(Map.class, String.class, ManifestEntry.class);
        manifest = objectMapper.readValue(inputStream, type);
      }
    } else {
      LOGGER.warn("Could not find vite-manifest.json. Run `npm run build` to generate it.");
    }
  }

  public String getBundledPath(String originalPath) {
    ManifestEntry entry = manifest.get(originalPath);
    return entry != null ? entry.file() : null;
  }

  public ManifestEntry getManifestEntry(String resource) {
    return manifest.get(resource);
  }

  public record ManifestEntry(String file, String src, boolean isEntry, List<String> css, List<String> imports) {

  }
}
