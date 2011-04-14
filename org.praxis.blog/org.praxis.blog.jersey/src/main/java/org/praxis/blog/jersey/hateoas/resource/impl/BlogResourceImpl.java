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
import org.praxis.blog.Blog;
import org.praxis.blog.dao.BlogDao;
import org.praxis.blog.jersey.hateoas.AbstractController;
import org.praxis.blog.jersey.hateoas.Link;
import org.praxis.blog.jersey.hateoas.om.BlogResourceRepresentation;
import org.praxis.blog.jersey.hateoas.resource.BlogResource;
import org.praxis.blog.jersey.hateoas.resource.BlogsResource;
import org.praxis.blog.jersey.hateoas.resource.StoriesResource;

@Component(immediate = true, metatype = true)
@Service
public class BlogResourceImpl extends AbstractController implements BlogResource {

  @Reference
  private BlogDao blogDao;

  @Reference
  private StoriesResource storiesResource;

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @Override
  public BlogResourceRepresentation get(@PathParam("blogId") final long blogId, @Context final UriInfo uriInfo) {
    return wrap(blogDao.get(blogId), uriInfo);
  }

  @Path("/stories")
  public StoriesResource getStories() {
    return storiesResource;
  }

  private BlogResourceRepresentation wrap(final Blog entity, final UriInfo uriInfo) {
    final BlogResourceRepresentation resource = new BlogResourceRepresentation(entity);
    final Link self = new Link(uriInfo.getRequestUriBuilder().build().toString(), "self", "application/json");
    final Link root = new Link(uriInfo.getBaseUriBuilder().path(BlogsResource.class).build().toString(), "root", "application/json");
    final Link stories = new Link(uriInfo.getRequestUriBuilder().path(getMethod(getClass(), "getStories")).build(entity.getId()).toString(), "stories", "application/json");
    Collections.addAll(resource.getLinks(), self, root, stories);
    return resource;
  }
}
