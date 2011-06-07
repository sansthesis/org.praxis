package org.praxis.blog.jersey.jackson;

import java.util.List;

public interface ResourceRepresentation {

  List<Link> getLinks();

  void setLinks(List<Link> links);
}
