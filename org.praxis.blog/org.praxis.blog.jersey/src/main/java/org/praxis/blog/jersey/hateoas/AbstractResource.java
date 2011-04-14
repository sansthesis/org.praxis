package org.praxis.blog.jersey.hateoas;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractResource implements Resource {
  protected final Logger log = LoggerFactory.getLogger(getClass());
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
