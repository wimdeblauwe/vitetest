package com.example.vitetest.infrastructure.vite;

import java.util.Set;
import org.thymeleaf.dialect.AbstractProcessorDialect;
import org.thymeleaf.processor.IProcessor;

public class ViteDialect extends AbstractProcessorDialect {

  private final ViteConfigurationProperties properties;
  private final ViteDevServerConfigurationProperties serverProperties;
  private final ViteManifestReader manifestReader;

  public ViteDialect(
      ViteConfigurationProperties properties,
      ViteDevServerConfigurationProperties serverProperties,
      ViteManifestReader manifestReader) {
    super("Vite Dialect", "vite", 1000);
    this.properties = properties;
    this.serverProperties = serverProperties;
    this.manifestReader = manifestReader;
  }

  @Override
  public Set<IProcessor> getProcessors(String dialectPrefix) {
    return Set.of(
        new ViteTagProcessor(dialectPrefix, properties, serverProperties, manifestReader)
    );
  }
}
