package org.praxis.blog.jersey.hateoas.impl;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Reference;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.http.HttpService;
import org.praxis.blog.jersey.hateoas.ApplicationConfiguration;
import org.praxis.blog.jersey.hateoas.BlogController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jersey.spi.container.servlet.ServletContainer;

@Component(immediate = true, metatype = true)
public class OsgiApplicationImpl extends Application {
  private final Logger log = LoggerFactory.getLogger(getClass());

  @Reference
  private HttpService httpService;

  @Reference
  private ApplicationConfiguration configuration;

  @Reference
  private BlogController blogController;

  //  @Reference
  //  private CommentController commentController;
  //
  //  @Reference
  //  private StoryController storyController;

  private String alias;

  private JAXBContextResolver jaxbContextResolver;

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
    alias = configuration.getContextPath();
    log.debug("Starting Jersey API Application at '{}' with resources: {}.", alias, getSingletons());
    final ServletContainer container = new ServletContainer(this);
    jaxbContextResolver = new JAXBContextResolver();
    httpService.registerServlet(alias, container, null, null);
  }

  @Deactivate
  protected void deactivate(final ComponentContext ctx) {
    log.debug("Unbinding for context: {}.", alias);
    httpService.unregister(alias);
    alias = null;
    jaxbContextResolver = null;
  }
}