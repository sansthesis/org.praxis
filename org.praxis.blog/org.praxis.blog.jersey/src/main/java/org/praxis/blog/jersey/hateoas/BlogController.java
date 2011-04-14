package org.praxis.blog.jersey.hateoas;

import java.util.List;

import org.praxis.blog.Blog;

public interface BlogController extends BaseCrudApi<BlogResource, Blog> {
  @Override
  BlogResource get(long id);

  @Override
  List<BlogResource> list();

  List<StoryResource> listStories(long blogId);
}
