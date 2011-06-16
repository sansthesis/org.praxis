package org.praxis.jersey.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.ReferenceCardinality;
import org.apache.felix.scr.annotations.ReferencePolicy;
import org.osgi.service.http.HttpService;
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
@Reference(target = "(&(!(jaxrs.ignore.resource=true))(jaxrs.application.path=*))", cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE, policy = ReferencePolicy.DYNAMIC, bind = "bindResource", unbind = "unbindResource", referenceInterface = JaxRSResource.class)
public class JaxRSApplicationManagerImpl implements JaxRSApplicationManager {

  private final Map<String, Set<JaxRSResource>> resourceMap = new HashMap<String, Set<JaxRSResource>>();

  @Reference
  private HttpService httpService;

  private final Logger log = LoggerFactory.getLogger(getClass());

  public JaxRSApplicationManagerImpl() {
    super();
  }

  protected JaxRSApplicationManagerImpl(final HttpService httpService) {
    this();
    this.httpService = httpService;
  }

  @Override
  public void bindResource(final JaxRSResource resource, final Map<String, ?> map) {
    log.info("Binding service: {}, {}", resource, map);
    // Get application path.
    final String path = map.get(JaxRSResourceConstants.PROPERTY_APPLICATION_PATH).toString();
    log.debug("Path: {}", path);

    // Add resource to that Application's context.
    final Set<JaxRSResource> resources = getResources(path);
    resources.add(resource);

    // Refresh that Application.
    refreshJerseyApplication(path, resources);
  }

  @Override
  public void unbindResource(final JaxRSResource resource, final Map<String, ?> map) {
    log.info("Unbinding service: {}, {}", resource, map);
    // Get application path.
    final String path = map.get(JaxRSResourceConstants.PROPERTY_APPLICATION_PATH).toString();
    log.debug("Path: {}", path);

    // Remove resource from that Application's context.
    final Set<JaxRSResource> resources = getResources(path);
    resources.remove(resource);

    // Refresh that Application.
    if( !resources.isEmpty() ) {
      refreshJerseyApplication(path, resources);
    } else {
      unbindContext(path);
      resourceMap.remove(path);
    }
  }

  /**
   * Deactivates the component.
   */
  @Deactivate
  protected void deactivate() {
    log.debug("Deactivating.");
    for( final String path : resourceMap.keySet() ) {
      unbindContext(path);
    }
    resourceMap.clear();
  }

  /**
   * Returns the JaxRSResources to build a Jersey Application out of for a given path.
   * @param path The context path to build an application for.
   * @return The JaxRSResources to build a Jersey Application out of for a given path.
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
