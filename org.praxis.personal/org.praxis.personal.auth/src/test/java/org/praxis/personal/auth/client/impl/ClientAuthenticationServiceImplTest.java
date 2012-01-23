package org.praxis.personal.auth.client.impl;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ClientAuthenticationServiceImplTest {

  private ClientAuthenticationServiceImpl service;

  @Before
  public void before() throws Exception {
    service = new ClientAuthenticationServiceImpl();
  }

  @Test(expected = NullPointerException.class)
  public void testGenerateHmacInvalidUsername() throws Exception {
    service.generateHmac(null, "realm", "pass");
  }

  @Test(expected = NullPointerException.class)
  public void testGenerateHmacInvalidRealm() throws Exception {
    service.generateHmac("user", null, "pass");
  }

  @Test(expected = NullPointerException.class)
  public void testGenerateHmacInvalidPassword() throws Exception {
    service.generateHmac("user", "realm", null);
  }

  @Test
  public void testGenerateHmac() throws Exception {
    final String hmac = service.generateHmac("user", "realm", "pass");
    Assert.assertEquals("fdda146e83f0866f23f6130082c5587c", hmac);
  }
}
