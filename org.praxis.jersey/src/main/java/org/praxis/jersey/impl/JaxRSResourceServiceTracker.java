package org.praxis.jersey.impl;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTrackerCustomizer;
import org.praxis.jersey.JaxRSResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This ServiceTrackerCustomizer knows how to bind JaxRSApplications to the JaxRSResourceManager.
 * @author Jason Rose
 */
public class JaxRSResourceServiceTracker implements ServiceTrackerCustomizer {

  private final BundleContext bundleContext;
  private final JaxRSApplicationManagerImpl manager;
  private final Logger log = LoggerFactory.getLogger(getClass());

  public JaxRSResourceServiceTracker(final BundleContext bundleContext, final JaxRSApplicationManagerImpl manager) {
    this.bundleContext = bundleContext;
    this.manager = manager;
  }

  @Override
  public Object addingService(final ServiceReference reference) {
    final Object service = bundleContext.getService(reference);
    log.debug("Adding service: {}", service);
    manager.bindResource((JaxRSResource) service, reference);
    return service;
  }

  @Override
  public void modifiedService(final ServiceReference reference, final Object service) {
    log.debug("Modified service: {}", service);
    manager.unbindResource((JaxRSResource) service, reference);
    manager.bindResource((JaxRSResource) service, reference);
  }

  @Override
  public void removedService(final ServiceReference reference, final Object service) {
    log.debug("Removed service: {}", service);
    manager.unbindResource((JaxRSResource) service, reference);
  }

}
