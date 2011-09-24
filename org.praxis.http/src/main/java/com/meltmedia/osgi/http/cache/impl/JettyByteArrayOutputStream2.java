// ========================================================================
// Copyright (c) 2004-2009 Mort Bay Consulting Pty. Ltd.
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

import java.io.ByteArrayOutputStream;

/* ------------------------------------------------------------ */
/**
 * ByteArrayOutputStream with public internals
 * 
 * 
 */
public class JettyByteArrayOutputStream2 extends ByteArrayOutputStream {
  public JettyByteArrayOutputStream2() {
    super();
  }

  public JettyByteArrayOutputStream2(final int size) {
    super(size);
  }

  public byte[] getBuf() {
    return buf;
  }

  public int getCount() {
    return count;
  }

  public void reset(final int minSize) {
    reset();
    if( buf.length < minSize ) {
      buf = new byte[minSize];
    }
  }

  public void setCount(final int count) {
    this.count = count;
  }

  public void writeUnchecked(final int b) {
    buf[count++] = (byte) b;
  }

}
