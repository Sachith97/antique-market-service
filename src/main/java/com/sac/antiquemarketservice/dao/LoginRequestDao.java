package com.sac.antiquemarketservice.dao;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Sachith Harshamal
 * @created 2023-08-15
 */
@Data
@AllArgsConstructor
public class LoginRequestDao {

    private String username;
    private String password;
}
