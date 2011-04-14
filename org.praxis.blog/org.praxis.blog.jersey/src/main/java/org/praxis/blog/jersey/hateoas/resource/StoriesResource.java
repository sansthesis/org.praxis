package org.praxis.blog.jersey.hateoas.resource;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.praxis.blog.jersey.hateoas.om.StoryResourceListRepresentation;

public interface StoriesResource {

  @Path("/{storyId: [0-9]+}")
  StoryResource get();

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  List<StoryResourceListRepresentation> list(@Context UriInfo uriInfo);
}
