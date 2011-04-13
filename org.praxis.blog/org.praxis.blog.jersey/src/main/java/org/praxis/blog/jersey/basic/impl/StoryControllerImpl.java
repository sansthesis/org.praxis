package org.praxis.blog.jersey.basic.impl;

import java.util.List;

import javax.ws.rs.Path;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.praxis.blog.Story;
import org.praxis.blog.dao.StoryDao;
import org.praxis.blog.jersey.basic.StoryController;

@Component(metatype = true, immediate = true)
@Service
@Path("/stories")
public class StoryControllerImpl implements StoryController {

  @Reference
  private StoryDao storyDao;

  @Override
  public Story get(final long id) {
    return storyDao.get(id);
  }

  @Override
  public List<Story> list() {
    return storyDao.list();
  }

}
