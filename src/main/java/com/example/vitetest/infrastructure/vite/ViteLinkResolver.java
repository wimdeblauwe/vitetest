package com.example.vitetest.infrastructure.vite;

import com.example.vitetest.infrastructure.vite.ViteConfigurationProperties.Mode;

public class ViteLinkResolver {

  private final ViteConfigurationProperties properties;
  private final ViteDevServerConfigurationProperties devServerProperties;
  private final ViteManifestReader manifestReader;


  public ViteLinkResolver(ViteConfigurationProperties properties, ViteDevServerConfigurationProperties devServerProperties,
      ViteManifestReader manifestReader) {
    this.properties = properties;
    this.devServerProperties = devServerProperties;
    this.manifestReader = manifestReader;
  }

  public String resolveResource(String resource) {
    if (properties.mode() == Mode.DEV) {
      return devServerProperties.baseUrl() + "/"
             + prependWithStatic(resource);
    } else {
      return manifestReader.getBundledPath(prependWithStatic(resource));
    }
  }

  private String prependWithStatic(String resource) {
    if (resource.startsWith("/")) {
      return "static" + resource;
    } else {
      return "static/" + resource;
    }
  }
}
