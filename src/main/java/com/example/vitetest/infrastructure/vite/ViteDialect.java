package com.example.vitetest.infrastructure.vite;

import java.util.Set;
import org.thymeleaf.dialect.AbstractProcessorDialect;
import org.thymeleaf.processor.IProcessor;

public class ViteDialect extends AbstractProcessorDialect {

  private final ViteServerConfigurationProperties serverProperties;

  protected ViteDialect(ViteServerConfigurationProperties serverProperties) {
    super("Vite Dialect", "vite", 1000);
    this.serverProperties = serverProperties;
  }

  @Override
  public Set<IProcessor> getProcessors(String dialectPrefix) {
    return Set.of(
        new ViteTagProcessor(dialectPrefix, serverProperties)
    );
  }
}
