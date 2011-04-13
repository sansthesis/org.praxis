package org.praxis.blog.jersey.basic;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.praxis.blog.Comment;

public interface CommentController extends BaseCrudApi<Comment> {
  @Override
  @Path("/{id}")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  Comment get(@PathParam("id") long id);

  @Override
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  List<Comment> list();
}
