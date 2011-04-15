package org.praxis.blog.jersey.hateoas.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.praxis.blog.jersey.hateoas.om.CommentResourceRepresentation;

public interface CommentResource {

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  CommentResourceRepresentation get(@PathParam("commentId") long commentId, @Context UriInfo uriInfo);
}
