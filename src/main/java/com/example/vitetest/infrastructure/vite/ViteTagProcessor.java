package com.example.vitetest.infrastructure.vite;

import com.example.vitetest.infrastructure.vite.TagFactory.ScriptTags;
import com.example.vitetest.infrastructure.vite.ViteManifestReader.ManifestEntry;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.model.AbstractModelVisitor;
import org.thymeleaf.model.AttributeValueQuotes;
import org.thymeleaf.model.IAttribute;
import org.thymeleaf.model.ICloseElementTag;
import org.thymeleaf.model.IModel;
import org.thymeleaf.model.IModelFactory;
import org.thymeleaf.model.IOpenElementTag;
import org.thymeleaf.model.IStandaloneElementTag;
import org.thymeleaf.model.ITemplateEvent;
import org.thymeleaf.processor.element.AbstractElementModelProcessor;
import org.thymeleaf.processor.element.IElementModelStructureHandler;
import org.thymeleaf.templatemode.TemplateMode;

// https://github.com/laravel/framework/blob/11.x/src/Illuminate/Foundation/Vite.php
public class ViteTagProcessor extends AbstractElementModelProcessor {

  private static final Logger LOGGER = LoggerFactory.getLogger(ViteTagProcessor.class);
  private static final String TAG_NAME = "vite";
  private static final String ENTRY_TAG_NAME = "entry";
  private static final int PRECEDENCE = 1000;

  private final ViteLinkResolver linkResolver;

  public ViteTagProcessor(String dialectPrefix,
      ViteLinkResolver linkResolver) {
    super(TemplateMode.HTML, dialectPrefix, TAG_NAME, true, null, false, PRECEDENCE);
    this.linkResolver = linkResolver;
  }

  @Override
  protected void doProcess(ITemplateContext context, IModel model, IElementModelStructureHandler structureHandler) {
    IModelFactory modelFactory = context.getModelFactory();
    ViteModelVisitor visitor = new ViteModelVisitor(modelFactory);
    model.accept(visitor);
    model.reset();
    visitor.getHtmlEntries().forEach(model::add);
  }

  /*private ScriptTags generateScriptTags(String srcValue, IModelFactory modelFactory) {
    LOGGER.debug("Generating script tag for {}", srcValue);
    // Use LinkedHashMap for predicable order in the HTML
    LinkedHashMap<String, String> attributes = new LinkedHashMap<>();
    attributes.put("type", "module");
    attributes.put("src", srcValue);

    IOpenElementTag scriptOpenTag = modelFactory.createOpenElementTag("script",
        attributes,
        AttributeValueQuotes.DOUBLE, false);
    ICloseElementTag scriptCloseTag = modelFactory.createCloseElementTag("script");
    return new ScriptTags(scriptOpenTag, scriptCloseTag);
  }

  private IStandaloneElementTag generateCssLinkTag(String hrefValue, IModelFactory modelFactory) {
    LOGGER.debug("Generating CSS tag for {}", hrefValue);
    // Use LinkedHashMap for predicable order in the HTML
    LinkedHashMap<String, String> attributes = new LinkedHashMap<>();
    attributes.put("rel", "stylesheet");
    attributes.put("href", hrefValue);

    return modelFactory.createStandaloneElementTag("link",
        attributes,
        AttributeValueQuotes.DOUBLE, false, false);
  }*/

  private boolean isCssPath(String value) {
    // TODO use regex ->        return preg_match('/\.(css|less|sass|scss|styl|stylus|pcss|postcss)$/', $path) === 1;
    return value.endsWith(".css") || value.endsWith(".scss");
  }

 /* private record ScriptTags(IOpenElementTag openTag, ICloseElementTag closeTag) {

    public void addTagsTo(List<ITemplateEvent> htmlEntries) {
      htmlEntries.add(openTag);
      htmlEntries.add(closeTag);
    }
  }*/

  private class ViteModelVisitor extends AbstractModelVisitor {

    private final TagFactory tagFactory;
    private final List<ITemplateEvent> htmlEntries = new ArrayList<>();

    public ViteModelVisitor(IModelFactory modelFactory) {
      this.tagFactory = new TagFactory(modelFactory);
    }

    public List<ITemplateEvent> getHtmlEntries() {
      return htmlEntries;
    }

    @Override
    public void visit(IOpenElementTag openElementTag) {
      String elementName = openElementTag.getElementDefinition().getElementName().getElementName();
      if (elementName.equals(TAG_NAME)) {
        // no-op
      } else if (elementName.equals(ENTRY_TAG_NAME)) {
        IAttribute valueAttribute = openElementTag.getAttribute("value");
        if (valueAttribute != null) {
          String value = valueAttribute.getValue();
          handleValue(value);
        }
      }
    }

    private void handleValue(String value) {
      LOGGER.debug("resolving {}", value);
      if (isCssPath(value)) {
        htmlEntries.add(tagFactory.generateCssLinkTag(linkResolver.resolveResource(value)));
      } else {
        ScriptTags scriptTags = tagFactory.generateScriptTags(linkResolver.resolveResource(value));
        scriptTags.addTagsTo(htmlEntries);
      }
      ManifestEntry manifestEntry = linkResolver.getManifestEntry(value);
      if (manifestEntry.css() != null) {
        for (String linkedCss : manifestEntry.css()) {
          htmlEntries.add(tagFactory.generateCssLinkTag(linkResolver.resolveResource(linkedCss)));
        }
      }

      if (manifestEntry.imports() != null) {
        for (String importedResource : manifestEntry.imports()) {
          handleImportedResource(importedResource);
        }
      }
    }

    private void handleImportedResource(String importedResource) {
      LOGGER.debug("Handling imported resource: {}", importedResource);
      ManifestEntry manifestEntry = linkResolver.getManifestEntry(importedResource);
      String file = manifestEntry.file();
      if (isCssPath(file)) {
        htmlEntries.add(tagFactory.generateCssLinkTag(file));
      } else {
        ScriptTags scriptTags = tagFactory.generateScriptTags(file);
        scriptTags.addTagsTo(htmlEntries);
      }

      if (manifestEntry.css() != null) {
        for (String linkedCss : manifestEntry.css()) {
          htmlEntries.add(tagFactory.generateCssLinkTag(linkedCss));
        }
      }

      if (manifestEntry.imports() != null) {
        for (String nestedImportedResource : manifestEntry.imports()) {
          handleImportedResource(nestedImportedResource);
        }
      }
    }
  }
}
