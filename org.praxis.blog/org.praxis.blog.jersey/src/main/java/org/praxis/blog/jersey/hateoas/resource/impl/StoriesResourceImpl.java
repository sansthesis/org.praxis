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
import org.praxis.blog.Story;
import org.praxis.blog.dao.StoryDao;
import org.praxis.blog.jersey.hateoas.AbstractController;
import org.praxis.blog.jersey.hateoas.Link;
import org.praxis.blog.jersey.hateoas.om.StoryResourceListRepresentation;
import org.praxis.blog.jersey.hateoas.resource.StoriesResource;
import org.praxis.blog.jersey.hateoas.resource.StoryResource;

@Component(metatype = true, immediate = true)
@Service
public class StoriesResourceImpl extends AbstractController implements StoriesResource {

  @Reference
  private StoryDao storyDao;

  @Reference
  private StoryResource storyResource;

  @Override
  @Path("/{storyId: [0-9]+}")
  public StoryResource get() {
    return storyResource;
  }

  @Override
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public List<StoryResourceListRepresentation> list(@PathParam("blogId") final long blogId, @Context final UriInfo uriInfo) {
    return wrap(storyDao.listByRelation("blog", blogId), uriInfo);
  }

  private List<StoryResourceListRepresentation> wrap(final List<Story> list, final UriInfo uriInfo) {
    final List<StoryResourceListRepresentation> output = new ArrayList<StoryResourceListRepresentation>();
    for( final Story entity : list ) {
      output.add(wrap(entity, uriInfo));
    }
    return output;
  }

  private StoryResourceListRepresentation wrap(final Story entity, final UriInfo uriInfo) {
    final StoryResourceListRepresentation resource = new StoryResourceListRepresentation(entity);
    final Link self = new Link(uriInfo.getRequestUriBuilder().path(getClass(), "get").build(entity.getId()).toString(), "self", "application/json");
    Collections.addAll(resource.getLinks(), self);
    return resource;
  }
}
