package com.sac.antiquemarketservice.service;

import com.sac.antiquemarketservice.model.User;

import java.util.Optional;

/**
 * @author Sachith Harshamal
 * @created 2023-08-15
 */
public interface UserService {

    Optional<User> findUserByUsername(String username);

    Optional<User> getLoggedInUser();
}
