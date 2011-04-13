package org.praxis.blog.jersey.hateoas;

import java.util.List;

public interface Resource {

  List<Link> getLinks();

  void setLinks(List<Link> links);
}
