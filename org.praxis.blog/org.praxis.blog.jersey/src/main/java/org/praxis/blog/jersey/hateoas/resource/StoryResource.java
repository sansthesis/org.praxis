package org.praxis.blog.jersey.hateoas.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.praxis.blog.jersey.hateoas.om.StoryResourceRepresentation;

public interface StoryResource {

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  StoryResourceRepresentation get(@PathParam("storyId") long storyId, @Context UriInfo uriInfo);
}
