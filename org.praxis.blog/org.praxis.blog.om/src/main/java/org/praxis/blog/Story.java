package org.praxis.blog;

import java.util.Date;
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
public class Story implements org.praxis.blog.Entity {

  private Long id;
  private String title;
  private String story;
  private Date date;
  private String createdBy;
  private Set<Comment> comments = new HashSet<Comment>();
  private Blog blog;

  @XmlTransient
  public Blog getBlog() {
    return blog;
  }

  @XmlTransient
  public Set<Comment> getComments() {
    return comments;
  }

  public String getCreatedBy() {
    return createdBy;
  }

  public Date getDate() {
    return date;
  }

  @Override
  @Id
  @GeneratedValue
  public Long getId() {
    return id;
  }

  public String getStory() {
    return story;
  }

  public String getTitle() {
    return title;
  }

  public void setBlog(final Blog blog) {
    this.blog = blog;
  }

  public void setComments(final Set<Comment> comments) {
    this.comments = comments;
  }

  public void setCreatedBy(final String createdBy) {
    this.createdBy = createdBy;
  }

  public void setDate(final Date date) {
    this.date = date;
  }

  @Override
  public void setId(final Long id) {
    this.id = id;
  }

  public void setStory(final String story) {
    this.story = story;
  }

  public void setTitle(final String title) {
    this.title = title;
  }

  @Override
  public String toString() {
    return new ReflectionToStringBuilder(this).toString();
  }

}
