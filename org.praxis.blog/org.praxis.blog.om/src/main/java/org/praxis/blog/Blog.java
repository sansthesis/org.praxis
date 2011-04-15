package org.praxis.blog;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

@Entity
@XmlRootElement
public class Blog implements org.praxis.blog.Entity {
  private Long id;
  private String title;
  private String description;
  private Set<Story> stories = new HashSet<Story>();

  public String getDescription() {
    return description;
  }

  @Override
  @Id
  @GeneratedValue
  public Long getId() {
    return id;
  }

  @XmlTransient
  public Set<Story> getStories() {
    return stories;
  }

  public String getTitle() {
    return title;
  }

  public void setDescription(final String description) {
    this.description = description;
  }

  @Override
  public void setId(final Long id) {
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
