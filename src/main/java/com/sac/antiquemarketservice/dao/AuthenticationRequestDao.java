package com.sac.antiquemarketservice.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * @author Sachith Harshamal
 * @created 2023-08-15
 */
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class AuthenticationRequestDao {

    private String username;
    private String password;
}
