package com.sac.antiquemarketservice.service;

import com.sac.antiquemarketservice.dao.AuthenticationRequestDao;
import com.sac.antiquemarketservice.exception.CommonResponse;

/**
 * @author Sachith Harshamal
 * @created 2023-08-15
 */
public interface AuthenticationService {

    CommonResponse authenticate(AuthenticationRequestDao authenticationRequest);
}
