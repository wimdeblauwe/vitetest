package com.example.vitetest.infrastructure.vite;

import java.util.Map;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.model.AttributeValueQuotes;
import org.thymeleaf.model.IAttribute;
import org.thymeleaf.model.IModel;
import org.thymeleaf.model.IModelFactory;
import org.thymeleaf.model.IOpenElementTag;
import org.thymeleaf.model.ITemplateEvent;
import org.thymeleaf.processor.element.AbstractElementModelProcessor;
import org.thymeleaf.processor.element.IElementModelStructureHandler;
import org.thymeleaf.templatemode.TemplateMode;

public class ViteTagProcessor extends AbstractElementModelProcessor {

  private final ViteServerConfigurationProperties serverProperties;

  public ViteTagProcessor(String dialectPrefix, ViteServerConfigurationProperties serverProperties) {
    super(TemplateMode.HTML, dialectPrefix, "vite", true, null, false, 10_000);
    this.serverProperties = serverProperties;
  }

  @Override
  protected void doProcess(ITemplateContext context, IModel model, IElementModelStructureHandler structureHandler) {
    // <script type="module" src="http://127.0.0.1:5173/@vite/client"></script>
    // <link rel="stylesheet" href="http://127.0.0.1:5173/resources/css/app.css?t=1720614003572">
    IModelFactory modelFactory = context.getModelFactory();
    ITemplateEvent event = model.get(0);
    String entrypointValue = null;
    if (event instanceof IOpenElementTag openElementTag) {
      IAttribute entrypoint = openElementTag.getAttribute("entrypoint");
      if (entrypoint != null) {
        entrypointValue = entrypoint.getValue();
      }
    }
    if (entrypointValue != null) {
      // TODO should only be added for the first link tag on the page
      //  or alternatively expose an `<thvite:client></thvite:client>` and `<link rel="stylesheet" thvite:href="...">` option,
      //   similar to what is done at https://dev.to/tylerlwsmith/build-a-vite-5-backend-integration-with-flask-jch
      // TODO only add this script when running in development, for production this should not be there + URLs need to be taken from the generated manifest.json file
      model.replace(0, modelFactory.createOpenElementTag("script", Map.of("type", "module", "src", serverProperties.baseUrl() + "/@vite/client"),
          AttributeValueQuotes.DOUBLE, false));
      model.replace(model.size() - 1, modelFactory.createCloseElementTag("script"));
      model.add(modelFactory.createStandaloneElementTag("link",
          Map.of("rel", "stylesheet", "href", resolveStaticResource(entrypointValue)),
          AttributeValueQuotes.DOUBLE, false, false));
    } else {
      System.out.println("WARN: No entrypoint found!");
    }
  }

  private String resolveStaticResource(String entrypointValue) {
    return serverProperties.baseUrl() + "/static/" + entrypointValue;
  }
}
