// ========================================================================
// Copyright (c) Webtide LLC
// ------------------------------------------------------------------------
// All rights reserved. This program and the accompanying materials
// are made available under the terms of the Eclipse Public License v1.0
// and Apache License v2.0 which accompanies this distribution.
// The Eclipse Public License is available at 
// http://www.eclipse.org/legal/epl-v10.html
// The Apache License v2.0 is available at
// http://www.opensource.org/licenses/apache2.0.php
// You may elect to redistribute this code under either of these licenses. 
// ========================================================================

package com.meltmedia.osgi.http.cache.impl;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Set;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/* ------------------------------------------------------------ */
/**
 */
public class JettyGzipResponseWrapper extends HttpServletResponseWrapper {
  private final HttpServletRequest _request;
  private Set<String> _mimeTypes;
  private int _bufferSize = 8192;
  private int _minGzipSize = 256;

  private PrintWriter _writer;
  private JettyGzipStream _gzStream;
  private long _contentLength = -1;
  private boolean _noGzip;

  /**
   * Instantiates a new gzip response wrapper.
   * 
   * @param request the request
   * @param response the response
   */
  public JettyGzipResponseWrapper(final HttpServletRequest request, final HttpServletResponse response) {
    super(response);
    _request = request;
  }

  /* ------------------------------------------------------------ */
  /**
   * @see javax.servlet.http.HttpServletResponseWrapper#addHeader(java.lang.String, java.lang.String)
   */
  @Override
  public void addHeader(final String name, final String value) {
    if( "content-length".equalsIgnoreCase(name) ) {
      _contentLength = Long.parseLong(value);
      if( _gzStream != null ) {
        _gzStream.setContentLength(_contentLength);
      }
    } else if( "content-type".equalsIgnoreCase(name) ) {
      setContentType(value);
    } else if( "content-encoding".equalsIgnoreCase(name) ) {
      super.addHeader(name, value);
      if( !isCommitted() ) {
        noGzip();
      }
    } else {
      super.addHeader(name, value);
    }
  }

  /* ------------------------------------------------------------ */
  /**
   * Finish.
   * 
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public void finish() throws IOException {
    if( _writer != null && !_gzStream._closed ) {
      _writer.flush();
    }
    if( _gzStream != null ) {
      _gzStream.finish();
    }
  }

  /* ------------------------------------------------------------ */
  /**
   * @see javax.servlet.ServletResponseWrapper#flushBuffer()
   */
  @Override
  public void flushBuffer() throws IOException {
    if( _writer != null ) {
      _writer.flush();
    }
    if( _gzStream != null ) {
      _gzStream.finish();
    } else {
      getResponse().flushBuffer();
    }
  }

  /* ------------------------------------------------------------ */
  /**
   * @see javax.servlet.ServletResponseWrapper#getOutputStream()
   */
  @Override
  public ServletOutputStream getOutputStream() throws IOException {
    if( _gzStream == null ) {
      if( getResponse().isCommitted() || _noGzip ) {
        return getResponse().getOutputStream();
      }

      _gzStream = newGzipStream(_request, (HttpServletResponse) getResponse(), _contentLength, _bufferSize, _minGzipSize);
    } else if( _writer != null ) {
      throw new IllegalStateException("getWriter() called");
    }

    return _gzStream;
  }

  /* ------------------------------------------------------------ */
  /**
   * @see javax.servlet.ServletResponseWrapper#getWriter()
   */
  @Override
  public PrintWriter getWriter() throws IOException {
    if( _writer == null ) {
      if( _gzStream != null ) {
        throw new IllegalStateException("getOutputStream() called");
      }

      if( getResponse().isCommitted() || _noGzip ) {
        return getResponse().getWriter();
      }

      _gzStream = newGzipStream(_request, (HttpServletResponse) getResponse(), _contentLength, _bufferSize, _minGzipSize);
      _writer = newWriter(_gzStream, getCharacterEncoding());
    }
    return _writer;
  }

  /* ------------------------------------------------------------ */
  /**
   * No gzip.
   */
  public void noGzip() {
    _noGzip = true;
    if( _gzStream != null ) {
      try {
        _gzStream.doNotGzip();
      } catch( final IOException e ) {
        throw new IllegalStateException(e);
      }
    }
  }

  /* ------------------------------------------------------------ */
  /**
   * @see javax.servlet.ServletResponseWrapper#reset()
   */
  @Override
  public void reset() {
    super.reset();
    if( _gzStream != null ) {
      _gzStream.resetBuffer();
    }
    _writer = null;
    _gzStream = null;
    _noGzip = false;
    _contentLength = -1;
  }

  /* ------------------------------------------------------------ */
  /**
   * @see javax.servlet.ServletResponseWrapper#resetBuffer()
   */
  @Override
  public void resetBuffer() {
    super.resetBuffer();
    if( _gzStream != null ) {
      _gzStream.resetBuffer();
    }
    _writer = null;
    _gzStream = null;
  }

  /* ------------------------------------------------------------ */
  /**
   * @see javax.servlet.http.HttpServletResponseWrapper#sendError(int)
   */
  @Override
  public void sendError(final int sc) throws IOException {
    resetBuffer();
    super.sendError(sc);
  }

