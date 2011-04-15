package org.praxis.blog.jersey.hateoas.resource;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.praxis.blog.jersey.hateoas.om.StoryResourceListRepresentation;

/**
 * The <code>StoriesResource</code> acts as the dispatch Resource for all Story objects.
 * @author Jason Rose
 * 
 */
@Path("/stories")
public interface StoriesResource extends Resource {

  /**
   * Delegates all requests for specific Stories to the StoryResource.
   * @return The Resource handling single Story objects.
   */
  @Path("/{storyId: [0-9]+}")
  StoryResource get();

  /**
   * Lists all Stories belonging to a specific Blog.
   * @param blogId The id of the Blog.
   * @param uriInfo Jersey contextual information.
   * @return All Stories belonging to a specific Blog.
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  List<StoryResourceListRepresentation> list(@PathParam("blogId") long blogId, @Context UriInfo uriInfo);
}
