package org.praxis.blog.jersey.jackson;

import java.util.List;

import org.praxis.blog.jersey.hateoas.Link;

public interface ResourceRepresentation {

  List<Link> getLinks();

  void setLinks(List<Link> links);
}
