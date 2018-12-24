package com.devoteam.devomusic.repository;

import com.devoteam.devomusic.model.PasswordUser;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * A repository for managing password users. Inherits properties from {@UserBaseRepository}.
 */
@Transactional
public interface PasswordUserRepository extends UserBaseRepository<PasswordUser> {

    /**
     * Indicates whether a User with the given username exists.
     * @param username: The username of the user.
     * @return Returns a boolean.
     */
    Boolean existsByUsername(String username);

    Optional<PasswordUser> findByUsernameOrEmail(String username, String email);

}