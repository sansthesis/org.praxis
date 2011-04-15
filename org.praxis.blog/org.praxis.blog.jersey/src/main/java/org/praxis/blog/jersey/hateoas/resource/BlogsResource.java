package org.praxis.blog.jersey.hateoas.resource;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.praxis.blog.jersey.hateoas.om.BlogResourceListRepresentation;

/**
 * The <code>BlogsResource</code> acts as the dispatch Resource for all Blog objects.
 * @author Jason Rose
 * 
 */
@Path("/blogs")
public interface BlogsResource extends Resource {

  /**
   * Delegates all requests for specific Blogs to the BlogResource.
   * @return The Resource handling single Blog objects.
   */
  @Path("/{blogId: [0-9]+}")
  BlogResource get();

  /**
   * Lists all Blog objects.
   * @return A List view of all Blog objects.
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  List<BlogResourceListRepresentation> list();
}
