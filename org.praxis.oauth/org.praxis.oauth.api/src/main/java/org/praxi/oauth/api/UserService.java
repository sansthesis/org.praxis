package org.praxi.oauth.api;

import org.praxis.oauth.om.User;

public interface UserService {

  UserContext login(String username, String password);

  UserContext register(String username, String password);

  UserContext changePassword(User user, String password, String newPassword);

  UserContext setPassword(User user, String newPassword);
}
