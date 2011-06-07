package org.praxis.blog.jersey.jackson.impl;

import java.util.ArrayList;
import java.util.List;

import org.praxis.blog.jersey.jackson.Link;
import org.praxis.blog.jersey.jackson.ResourceRepresentation;

public class ResourceRepresentationImpl implements ResourceRepresentation {
  private List<Link> links = new ArrayList<Link>();

  @Override
  public List<Link> getLinks() {
    return links;
  }

  @Override
  public void setLinks(final List<Link> links) {
    this.links = links;
  }

}
