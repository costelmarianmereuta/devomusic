package com.devoteam.devomusic.repository;

import com.devoteam.devomusic.model.GoogleUser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@DataJpaTest
public class GoogleUserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private GoogleUserRepository googleUserRepository;

    @Test
    public void whenFindByEmail_thenReturnGoogleUser() {
        GoogleUser googleUser = new GoogleUser("test@test.com", "test test", "www.example.com");
        entityManager.persist(googleUser);
        entityManager.flush();

        GoogleUser found = null;

        if (googleUserRepository.findByEmail(googleUser.getEmail()).isPresent()) {
            found = googleUserRepository.findByEmail(googleUser.getEmail()).get();
        }

        assertThat(found.getEmail()).isEqualTo(googleUser.getEmail());
    }

    @Test
    public void whenFindByEmail_thenReturnNullable() {
        assertThat(googleUserRepository.existsByEmail("blabla@blabloe.com")).isFalse();
        GoogleUser found = null;

        if (googleUserRepository.findByEmail("test@test.com").isPresent()) {
            found = googleUserRepository.findByEmail("test@test.com").get();
        }

        assertThat(found).isNull();
    }


}
