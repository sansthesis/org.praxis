package org.praxis.blog.jersey.hateoas;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.praxis.blog.Blog;

@XmlRootElement
public class BlogResource implements EntityResource<Blog> {
  private List<Link> links = new ArrayList<Link>();
  private StoryResource stories;
  private Blog entity;

  public BlogResource() {
    super();
  }

  public BlogResource(final Blog entity) {
    this();
    this.entity = entity;
  }

  @Override
  public List<Link> getLinks() {
    return links;
  }

  @Override
  public void setLinks(final List<Link> links) {
    this.links = links;
  }

  @Override
  public EntityResource<Blog> wrap(final Blog entity) {
    return new BlogResource(entity);
  }

  @Override
  public Blog unwrap() {
    return entity;
  }

  public void setTitle(final String title) {
    entity.setTitle(title);
  }

  public String getTitle() {
    return entity.getTitle();
  }

  public void setStories(final StoryResource stories) {
    this.stories = stories;
  }

  public StoryResource getStories() {
    return stories;
  }

}
