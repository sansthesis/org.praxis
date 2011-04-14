package org.praxis.blog.jersey.hateoas;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

import org.praxis.blog.Story;

@XmlRootElement
public class StoryResource extends AbstractResource implements EntityResource<Story> {

  private Story entity;

  public StoryResource() {
    super();
  }

  public StoryResource(final Story entity) {
    this();
    this.entity = entity;
  }

  public String getCreatedBy() {
    return entity.getCreatedBy();
  }

  public Date getDate() {
    return entity.getDate();
  }

  public String getStory() {
    return entity.getStory();
  }

  public String getTitle() {
    return entity.getTitle();
  }

  public void setCreatedBy(final String createdBy) {
    entity.setCreatedBy(createdBy);
  }

  public void setDate(final Date date) {
    entity.setDate(date);
  }

  public void setStory(final String story) {
    entity.setStory(story);
  }

  public void setTitle(final String title) {
    entity.setTitle(title);
  }

  @Override
  public Story unwrap() {
    return entity;
  }

  @Override
  public EntityResource<Story> wrap(final Story entity) {
    return new StoryResource(entity);
  }

}
