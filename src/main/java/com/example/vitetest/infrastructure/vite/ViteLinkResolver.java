package com.example.vitetest.infrastructure.vite;

import com.example.vitetest.infrastructure.vite.ViteConfigurationProperties.Mode;
import com.example.vitetest.infrastructure.vite.ViteManifestReader.ManifestEntry;
import java.util.Map;

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
      String bundledPath = manifestReader.getBundledPath(prependWithStatic(resource));
//      if( bundledPath == null ) {
//        // imported resources don't have the /static prefix
//        bundledPath = manifestReader.getBundledPath(resource);
//      }
      return bundledPath;
    }
  }

  public ManifestEntry getManifestEntry(String resource) {
    ManifestEntry manifestEntry = manifestReader.getManifestEntry(prependWithStatic(resource));
    if( manifestEntry == null ) {
      // imported resources don't have the /static prefix
      manifestEntry = manifestReader.getManifestEntry(resource);
    }
    return manifestEntry;
  }

  private String prependWithStatic(String resource) {
    if (resource.startsWith("/")) {
      return "static" + resource;
    } else {
      return "static/" + resource;
    }
  }
}
