package com.sac.antiquemarketservice.service.impl;

import com.sac.antiquemarketservice.model.User;
import com.sac.antiquemarketservice.repository.UserRepository;
import com.sac.antiquemarketservice.service.UserService;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Sachith Harshamal
 * @created 2023-08-15
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
