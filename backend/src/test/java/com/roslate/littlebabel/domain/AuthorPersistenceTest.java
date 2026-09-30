// backend/src/test/java/com/roslate/littlebabel/domain/AuthorPersistenceTest.java
package com.roslate.littlebabel.domain;

import org.junit.jupiter.api.Test;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Verifies Author actually round-trips through JPA against a real (in-memory)
 * database, in particular that the id is database-generated on insert rather
 * than defaulting to 0 — see the field-level JavaDoc on {@link Author#getId()}.
 */
@DataJpaTest
class AuthorPersistenceTest {

    @Autowired
    private EntityManager entityManager;

    @Test
    void persistingAssignsGeneratedId() {
        Author author = new Author("Octavia E. Butler", "United States", 1947);
        assertThat(author.getId()).isNull();

        entityManager.persist(author);
        entityManager.flush();

        assertThat(author.getId()).isNotNull();
    }

    @Test
    void findByIdReturnsPersistedValues() {
        Author author = new Author("Octavia E. Butler", "United States", 1947);
        entityManager.persist(author);
        entityManager.flush();
        Long id = author.getId();
        entityManager.clear(); // detach, force a real reload from the DB

        Author reloaded = entityManager.find(Author.class, id);

        assertThat(reloaded).isNotNull();
        assertThat(reloaded.getName()).isEqualTo("Octavia E. Butler");
        assertThat(reloaded.getCountry()).isEqualTo("United States");
        assertThat(reloaded.getBirthYear()).isEqualTo(1947);
    }
}