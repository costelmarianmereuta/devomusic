package com.devoteam.devomusic.repository;

import com.devoteam.devomusic.model.GoogleUser;
import org.springframework.transaction.annotation.Transactional;

/**
 * A class for managing the database of Google users.
 */
@Transactional
public interface GoogleUserRepository extends  UserBaseRepository<GoogleUser> {

}