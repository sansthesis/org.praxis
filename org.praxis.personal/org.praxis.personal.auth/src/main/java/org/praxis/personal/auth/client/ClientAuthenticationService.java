package org.praxis.personal.auth.client;

public interface ClientAuthenticationService {

  //  void authenticate(String username, String realm, String hmac);

  String generateHmac(String username, String realm, String password);
}
