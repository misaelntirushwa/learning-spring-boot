package com.ntm.learningspringboot.auth;

import java.util.Optional;

public interface ApplicationUserDao {
    Optional<ApplicationUser> selectApplicationUserByUsername(String username);
}
