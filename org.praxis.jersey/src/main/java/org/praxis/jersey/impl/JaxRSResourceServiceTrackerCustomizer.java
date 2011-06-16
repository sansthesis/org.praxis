package org.praxis.jersey.impl;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTrackerCustomizer;
import org.praxis.jersey.JaxRSApplicationManager;
import org.praxis.jersey.JaxRSResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This ServiceTrackerCustomizer knows how to bind JaxRSApplications to the JaxRSApplicationManager.
 * @author Jason Rose
 */
public class JaxRSResourceServiceTrackerCustomizer implements ServiceTrackerCustomizer {

  private final BundleContext bundleContext;
  private final JaxRSApplicationManager manager;
  private final Logger log = LoggerFactory.getLogger(getClass());

  public JaxRSResourceServiceTrackerCustomizer(final BundleContext bundleContext, final JaxRSApplicationManager manager) {
    this.bundleContext = bundleContext;
    this.manager = manager;
  }

  @Override
  public Object addingService(final ServiceReference reference) {
    final JaxRSResource service = (JaxRSResource) bundleContext.getService(reference);
    log.debug("Adding service: {}", service);
    manager.bindResource(service, reference);
    return service;
  }

  @Override
  public void modifiedService(final ServiceReference reference, final Object svc) {
    final JaxRSResource service = (JaxRSResource) svc;
    log.debug("Modified service: {}", service);
    manager.unbindResource(service, reference);
    manager.bindResource(service, reference);
  }

  @Override
  public void removedService(final ServiceReference reference, final Object svc) {
    final JaxRSResource service = (JaxRSResource) svc;
    log.debug("Removed service: {}", service);
    manager.unbindResource(service, reference);
  }

}
