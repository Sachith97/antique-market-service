package com.sac.antiquemarketservice.service.impl;

import com.sac.antiquemarketservice.model.User;
import com.sac.antiquemarketservice.model.UserPrincipal;
import com.sac.antiquemarketservice.repository.UserRepository;
import com.sac.antiquemarketservice.service.CustomUserDetailService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Sachith Harshamal
 * @created 2023-08-15
 */
@Service
public class CustomUserDetailServiceImpl implements CustomUserDetailService {

    private final UserRepository userRepository;

    public CustomUserDetailServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);
        if(!user.isPresent()) {
            throw new UsernameNotFoundException("User 404");
        }
        return new UserPrincipal(user.get());
    }
}
