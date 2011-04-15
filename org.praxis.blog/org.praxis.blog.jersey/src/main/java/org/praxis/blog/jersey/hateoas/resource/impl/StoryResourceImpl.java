package org.praxis.blog.jersey.hateoas.resource.impl;

import java.util.Collections;

import javax.ws.rs.Consumes;
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
import org.praxis.blog.Story;
import org.praxis.blog.dao.StoryDao;
import org.praxis.blog.jersey.hateoas.AbstractController;
import org.praxis.blog.jersey.hateoas.Link;
import org.praxis.blog.jersey.hateoas.om.StoryResourceRepresentation;
import org.praxis.blog.jersey.hateoas.resource.BlogsResource;
import org.praxis.blog.jersey.hateoas.resource.CommentsResource;
import org.praxis.blog.jersey.hateoas.resource.StoryResource;

@Component(immediate = true, metatype = true)
@Service
public class StoryResourceImpl extends AbstractController implements StoryResource {

  @Reference
  private StoryDao storyDao;

  @Reference
  private CommentsResource commentsResource;

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @Override
  public StoryResourceRepresentation get(@PathParam("storyId") final long storyId, @Context final UriInfo uriInfo) {
    return wrap(storyDao.get(storyId), uriInfo);
  }

  @Path("/comments")
  public CommentsResource getComments() {
    return commentsResource;
  }

  private StoryResourceRepresentation wrap(final Story entity, final UriInfo uriInfo) {
    final StoryResourceRepresentation resource = new StoryResourceRepresentation(entity);
    final Link self = new Link(uriInfo.getRequestUriBuilder().build().toString(), "self", "application/json");
    final Link root = new Link(uriInfo.getAbsolutePathBuilder().path("..").build().normalize().toString(), "root", "application/json");
    final Link blog = new Link(uriInfo.getBaseUriBuilder().path(BlogsResource.class).path(BlogsResource.class, "get").build(entity.getBlog().getId()).toString(), "blog", "application/json");
    final Link comments = new Link(uriInfo.getRequestUriBuilder().path(getClass(), "getComments").build().toString(), "blog", "application/json");
    Collections.addAll(resource.getLinks(), self, root, blog, comments);
    return resource;
  }
}
