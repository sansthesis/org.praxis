package org.praxis.blog.jersey.hateoas.resource.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.praxis.blog.Comment;
import org.praxis.blog.dao.CommentDao;
import org.praxis.blog.jersey.hateoas.AbstractController;
import org.praxis.blog.jersey.hateoas.Link;
import org.praxis.blog.jersey.hateoas.om.CommentResourceListRepresentation;
import org.praxis.blog.jersey.hateoas.resource.CommentResource;
import org.praxis.blog.jersey.hateoas.resource.CommentsResource;

@Component(metatype = true, immediate = true)
@Service
public class CommentsResourceImpl extends AbstractController implements CommentsResource {

  @Reference
  private CommentDao commentDao;

  @Reference
  private CommentResource commentResource;

  @Override
  @Path("/{commentId: [0-9]+}")
  public CommentResource get() {
    return commentResource;
  }

  @Override
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public List<CommentResourceListRepresentation> list(@PathParam("storyId") final long storyId, @Context final UriInfo uriInfo) {
    return wrap(commentDao.listByRelation("story", storyId), uriInfo);
  }

  private CommentResourceListRepresentation wrap(final Comment entity, final UriInfo uriInfo) {
    final CommentResourceListRepresentation resource = new CommentResourceListRepresentation(entity);
    final Link self = new Link(uriInfo.getRequestUriBuilder().path(getClass(), "get").build(entity.getId()).toString(), "self", "application/json");
    Collections.addAll(resource.getLinks(), self);
    return resource;
  }

  private List<CommentResourceListRepresentation> wrap(final List<Comment> list, final UriInfo uriInfo) {
    final List<CommentResourceListRepresentation> output = new ArrayList<CommentResourceListRepresentation>();
    for( final Comment entity : list ) {
      output.add(wrap(entity, uriInfo));
    }
    return output;
  }
}
