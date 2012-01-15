package org.praxis.personal.minifier.impl;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.mozilla.javascript.ErrorReporter;
import org.mozilla.javascript.EvaluatorException;
import org.praxis.personal.minifier.CacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yahoo.platform.yui.compressor.CssCompressor;
import com.yahoo.platform.yui.compressor.JavaScriptCompressor;

@Component
@Service
public class YUICompressorImpl extends AbstractCompressorImpl {

  private final Logger log = LoggerFactory.getLogger(getClass());

  public YUICompressorImpl() {
    super();
  }

  protected YUICompressorImpl(final boolean combine, final boolean compress) {
    super(combine, compress);
  }

  protected YUICompressorImpl(final CacheService cache, final boolean combine, final boolean compress) {
    super(cache, combine, compress);
  }

  protected YUICompressorImpl(final CacheService cache) {
    super(cache);
  }

  @Override
  protected String compress(final String uri, final String contents) {
    final String output;
    if( isCss(uri) ) {
      output = compressCss(contents);
    } else if( isJs(uri) ) {
      output = compressJs(contents);
    } else {
      throw new UnsupportedOperationException("I don't know what " + uri + " should be.");
    }
    return output;
  }

  private boolean isJs(final String uri) {
    return uri.endsWith(".js");
  }

  private String compressCss(final String contents) {
    try {
      final CssCompressor compressor = new CssCompressor(new StringReader(contents));
      final StringWriter out = new StringWriter();
      compressor.compress(out, 120);
      return out.toString();
    } catch( final IOException e ) {
      throw new RuntimeException(e);
    }
  }

  private boolean isCss(final String uri) {
    return uri.endsWith(".css");
  }

  private String compressJs(final String contents) {
    try {
      final JavaScriptCompressor compressor = new JavaScriptCompressor(new StringReader(contents), new ErrorReporter() {

        @Override
        public void error(final String arg0, final String arg1, final int arg2, final String arg3, final int arg4) {
          log.error("({}): {} at {}:{}", new Object[] { arg1, arg0, arg2, arg4 });
        }

        @Override
        public EvaluatorException runtimeError(final String arg0, final String arg1, final int arg2, final String arg3, final int arg4) {
          return new EvaluatorException(arg0, arg1, arg2, arg3, arg4);
        }

        @Override
        public void warning(final String arg0, final String arg1, final int arg2, final String arg3, final int arg4) {
          log.info("({}): {} at {}:{}", new Object[] { arg1, arg0, arg2, arg4 });
        }
      });
      final StringWriter out = new StringWriter();
      compressor.compress(out, 0, true, true, false, false);
      return out.toString();
    } catch( final EvaluatorException e ) {
      throw new RuntimeException(e);
    } catch( final IOException e ) {
      throw new RuntimeException(e);
    }
  }
}
