package org.praxis.personal.auth.client.impl;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.praxis.personal.auth.client.ClientAuthenticationService;

import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;

@Component
@Service
public class ClientAuthenticationServiceImpl implements ClientAuthenticationService {

  @Override
  public String generateHmac(final String username, final String realm, final String password) {
    Preconditions.checkNotNull(username, "Username is null.");
    Preconditions.checkNotNull(realm, "Realm is null.");
    Preconditions.checkNotNull(password, "Password is null.");

    final String ha1 = String.format("{%s:%s:%s}", username, realm, password);
    final byte[] bytes = ha1.getBytes(Charsets.UTF_8);
    final String md5 = DigestUtils.md5Hex(bytes);
    return md5;
  }

}
