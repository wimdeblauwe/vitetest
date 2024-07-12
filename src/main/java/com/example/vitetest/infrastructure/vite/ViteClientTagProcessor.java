package com.example.vitetest.infrastructure.vite;

import com.example.vitetest.infrastructure.vite.TagFactory.ScriptTags;
import com.example.vitetest.infrastructure.vite.ViteConfigurationProperties.Mode;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.model.IModel;
import org.thymeleaf.model.IModelFactory;
import org.thymeleaf.model.IOpenElementTag;
import org.thymeleaf.model.ITemplateEvent;
import org.thymeleaf.processor.element.AbstractElementModelProcessor;
import org.thymeleaf.processor.element.IElementModelStructureHandler;
import org.thymeleaf.templatemode.TemplateMode;

public class ViteClientTagProcessor extends AbstractElementModelProcessor {

  private final ViteConfigurationProperties properties;
  private final ViteDevServerConfigurationProperties devServerProperties;

  public ViteClientTagProcessor(String dialectPrefix,
      ViteConfigurationProperties properties,
      ViteDevServerConfigurationProperties devServerProperties) {
    super(TemplateMode.HTML, dialectPrefix, "client", true, null, false, 10_000);
    this.properties = properties;
    this.devServerProperties = devServerProperties;
  }

  @Override
  protected void doProcess(ITemplateContext context, IModel model, IElementModelStructureHandler structureHandler) {
    IModelFactory modelFactory = context.getModelFactory();
    ITemplateEvent event = model.get(0);
    if (event instanceof IOpenElementTag) {
      if (properties.mode() == Mode.DEV) {
        TagFactory tagFactory = new TagFactory(modelFactory);
        ScriptTags scriptTags = tagFactory.generateScriptTags(devServerProperties.baseUrl() + "/@vite/client");
        model.replace(0,
            scriptTags.openTag());
        model.replace(model.size() - 1, scriptTags.closeTag());
      } else {
        model.reset();
      }
    }
  }

}
