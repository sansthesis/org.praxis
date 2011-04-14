package org.praxis.blog.jersey.hateoas;

import javax.xml.bind.annotation.XmlRootElement;

import org.praxis.blog.Blog;

@XmlRootElement
public class BlogResource extends AbstractResource implements EntityResource<Blog> {
  private StoryResource stories;
  private Blog entity;

  public BlogResource() {
    super();
  }

  public BlogResource(final Blog entity) {
    this();
    this.entity = entity;
  }

  public StoryResource getStories() {
    return stories;
  }

  public String getTitle() {
    return entity.getTitle();
  }

  public void setStories(final StoryResource stories) {
    this.stories = stories;
  }

  public void setTitle(final String title) {
    entity.setTitle(title);
  }

  @Override
  public Blog unwrap() {
    return entity;
  }

  @Override
  public EntityResource<Blog> wrap(final Blog entity) {
    return new BlogResource(entity);
  }

}
