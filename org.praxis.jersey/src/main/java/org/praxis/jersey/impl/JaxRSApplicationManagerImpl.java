package org.praxis.jersey.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Reference;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.http.HttpService;
import org.osgi.util.tracker.ServiceTracker;
import org.praxis.jersey.JaxRSResource;
import org.praxis.jersey.JaxRSResourceConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jersey.spi.container.servlet.ServletContainer;

/**
 * The <code>OsgiApplicationImpl</code> is an SCR Component that registers all of this application's endpoints into the Jersey context. This class is necessary because Jersey is not currently OSGi-aware enough to be able to register Resources via a whiteboard pattern.
 * @author Jason Rose
 */

@Component(immediate = true, metatype = true)
public class JaxRSApplicationManagerImpl {

  private final Map<String, Set<JaxRSResource>> resourceMap = new HashMap<String, Set<JaxRSResource>>();

  @Reference
  private HttpService httpService;

  private final Logger log = LoggerFactory.getLogger(getClass());

  private ServiceTracker tracker;

  public JaxRSApplicationManagerImpl() {
    super();
  }

  protected JaxRSApplicationManagerImpl(final HttpService httpService) {
    this();
    this.httpService = httpService;
  }

  /**
   * Activates the component.
   */
  @Activate
  protected void activate(final ComponentContext ctx) throws Exception {
    log.debug("Activating.");
    final BundleContext bundleContext = ctx.getBundleContext();
    tracker = new ServiceTracker(bundleContext, bundleContext.createFilter(String.format("(&(objectClass=%s)(!(%s=true))(%s=*))", JaxRSResource.class.getName(), JaxRSResourceConstants.PROPERTY_IGNORE_RESOURCE, JaxRSResourceConstants.PROPERTY_APPLICATION_PATH)), new JaxRSResourceServiceTracker(bundleContext, this));
    tracker.open();
  }

  /**
   * This method binds a new JaxRSResource to the application specified in its path service property.
   * @param resource The new JaxRSResource to bind.
   * @param reference The ServiceReference to pull registration properties from.
   */
  protected void bindResource(final JaxRSResource resource, final ServiceReference reference) {
    log.debug("Binding Resource: {}", resource);

    // Get application path.
    final String path = reference.getProperty(JaxRSResourceConstants.PROPERTY_APPLICATION_PATH).toString();
    log.debug("Path: {}", path);

    // Add resource to that Application's context.
    final Set<JaxRSResource> resources = getResources(path);
    resources.add(resource);

    // Refresh that Application.
    refreshJerseyApplication(path, resources);
  }

  /**
   * Deactivates the component.
   */
  @Deactivate
  protected void deactivate() {
    log.debug("Deactivating.");
    tracker.close();
    for( final String path : resourceMap.keySet() ) {
      unbindContext(path);
    }
    resourceMap.clear();
  }

  /**
   * This method removes a JaxRSResource from the application specified in its path service property.
   * @param resource The new JaxRSResource to remove.
   * @param reference The ServiceReference to pull registration properties from.
   */
  protected void unbindResource(final JaxRSResource resource, final ServiceReference reference) {
    log.debug("Unbinding Resource: {}", resource);

    // Get application path.
    final String path = reference.getProperty(JaxRSResourceConstants.PROPERTY_APPLICATION_PATH).toString();
    log.debug("Path: {}", path);

    // Remove resource from that Application's context.
    final Set<JaxRSResource> resources = getResources(path);
    resources.remove(resource);

    // Refresh that Application.
    if( !resources.isEmpty() ) {
      refreshJerseyApplication(path, resources);
    } else {
      resourceMap.remove(path);
    }
  }

  /**
   * Returns the JaxRSResources to build a Jersey Application out of for a given path.
   * @param path The context path to build an application for.
   * @return The JaxRSResources to build a Jersey Application ut of for a given path.
   */
  private Set<JaxRSResource> getResources(final String path) {
    Set<JaxRSResource> set = resourceMap.get(path);
    if( set == null ) {
      set = new HashSet<JaxRSResource>();
      resourceMap.put(path, set);
    }
    return set;
  }

  /**
   * This method removes any existing Jersey Application from the specified path and remaps a new Application with the given Set of JaxRSResources to it.
   * @param path The context path of the Application to use.
   * @param resources The Set of JaxRSResources that should be served under the specified path.
   */
  private void refreshJerseyApplication(final String path, final Set<JaxRSResource> resources) {
    try {
      unbindContext(path);
      final JaxRSApplicationImpl application = new JaxRSApplicationImpl(path, httpService.createDefaultHttpContext(), resources, null);
      final ServletContainer container = new ServletContainer(application);
      httpService.registerServlet(path, container, application.getInitParams(), application.getHttpContext());
    } catch (final Exception e) {
      log.warn("Unable to refresh Jersey application at path " + path + ".", e);
    }
  }

  /**
   * Removes any Jersey Applications bound to the given context path.
   * @param path The path of the Jersey Application to unbind.
   */
  private void unbindContext(final String path) {
    try {
      httpService.unregister(path);
    } catch (final IllegalArgumentException iae) {
      log.debug("Unable to unbind path " + path + ".", iae);
    } catch (final Exception e) {
      log.warn("Unable to unbind path " + path + ".", e);
    }
  }
}
