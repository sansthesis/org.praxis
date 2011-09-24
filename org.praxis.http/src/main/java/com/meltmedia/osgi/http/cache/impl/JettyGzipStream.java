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
import java.util.zip.GZIPOutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/* ------------------------------------------------------------ */
/**
 */
public class JettyGzipStream extends ServletOutputStream {
  protected HttpServletRequest _request;
  protected HttpServletResponse _response;
  protected OutputStream _out;
  protected JettyByteArrayOutputStream2 _bOut;
  protected GZIPOutputStream _gzOut;
  protected boolean _closed;
  protected int _bufferSize;
  protected int _minGzipSize;
  protected long _contentLength;

  /**
   * Instantiates a new gzip stream.
   * 
   * @param request the request
   * @param response the response
   * @param contentLength the content length
   * @param bufferSize the buffer size
   * @param minGzipSize the min gzip size
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public JettyGzipStream(final HttpServletRequest request, final HttpServletResponse response, final long contentLength, final int bufferSize, final int minGzipSize) throws IOException {
    _request = request;
    _response = response;
    _contentLength = contentLength;
    _bufferSize = bufferSize;
    _minGzipSize = minGzipSize;
    if( minGzipSize == 0 ) {
      doGzip();
    }
  }

  /* ------------------------------------------------------------ */
  /**
   * @see java.io.OutputStream#close()
   */
  @Override
  public void close() throws IOException {
    if( _closed ) {
      return;
    }

    if( _request.getAttribute("javax.servlet.include.request_uri") != null ) {
      flush();
    } else {
      if( _bOut != null ) {
        if( _contentLength < 0 ) {
          _contentLength = _bOut.getCount();
        }
        if( _contentLength < _minGzipSize ) {
          doNotGzip();
        } else {
          doGzip();
        }
      } else if( _out == null ) {
        doNotGzip();
      }

      if( _gzOut != null ) {
        _gzOut.close();
      } else {
        _out.close();
      }
      _closed = true;
    }
  }

  /**
   * Do gzip.
   * 
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public void doGzip() throws IOException {
    if( _gzOut == null ) {
      if( _response.isCommitted() ) {
        throw new IllegalStateException();
      }

      if( setContentEncodingGzip() ) {
        _out = _gzOut = new GZIPOutputStream(_response.getOutputStream(), _bufferSize);

        if( _bOut != null ) {
          _out.write(_bOut.getBuf(), 0, _bOut.getCount());
          _bOut = null;
        }
      } else {
        doNotGzip();
      }
    }
  }

  /**
   * Do not gzip.
   * 
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public void doNotGzip() throws IOException {
    if( _gzOut != null ) {
      throw new IllegalStateException();
    }
    if( _out == null || _bOut != null ) {
      _out = _response.getOutputStream();
      if( _contentLength >= 0 ) {
        if( _contentLength < Integer.MAX_VALUE ) {
          _response.setContentLength((int) _contentLength);
        } else {
          _response.setHeader("Content-Length", Long.toString(_contentLength));
        }
      }

      if( _bOut != null ) {
        _out.write(_bOut.getBuf(), 0, _bOut.getCount());
      }
      _bOut = null;
    }
  }

  /**
   * Finish.
   * 
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public void finish() throws IOException {
    if( !_closed ) {
      if( _out == null || _bOut != null ) {
        if( _contentLength > 0 && _contentLength < _minGzipSize ) {
          doNotGzip();
        } else {
          doGzip();
        }
      }

      if( _gzOut != null && !_closed ) {
        _closed = true;
        _gzOut.close();
      }
    }
  }

  /* ------------------------------------------------------------ */
  /**
   * @see java.io.OutputStream#flush()
   */
  @Override
  public void flush() throws IOException {
    if( _out == null || _bOut != null ) {
      if( _contentLength > 0 && _contentLength < _minGzipSize ) {
        doNotGzip();
      } else {
        doGzip();
      }
    }

    _out.flush();
  }

  /**
   * Reset buffer.
   */
  public void resetBuffer() {
    if( _response.isCommitted() ) {
      throw new IllegalStateException("Committed");
    }
    _closed = false;
    _out = null;
    _bOut = null;
    if( _gzOut != null ) {
      _response.setHeader("Content-Encoding", null);
    }
    _gzOut = null;
  }

  /**
   * Sets the content length.
   * 
   * @param length the new content length
   */
  public void setContentLength(final long length) {
    _contentLength = length;
  }

  /* ------------------------------------------------------------ */
  /**
   * @see java.io.OutputStream#write(byte[])
   */
  @Override
  public void write(final byte b[]) throws IOException {
    checkOut(b.length);
    _out.write(b);
  }

  /* ------------------------------------------------------------ */
  /**
   * @see java.io.OutputStream#write(byte[], int, int)
   */
  @Override
  public void write(final byte b[], final int off, final int len) throws IOException {
    checkOut(len);
    _out.write(b, off, len);
  }

  /* ------------------------------------------------------------ */
  /**
   * @see java.io.OutputStream#write(int)
   */
  @Override
  public void write(final int b) throws IOException {
    checkOut(1);
    _out.write(b);
  }

  /**
   * Allows derived implementations to replace PrintWriter implementation.
   */
  protected PrintWriter newWriter(final OutputStream out, final String encoding) throws UnsupportedEncodingException {
    return encoding == null ? new PrintWriter(out) : new PrintWriter(new OutputStreamWriter(out, encoding));
  }

  /**
   * Sets the content encoding gzip.
   * 
   * @return true, if successful
   */
  protected boolean setContentEncodingGzip() {
    _response.setHeader("Content-Encoding", "gzip");
    return _response.containsHeader("Content-Encoding");
  }

  /**
   * Check out.
   * 
   * @param length the length
   * @throws IOException Signals that an I/O exception has occurred.
   */
  private void checkOut(final int length) throws IOException {
    if( _closed ) {
      throw new IOException("CLOSED");
    }

    if( _out == null ) {
      if( _response.isCommitted() || (_contentLength >= 0 && _contentLength < _minGzipSize) ) {
        doNotGzip();
      } else if( length > _minGzipSize ) {
        doGzip();
      } else {
        _out = _bOut = new JettyByteArrayOutputStream2(_bufferSize);
      }
    } else if( _bOut != null ) {
      if( _response.isCommitted() || (_contentLength >= 0 && _contentLength < _minGzipSize) ) {
        doNotGzip();
      } else if( length >= (_bOut.getBuf().length - _bOut.getCount()) ) {
        doGzip();
      }
    }
  }
}
