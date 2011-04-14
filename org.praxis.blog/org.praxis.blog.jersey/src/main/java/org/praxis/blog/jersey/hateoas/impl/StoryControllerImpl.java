package org.praxis.blog.jersey.hateoas.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.praxis.blog.Story;
import org.praxis.blog.dao.StoryDao;
import org.praxis.blog.jersey.hateoas.AbstractController;
import org.praxis.blog.jersey.hateoas.Link;
import org.praxis.blog.jersey.hateoas.StoryController;
import org.praxis.blog.jersey.hateoas.StoryResource;

@Component(metatype = true, immediate = true)
@Service
@Path("/stories")
public class StoryControllerImpl extends AbstractController implements StoryController {

  @Context
  UriInfo uriInfo;

  @Reference
  private StoryDao storyDao;

  @Override
  public StoryResource get(final long id) {
    return wrap(storyDao.get(id));
  }

  @Override
  public List<StoryResource> list() {
    return wrap(storyDao.list());
  }

  private List<StoryResource> wrap(final List<Story> list) {
    final List<StoryResource> output = new ArrayList<StoryResource>();
    for( final Story entity : list ) {
      output.add(wrap(entity));
    }
    return output;
  }

  private StoryResource wrap(final Story entity) {
    final UriBuilder baseBuilder = uriInfo.getBaseUriBuilder();
    final StoryResource resource = new StoryResource(entity);
    final Link self = new Link(baseBuilder.path(getClass()).path("{id}").build(entity.getId()).toString(), "self", "application/json");
    Collections.addAll(resource.getLinks(), self);
    return resource;
  }

}
