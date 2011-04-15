package org.praxis.blog.jersey.jackson.impl;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.codehaus.jackson.map.ObjectMapper;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.http.HttpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jersey.spi.container.servlet.ServletContainer;

/**
 * The <code>OsgiApplicationImpl</code> is an SCR Component that registers all of this application's endpoints into the Jersey context. This class is necessary because Jersey is not currently OSGi-aware enough to be able to register Resources via a whiteboard pattern.
 * @author Jason Rose
 * 
 */
@Component(immediate = true, metatype = true)
public class OsgiApplicationImpl extends Application {

  /**
   * This is the servlet path that Jersey will register its context under.
   */
  @Property(label = "Application Alias", value = "/jackson")
  public static final String PROPERTY_APPLICATION_ALIAS = "application.alias";

  private String alias;

  @Reference
  private HttpService httpService;

  private final Logger log = LoggerFactory.getLogger(getClass());

  @Reference
  private TestResource testResource;

  @Override
  public Set<Object> getSingletons() {
    final ObjectMapper mapper = new ObjectMapper();
    final JacksonJsonProvider provider = new JacksonJsonProvider(mapper);
    final Set<Object> singletons = new HashSet<Object>();
    singletons.add(testResource);
    singletons.add(provider);
    return singletons;
  }

  /**
   * Activates the component.
   * @param ctx The ComponentContext at the time of activation.
   */
  @Activate
  protected void activate(final ComponentContext ctx) throws Exception {
    alias = (String) ctx.getProperties().get(PROPERTY_APPLICATION_ALIAS);
    log.debug("Starting Jersey API Application at '{}' with resources: {}.", alias, getSingletons());
    final ServletContainer container = new ServletContainer(this);
    httpService.registerServlet(alias, container, null, null);
  }

  /**
   * Deactivates the component.
   * @param ctx The ComponentContext at the time of deactivation.
   */
  @Deactivate
  protected void deactivate(final ComponentContext ctx) {
    log.debug("Unbinding from context: {}.", alias);
    httpService.unregister(alias);
    alias = null;
  }
}
