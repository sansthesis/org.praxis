package org.praxis.blog.jersey.hateoas.resource;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.praxis.blog.jersey.hateoas.om.CommentResourceListRepresentation;

public interface CommentsResource {

  @Path("/{commentId: [0-9]+}")
  CommentResource get();

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  List<CommentResourceListRepresentation> list(@PathParam("storyId") long storyId, @Context UriInfo uriInfo);
}
