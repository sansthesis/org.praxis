package org.praxis.blog.jersey.jackson.om;

import java.util.HashSet;
import java.util.Set;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.praxis.blog.Story;

public class FooImpl implements Foo {

  private Set<Story> stories = new HashSet<Story>();
  private String title;
  private long id;

  @Override
  public Long getId() {
    return id;
  }

  @Override
  @JsonIgnore
  public Set<Story> getStories() {
    return stories;
  }

  @Override
  public String getTitle() {
    return title;
  }

  @Override
  public void setId(final Long id) {
    this.id = id;
  }

  @Override
  public void setStories(final Set<Story> stories) {
    this.stories = stories;
  }

  @Override
  public void setTitle(final String title) {
    this.title = title;
  }

}
