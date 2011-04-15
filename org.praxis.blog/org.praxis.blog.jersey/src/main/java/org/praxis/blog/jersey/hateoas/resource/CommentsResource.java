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

/**
 * The <code>CommentsResource</code> acts as the delegate for all Comment objects.
 * @author Jason Rose
 * 
 */
@Path("/comments")
public interface CommentsResource extends Resource {

  /**
   * Delegates all requests for specific Comments to the CommentResource.
   * @return The Resource handling single Comment objects.
   */
  @Path("/{commentId: [0-9]+}")
  CommentResource get();

  /**
   * Lists all Comments belonging to a specific Story.
   * @param storyId The id of the Story.
   * @param uriInfo Jersey contextual information.
   * @return All Comments belonging to a specific Story.
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  List<CommentResourceListRepresentation> list(@PathParam("storyId") long storyId, @Context UriInfo uriInfo);
}
