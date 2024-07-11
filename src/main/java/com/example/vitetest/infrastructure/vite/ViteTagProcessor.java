package com.example.vitetest.infrastructure.vite;

import com.example.vitetest.infrastructure.vite.ViteConfigurationProperties.Mode;
import java.util.Map;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.model.AttributeValueQuotes;
import org.thymeleaf.model.IModel;
import org.thymeleaf.model.IModelFactory;
import org.thymeleaf.model.IOpenElementTag;
import org.thymeleaf.model.ITemplateEvent;
import org.thymeleaf.processor.element.AbstractElementModelProcessor;
import org.thymeleaf.processor.element.IElementModelStructureHandler;
import org.thymeleaf.templatemode.TemplateMode;

public class ViteTagProcessor extends AbstractElementModelProcessor {

  private final ViteConfigurationProperties properties;
  private final ViteDevServerConfigurationProperties devServerProperties;
  private final ViteLinkResolver linkResolver;

  public ViteTagProcessor(String dialectPrefix,
      ViteConfigurationProperties properties,
      ViteDevServerConfigurationProperties devServerProperties,
      ViteLinkResolver linkResolver) {
    super(TemplateMode.HTML, dialectPrefix, "client", true, null, false, 10_000);
    this.properties = properties;
    this.devServerProperties = devServerProperties;
    this.linkResolver = linkResolver;
  }

  @Override
  protected void doProcess(ITemplateContext context, IModel model, IElementModelStructureHandler structureHandler) {
    IModelFactory modelFactory = context.getModelFactory();
    ITemplateEvent event = model.get(0);
    if (event instanceof IOpenElementTag) {
      if (properties.mode() == Mode.DEV) {
        model.replace(0,
            modelFactory.createOpenElementTag("script", Map.of("type", "module", "src", devServerProperties.baseUrl() + "/@vite/client"),
                AttributeValueQuotes.DOUBLE, false));
        model.replace(model.size() - 1, modelFactory.createCloseElementTag("script"));
      } else {
        model.reset();
      }
    }
  }

}
