package org.praxis.blog.jersey.hateoas.resource;

import javax.ws.rs.Path;

@Path("/blogs")
public interface BlogsResource {

  @Path("/{blogId: [0-9]+}")
  BlogResource get();
}