  /* ------------------------------------------------------------ */
  /**
   * @see javax.servlet.http.HttpServletResponseWrapper#sendError(int, java.lang.String)
   */
  @Override
  public void sendError(final int sc, final String msg) throws IOException {
    resetBuffer();
    super.sendError(sc, msg);
  }

  /* ------------------------------------------------------------ */
  /**
   * @see javax.servlet.http.HttpServletResponseWrapper#sendRedirect(java.lang.String)
   */
  @Override
  public void sendRedirect(final String location) throws IOException {
    resetBuffer();
    super.sendRedirect(location);
  }

  /* ------------------------------------------------------------ */
  /**
   * @see javax.servlet.ServletResponseWrapper#setBufferSize(int)
   */
  @Override
  public void setBufferSize(final int bufferSize) {
    _bufferSize = bufferSize;
  }

  /* ------------------------------------------------------------ */
  /**
   * @see javax.servlet.ServletResponseWrapper#setContentLength(int)
   */
  @Override
  public void setContentLength(final int length) {
    _contentLength = length;
    if( _gzStream != null ) {
      _gzStream.setContentLength(length);
    }
  }

  /* ------------------------------------------------------------ */
  /**
   * @see javax.servlet.ServletResponseWrapper#setContentType(java.lang.String)
   */
  @Override
  public void setContentType(String ct) {
    super.setContentType(ct);

    if( ct != null ) {
      final int colon = ct.indexOf(";");
      if( colon > 0 ) {
        ct = ct.substring(0, colon);
      }
    }

    if( (_gzStream == null || _gzStream._out == null) && (_mimeTypes == null && "application/gzip".equalsIgnoreCase(ct) || _mimeTypes != null && (ct == null || !_mimeTypes.contains(ct.toLowerCase()))) ) {
      noGzip();
    }
  }

  /* ------------------------------------------------------------ */
  /**
   * @see javax.servlet.http.HttpServletResponseWrapper#setHeader(java.lang.String, java.lang.String)
   */
  @Override
  public void setHeader(final String name, final String value) {
    if( "content-length".equalsIgnoreCase(name) ) {
      _contentLength = Long.parseLong(value);
      if( _gzStream != null ) {
        _gzStream.setContentLength(_contentLength);
      }
    } else if( "content-type".equalsIgnoreCase(name) ) {
      setContentType(value);
    } else if( "content-encoding".equalsIgnoreCase(name) ) {
      super.setHeader(name, value);
      if( !isCommitted() ) {
        noGzip();
      }
    } else {
      super.setHeader(name, value);
    }
  }

  /* ------------------------------------------------------------ */
  /**
   * @see javax.servlet.http.HttpServletResponseWrapper#setIntHeader(java.lang.String, int)
   */
  @Override
  public void setIntHeader(final String name, final int value) {
    if( "content-length".equalsIgnoreCase(name) ) {
      _contentLength = value;
      if( _gzStream != null ) {
        _gzStream.setContentLength(_contentLength);
      }
    } else {
      super.setIntHeader(name, value);
    }
  }

  /* ------------------------------------------------------------ */
  /**
   * Sets the mime types.
   * 
   * @param mimeTypes the new mime types
   */
  public void setMimeTypes(final Set<String> mimeTypes) {
    _mimeTypes = mimeTypes;
  }

  /* ------------------------------------------------------------ */
  /**
   * Sets the min gzip size.
   * 
   * @param minGzipSize the new min gzip size
   */
  public void setMinGzipSize(final int minGzipSize) {
    _minGzipSize = minGzipSize;
  }

  /* ------------------------------------------------------------ */
  /**
   * @see javax.servlet.http.HttpServletResponseWrapper#setStatus(int)
   */
  @Override
  public void setStatus(final int sc) {
    super.setStatus(sc);
    if( sc < 200 || sc >= 300 ) {
      //      noGzip();
    }
  }

  /* ------------------------------------------------------------ */
  /**
   * @see javax.servlet.http.HttpServletResponseWrapper#setStatus(int, java.lang.String)
   */
  @Override
  public void setStatus(final int sc, final String sm) {
    super.setStatus(sc, sm);
    if( sc < 200 || sc >= 300 ) {
      noGzip();
    }
  }

  /* ------------------------------------------------------------ */
  /**
   * Allows derived implementations to replace GzipStream implementation.
   * 
   * @param request the request
   * @param response the response
   * @param contentLength the content length
   * @param bufferSize the buffer size
   * @param minGzipSize the min gzip size
   * @return the gzip stream
   * @throws IOException Signals that an I/O exception has occurred.
   */
  protected JettyGzipStream newGzipStream(final HttpServletRequest request, final HttpServletResponse response, final long contentLength, final int bufferSize, final int minGzipSize) throws IOException {
    return new JettyGzipStream(request, response, contentLength, bufferSize, minGzipSize);
  }

  /* ------------------------------------------------------------ */
  /**
   * Allows derived implementations to replace PrintWriter implementation.
   * 
   * @param out the out
   * @param encoding the encoding
   * @return the prints the writer
   * @throws UnsupportedEncodingException the unsupported encoding exception
   */
  protected PrintWriter newWriter(final OutputStream out, final String encoding) throws UnsupportedEncodingException {
    return encoding == null ? new PrintWriter(out) : new PrintWriter(new OutputStreamWriter(out, encoding));
  }
}
