package com.roslate.littlebabel.domain;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Verifies Book round-trips through JPA, including the {@code @ManyToMany}
 * relationship to {@link Author} — the join table is only ever exercised at
 * runtime, so this is the one place we actually prove it's wired correctly.
 */
@DataJpaTest
class BookPersistenceTest {

    @Autowired
    private EntityManager entityManager;

    @Test
    void persistingAssignsGeneratedId() {
        Book book = new Book("The Dispossessed");
        assertThat(book.getId()).isNull();

        entityManager.persist(book);
        entityManager.flush();

        assertThat(book.getId()).isNotNull();
    }

    @Test
    void persistsWithMultipleAuthors() {
        Author leGuin = new Author("Ursula K. Le Guin", "United States", 1929);
        Author gaiman = new Author("Neil Gaiman", "United Kingdom", 1960);
        entityManager.persist(leGuin);
        entityManager.persist(gaiman);

        Book book = new Book("Good Omens");
        book.setAuthors(Set.of(leGuin, gaiman));
        entityManager.persist(book);
        entityManager.flush();
        entityManager.clear(); // detach, force a real reload from the DB

        Book reloaded = entityManager.find(Book.class, book.getId());

        assertThat(reloaded.getAuthors())
                .extracting(Author::getName)
                .containsExactlyInAnyOrder("Ursula K. Le Guin", "Neil Gaiman");
    }

    @Test
    void findByIdReturnsPersistedValues() {
        Book book = new Book("The Dispossessed");
        book.setExternalKey("/works/OL59863W");
        book.setCoverId(6979680);
        book.setPublishedYear(1974);
        book.setPageCount(352);
        book.setStatus(ReadingStatus.FINISHED);
        book.setRating(4.5);
        entityManager.persist(book);
        entityManager.flush();
        Long id = book.getId();
        entityManager.clear();

        Book reloaded = entityManager.find(Book.class, id);

        assertThat(reloaded).isNotNull();
        assertThat(reloaded.getTitle()).isEqualTo("The Dispossessed");
        assertThat(reloaded.getExternalKey()).isEqualTo("/works/OL59863W");
        assertThat(reloaded.getCoverId()).isEqualTo(6979680);
        assertThat(reloaded.getStatus()).isEqualTo(ReadingStatus.FINISHED);
        assertThat(reloaded.getRating()).isEqualTo(4.5);
    }
}