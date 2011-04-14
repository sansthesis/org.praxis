package org.praxis.blog.jersey.hateoas.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.praxis.blog.Blog;
import org.praxis.blog.Story;
import org.praxis.blog.dao.BlogDao;
import org.praxis.blog.dao.StoryDao;
import org.praxis.blog.jersey.hateoas.AbstractController;
import org.praxis.blog.jersey.hateoas.BlogController;
import org.praxis.blog.jersey.hateoas.BlogResource;
import org.praxis.blog.jersey.hateoas.Link;
import org.praxis.blog.jersey.hateoas.StoryResource;

@Component(metatype = true, immediate = true)
@Service
@Path("/blogs")
public class BlogControllerImpl extends AbstractController implements BlogController {

  @Context
  UriInfo uriInfo;

  @Reference
  private BlogDao blogDao;

  @Reference
  private StoryDao storyDao;

  @Override
  @Path("/{id}")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public BlogResource get(@PathParam("id") final long id) {
    return wrap(blogDao.get(id));
  }

  @Override
  @GET
  @Path("/")
  @Produces(MediaType.APPLICATION_JSON)
  public List<BlogResource> list() {
    return wrap(blogDao.list());
  }

  @Override
  @Path("/{id}/stories")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public List<StoryResource> listStories(@PathParam("id") final long blogId) {
    return wrapStories(storyDao.listByRelation("blog", blogId));
  }

  private BlogResource wrap(final Blog entity) {
    final BlogResource resource = new BlogResource(entity);
    final Link self = new Link(uriInfo.getBaseUriBuilder().path(getClass()).path(getMethod(getClass(), "get", long.class)).build(entity.getId()).toString(), "self", "application/json");
    final Link root = new Link(uriInfo.getBaseUriBuilder().path(getClass()).path(getMethod(getClass(), "list")).build().toString(), "root", "application/json");
    final Link stories = new Link(uriInfo.getBaseUriBuilder().path(getClass()).path(getClass(), "listStories").build(entity.getId()).toString(), "stories", "application/json");
    Collections.addAll(resource.getLinks(), self, stories, root);
    return resource;
  }

  private List<BlogResource> wrap(final List<Blog> list) {
    final List<BlogResource> output = new ArrayList<BlogResource>();
    for( final Blog entity : list ) {
      output.add(wrap(entity));
    }
    return output;
  }

  private StoryResource wrap(final Story entity) {
    final UriBuilder baseBuilder = uriInfo.getBaseUriBuilder();
    final StoryResource resource = new StoryResource(entity);
    final Link self = new Link(baseBuilder.path(StoryControllerImpl.class).path("{id}").build(entity.getId()).toString(), "self", "application/json");
    Collections.addAll(resource.getLinks(), self);
    return resource;
  }

  private List<StoryResource> wrapStories(final List<Story> list) {
    final List<StoryResource> output = new ArrayList<StoryResource>();
    for( final Story entity : list ) {
      output.add(wrap(entity));
    }
    return output;
  }

}
