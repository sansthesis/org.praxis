package org.praxis.blog;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

@Entity
@XmlRootElement
public class Comment implements org.praxis.blog.Entity {

  private Long id;
  private String author;
  private String body;
  private Date date;
  private Story story;
  private String subject;

  public String getAuthor() {
    return author;
  }

  public String getBody() {
    return body;
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

  @XmlTransient
  public Story getStory() {
    return story;
  }

  public String getSubject() {
    return subject;
  }

  public void setAuthor(final String author) {
    this.author = author;
  }

  public void setBody(final String body) {
    this.body = body;
  }

  public void setDate(final Date date) {
    this.date = date;
  }

  @Override
  public void setId(final Long id) {
    this.id = id;
  }

  public void setStory(final Story story) {
    this.story = story;
  }

  public void setSubject(final String subject) {
    this.subject = subject;
  }

  @Override
  public String toString() {
    return new ReflectionToStringBuilder(this).toString();
  }

}
