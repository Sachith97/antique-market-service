package com.sac.antiquemarketservice.dao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * @author Sachith Harshamal
 * @created 2023-08-15
 */
@Data
@Builder
@AllArgsConstructor
public class UserDao {

    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private String role;
}
