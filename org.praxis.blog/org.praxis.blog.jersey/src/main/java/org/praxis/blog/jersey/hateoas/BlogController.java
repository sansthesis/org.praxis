package org.praxis.blog.jersey.hateoas;

import java.util.List;

import org.praxis.blog.jersey.hateoas.om.BlogResourceRepresentation;
import org.praxis.blog.jersey.hateoas.om.StoryResourceRepresentation;

public interface BlogController extends BaseCrudApi<BlogResourceRepresentation> {
  List<StoryResourceRepresentation> listStories(long blogId);
}
