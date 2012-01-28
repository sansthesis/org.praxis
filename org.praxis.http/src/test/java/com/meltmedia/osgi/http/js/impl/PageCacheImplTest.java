package com.meltmedia.osgi.http.js.impl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.meltmedia.osgi.http.cache.impl.PageCacheImpl;

public class PageCacheImplTest {

  private PageCacheImpl service;

  @Before
  public void before() throws Exception {
    service = new PageCacheImpl();
  }

  @After
  public void after() throws Exception {
    service = null;
  }

  @Test
  public void testConditionalGetDateHit() throws Exception {

  }

  @Test
  public void testConditionalGetDateMiss() throws Exception {

  }

  @Test
  public void testConditionalGetETagHit() throws Exception {

  }

  @Test
  public void testConditionalGetETagMiss() throws Exception {

  }

  @Test
  public void testGetHit() throws Exception {

  }

  @Test
  public void testGetMiss() throws Exception {

  }
}
