package com.sac.antiquemarketservice.controller;

import com.sac.antiquemarketservice.dao.AuthenticationRequestDao;
import com.sac.antiquemarketservice.exception.CommonResponse;
import com.sac.antiquemarketservice.service.AuthenticationService;
import org.springframework.web.bind.annotation.*;

/**
 * @author Sachith Harshamal
 * @created 2023-08-15
 */
@RestController
@CrossOrigin
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping(value = "/login", produces = {"application/json"}, consumes = {"application/json"})
    public CommonResponse createAuthenticationToken(@RequestBody AuthenticationRequestDao authenticationRequest) {
        return authenticationService.authenticate(authenticationRequest);
    }
}
