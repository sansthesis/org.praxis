package org.praxis.personal.auth.client;

public interface ClientAuthenticationService {

  //  void authenticate(String username, String realm, String hmac);

  /**
   * Generates an MD5-HMAC of the arguments in a scheme similar to RFC 2617, Digest Authentication. This allows us to attempt authentication without transmitting a password from server to server.
   * @param username The username to include in the hash.
   * @param realm The realm to include in the hash.
   * @param password The password to include in the hash.
   * @return The HA1 segment of RFC 2617.
   */
  String generateHmac(String username, String realm, String password);
}
