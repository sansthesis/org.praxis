package org.praxis.blog.jersey.hateoas.impl;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.http.HttpService;
import org.praxis.blog.jersey.hateoas.BlogController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jersey.spi.container.servlet.ServletContainer;

@Component(immediate = true, metatype = true)
public class OsgiApplicationImpl extends Application {
  private final Logger log = LoggerFactory.getLogger(getClass());

  @Property(label = "Application Alias", cardinality = 0, value = "/hateoas")
  public static final String PROPERTY_APPLICATION_ALIAS = "application.alias";
  private String alias;

  @Reference
  private HttpService httpService;

  @Reference
  private BlogController blogController;

  //  @Reference
  //  private CommentController commentController;
  //
  //  @Reference
  //  private StoryController storyController;

  private JAXBContextResolver jaxbContextResolver;

  public String getAlias() {
    return alias;
  }

  @Override
  public Set<Object> getSingletons() {
    final Set<Object> singletons = new HashSet<Object>();
    singletons.add(blogController);
    //    singletons.add(commentController);
    //    singletons.add(storyController);
    singletons.add(jaxbContextResolver);
    return singletons;
  }

  @Activate
  protected void activate(final ComponentContext ctx) throws Exception {
    alias = (String) ctx.getProperties().get(PROPERTY_APPLICATION_ALIAS);
    log.debug("Starting Jersey API Application at '{}' with resources: {}.", getAlias(), getSingletons());
    final ServletContainer container = new ServletContainer(this);
    jaxbContextResolver = new JAXBContextResolver();
    httpService.registerServlet(getAlias(), container, null, null);
  }

  @Deactivate
  protected void deactivate(final ComponentContext ctx) {
    log.debug("Unbinding for context: {}.", getAlias());
    alias = null;
    httpService.unregister(getAlias());
  }
}
