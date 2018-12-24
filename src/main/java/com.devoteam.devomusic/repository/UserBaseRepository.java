package com.devoteam.devomusic.repository;

import com.devoteam.devomusic.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface UserBaseRepository<T extends User> extends CrudRepository<T, Long> {

    /**
     * Returns an Optional which may contain the user, if it's already in the database
     * @param email: the email of the user.
     * @return Returns the user with given email.
     */
    @Query("select u from #{#entityName} as u where u.email = ?1 ")
    Optional<T> findByEmail(String email);

    /**
     * Returns a list of Users, given the list of userIds.
     * @param userIds: A list of userIds to retrieve.
     * @return Returns a list of Users.
     */
    List<T> findByIdIn(List<Long> userIds);

    /**
     * Indicates whether a User with the given email exists.
     * @param email: the email for the user to check
     * @return Returns a boolean.
     */
    Boolean existsByEmail(String email);

}