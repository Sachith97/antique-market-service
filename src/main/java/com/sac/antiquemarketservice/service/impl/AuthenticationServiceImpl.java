package com.sac.antiquemarketservice.service.impl;

import com.sac.antiquemarketservice.dao.AuthenticationRequestDao;
import com.sac.antiquemarketservice.dao.AuthenticationResponseDao;
import com.sac.antiquemarketservice.dao.UserDao;
import com.sac.antiquemarketservice.enums.Response;
import com.sac.antiquemarketservice.exception.CommonResponse;
import com.sac.antiquemarketservice.model.User;
import com.sac.antiquemarketservice.service.AuthenticationService;
import com.sac.antiquemarketservice.service.CustomUserDetailService;
import com.sac.antiquemarketservice.service.JwtTokenService;
import com.sac.antiquemarketservice.service.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Sachith Harshamal
 * @created 2023-08-15
 */
@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailService userDetailsService;
    private final JwtTokenService jwtTokenService;
    private final UserService userService;

    public AuthenticationServiceImpl(AuthenticationManager authenticationManager, CustomUserDetailService userDetailsService, JwtTokenService jwtTokenService, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtTokenService = jwtTokenService;
        this.userService = userService;
    }

    @Override
    public CommonResponse authenticate(AuthenticationRequestDao authenticationRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
            );
        }
        catch (BadCredentialsException badCredentialsException) {
            return CommonResponse.builder()
                    .isOk(Boolean.FALSE)
                    .responseCode(404)
                    .responseMessage("Incorrect username or password")
                    .responseObject(null)
                    .build();
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String jwt = jwtTokenService.generateToken(userDetails);
        final Optional<User> user = userService.findUserByUsername(userDetails.getUsername());

        return new CommonResponse(
                Response.SUCCESS,
                AuthenticationResponseDao.builder()
                        .jwt(jwt)
                        .user(user.map(this::createUserResponse).orElse(null))
                        .build()
        );
    }

    private UserDao createUserResponse(User user) {
        return UserDao.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .username(user.getUsername())
                .role(user.getRole())
                .build();
    }
}
