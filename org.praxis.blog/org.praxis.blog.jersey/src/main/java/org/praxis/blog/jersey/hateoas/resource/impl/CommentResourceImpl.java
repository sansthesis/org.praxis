package org.praxis.blog.jersey.hateoas.resource.impl;

import java.util.Collections;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
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
import org.praxis.blog.jersey.hateoas.om.CommentResourceRepresentation;
import org.praxis.blog.jersey.hateoas.resource.CommentResource;

@Component(immediate = true, metatype = true)
@Service
public class CommentResourceImpl extends AbstractController implements CommentResource {

  @Reference
  private CommentDao commentDao;

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @Override
  public CommentResourceRepresentation get(@PathParam("commentId") final long commentId, @Context final UriInfo uriInfo) {
    return wrap(commentDao.get(commentId), uriInfo);
  }

  private CommentResourceRepresentation wrap(final Comment entity, final UriInfo uriInfo) {
    final CommentResourceRepresentation resource = new CommentResourceRepresentation(entity);
    final Link self = new Link(uriInfo.getRequestUriBuilder().build().toString(), "self", "application/json");
    final Link root = new Link(uriInfo.getAbsolutePathBuilder().path("..").build().normalize().toString(), "root", "application/json");
    final Link story = new Link(uriInfo.getAbsolutePathBuilder().path("..").path("..").build().normalize().toString(), "story", "application/json");
    Collections.addAll(resource.getLinks(), self, root, story);
    return resource;
  }
}
