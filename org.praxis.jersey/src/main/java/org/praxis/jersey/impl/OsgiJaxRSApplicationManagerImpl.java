package org.praxis.jersey.impl;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Reference;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.http.HttpService;
import org.osgi.util.tracker.ServiceTracker;
import org.praxis.jersey.JaxRSApplication;
import org.praxis.jersey.JaxRSApplicationManager;
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
public class OsgiJaxRSApplicationManagerImpl {

  @Reference
  private HttpService httpService;

  private final Logger log = LoggerFactory.getLogger(getClass());

  private ServiceTracker tracker;
  
  private JaxRSApplicationManager manager;

  public OsgiJaxRSApplicationManagerImpl() {
    super();
    manager = new JaxRSApplicationManagerImpl();
  }

  protected OsgiJaxRSApplicationManagerImpl(final HttpService httpService) {
    this();
    this.httpService = httpService;
  }

  /**
   * Activates the component.
   */
  @Activate
  protected void activate(final ComponentContext ctx) throws Exception {
    log.info("Activating.");
    final BundleContext bundleContext = ctx.getBundleContext();
    tracker = new ServiceTracker(bundleContext, bundleContext.createFilter(String.format("(&(objectClass=%s)(!(%s=true))(%s=*))", JaxRSResource.class.getName(), JaxRSResourceConstants.PROPERTY_IGNORE_RESOURCE, JaxRSResourceConstants.PROPERTY_APPLICATION_PATH)), new JaxRSResourceServiceTracker(bundleContext, this));
    tracker.open();
    // final ServletContainer container = new ServletContainer(this);
//     httpService.registerServlet(alias, container, null, null);
  }

  protected void bindResource(JaxRSResource resource, ServiceReference reference) {
    log.info("Binding Resource: {}", resource);

    // Get application path.
    final String path = reference.getProperty(JaxRSResourceConstants.PROPERTY_APPLICATION_PATH).toString();
    log.info("Path: {}", path);

    // Add resource to that Application's context.
    final JaxRSApplication application = manager.getApplication(path);
    application.getResources().add(resource);

    // Refresh that Application.
    final ServletContainer container = new ServletContainer(application);
    httpService.registerServlet(path, container, application.getInitParams(), application.getHttpContext());

  }

  /**
   * Deactivates the component.
   */
  @Deactivate
  protected void deactivate() {
    log.info("Deactivating.");
    tracker.close();
    // log.debug("Unbinding from context: {}.", alias);
    // httpService.unregister(alias);
    // alias = null;
  }

  protected void unbindResource(JaxRSResource resource, ServiceReference reference) {
    log.info("Unbinding Resource: {}", resource);

    // Get application path.
    final String path = reference.getProperty(JaxRSResourceConstants.PROPERTY_APPLICATION_PATH).toString();
    log.info("Path: {}", path);

    // Remove resource from that Application's context.
    final JaxRSApplication application = manager.getApplication(path);
    application.getResources().add(resource);

    // Destroy context at that path.
    httpService.unregister(path);
    
    // Refresh that Application.
    final ServletContainer container = new ServletContainer(application);
    httpService.registerServlet(path, container, application.getInitParams(), application.getHttpContext());
  }
}
