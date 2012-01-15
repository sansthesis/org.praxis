package org.praxis.personal.minifier.impl;

import java.io.File;
import java.net.URI;
import java.util.Arrays;
import java.util.List;

import junit.framework.Assert;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AbstractCompressorImplTest {

  private static final String CSS = "body%{%background-color : #b0c4de;%}%";
  private MapCacheServiceImpl cache;

  private AbstractCompressorImpl service;

  @After
  public void after() throws Exception {
    cache.deactivate();
    cache = null;
    service = null;
  }

  @Before
  public void before() throws Exception {
    cache = new MapCacheServiceImpl();
    cache.activate();
    service = new AbstractCompressorImpl(cache);
  }

  @Test
  public void testNothing() throws Exception {
    final String[] inputs = new String[] {};
    final List<URI> outputs = service.generateCompressedFiles("http://localhost", inputs);
    Assert.assertEquals(0, outputs.size());
    Assert.assertEquals(inputs.length, outputs.size());
  }

  @Test
  public void testPassthrough() throws Exception {
    final String[] inputs = new String[] { "foo/bar/baz", "/foo/bar/baz" };
    final List<URI> outputs = service.generateCompressedFiles("http://localhost/deep/", inputs);
    Assert.assertEquals(2, outputs.size());
    Assert.assertEquals(inputs.length, outputs.size());
    Assert.assertEquals("http://localhost/deep/foo/bar/baz", outputs.get(0).toString());
    Assert.assertEquals("http://localhost/foo/bar/baz", outputs.get(1).toString());
  }

  @Test
  public void testSimpleCombine() throws Exception {
    service = new AbstractCompressorImpl(cache, true, false);
    final String file1 = createCssAt("foo");
    final String file2 = createCssAt("foo/bar");
    final String[] inputs = new String[] { file1, file2 };
    final List<URI> outputs = service.generateCompressedFiles("http://localhost/deep/", inputs);
    Assert.assertEquals(1, outputs.size());
    Assert.assertNotNull(outputs.get(0).toString());
    Assert.assertEquals(createExpectedOutput(false, file1, file2), cache.get(outputs.get(0).toString()));
  }

  @Test
  public void testExternalCombine() throws Exception {
    service = new AbstractCompressorImpl(cache, true, false);
    final String file1 = createCssAt("foo");
    final String file2 = createCssAt("foo/bar");
    final String[] inputs = new String[] { file1, file2, "http://code.jquery.com/jquery-1.7.1.js" };
    final List<URI> outputs = service.generateCompressedFiles("http://localhost/deep/", inputs);
    Assert.assertEquals(1, outputs.size());
    Assert.assertNotNull(outputs.get(0).toString());
    Assert.assertEquals(createExpectedOutput(false, inputs), cache.get(outputs.get(0).toString()));
  }

  private String createCssAt(final String path) throws Exception {
    final File tmpFile = File.createTempFile(path.replace("/", "-"), null);
    FileUtils.writeLines(tmpFile, Arrays.asList(CSS.split("%")));
    return tmpFile.toURI().toString();
  }

  private String createExpectedOutput(final boolean compress, final String... files) throws Exception {
    final StringBuilder buffer = new StringBuilder();
    for( final String url : files ) {
      buffer.append("/*" + url + "*/\n");
      String contents = IOUtils.toString(new URI(url));
      if( compress ) {
        contents = contents.replaceAll("\\r|\\n", "");
      }
      buffer.append(contents);
      buffer.append("\n");
    }
    return buffer.toString();
  }

}
