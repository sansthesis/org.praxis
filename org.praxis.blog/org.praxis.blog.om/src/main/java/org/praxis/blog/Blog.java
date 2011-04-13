package org.praxis.blog;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

@Entity
@XmlRootElement
public class Blog implements org.praxis.blog.Entity {
  private long id;
  private String title;
  private Set<Story> stories = new HashSet<Story>();

  @Override
  @Id
  @GeneratedValue
  public long getId() {
    return id;
  }

  public Set<Story> getStories() {
    return stories;
  }

  public String getTitle() {
    return title;
  }

  @Override
  public void setId(final long id) {
    this.id = id;
  }

  public void setStories(final Set<Story> stories) {
    this.stories = stories;
  }

  public void setTitle(final String title) {
    this.title = title;
  }

  @Override
  public String toString() {
    return new ReflectionToStringBuilder(this).toString();
  }
}
