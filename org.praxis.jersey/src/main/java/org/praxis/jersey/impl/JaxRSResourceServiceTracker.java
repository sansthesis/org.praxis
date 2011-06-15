package org.praxis.jersey.impl;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTrackerCustomizer;
import org.praxis.jersey.JaxRSResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JaxRSResourceServiceTracker implements ServiceTrackerCustomizer {
  
  private BundleContext bundleContext;
  private OsgiJaxRSApplicationManagerImpl manager;
  private final Logger log = LoggerFactory.getLogger(getClass());
  
  public JaxRSResourceServiceTracker(BundleContext bundleContext, OsgiJaxRSApplicationManagerImpl manager) {
    this.bundleContext = bundleContext;
    this.manager = manager;
  }

  @Override
  public void removedService(ServiceReference reference, Object service) {
    log.info("Removed service: {}", service);
    manager.unbindResource((JaxRSResource) service, reference);
  }
  
  @Override
  public void modifiedService(ServiceReference reference, Object service) {
    log.info("Modified service: {}", service);
    manager.unbindResource((JaxRSResource) service, reference);
    manager.bindResource((JaxRSResource) service, reference);
  }
  
  @Override
  public Object addingService(ServiceReference reference) {
    final Object service = bundleContext.getService(reference);
    log.info("Adding service: {}", service);
    manager.bindResource((JaxRSResource) service, reference);
    return service;
  }

}
