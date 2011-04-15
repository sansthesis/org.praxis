package org.praxis.blog.jersey.jackson.om;

import java.util.Set;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonView;
import org.praxis.blog.Blog;
import org.praxis.blog.Story;
import org.praxis.blog.jersey.jackson.Views;

public class Foo extends Blog {

  @Override
  @JsonView(Views.Detail.class)
  public String getDescription() {
    return super.getDescription();
  }

  @Override
  @JsonIgnore
  public Set<Story> getStories() {
    return super.getStories();
  }

}
