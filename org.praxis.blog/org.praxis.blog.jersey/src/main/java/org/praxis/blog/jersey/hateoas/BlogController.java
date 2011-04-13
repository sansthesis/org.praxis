package org.praxis.blog.jersey.hateoas;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.praxis.blog.Blog;

public interface BlogController extends BaseCrudApi<BlogResource, Blog> {
  @Override
  @Path("/{id}")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  BlogResource get(@PathParam("id") long id);

  @Override
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  List<BlogResource> list();
}