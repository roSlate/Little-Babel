package com.roslate.littlebabel.repository;

import com.roslate.littlebabel.domain.Author;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class AuthorRepositoryTest {

    @Autowired
    private AuthorRepository authorRepository;

    @Test
    void findFirstByNameReturnsTheMatchingAuthor() {
        authorRepository.save(new Author("Ursula K. Le Guin", "United States", 1929));
        authorRepository.save(new Author("Octavia E. Butler", "United States", 1947));

        Optional<Author> found = authorRepository.findFirstByName("Octavia E. Butler");

        assertThat(found).isPresent();
        assertThat(found.get().getBirthYear()).isEqualTo(1947);
    }

    @Test
    void findFirstByNameReturnsEmptyWhenNoAuthorHasThatName() {
        authorRepository.save(new Author("Ursula K. Le Guin", "United States", 1929));

        Optional<Author> found = authorRepository.findFirstByName("Nobody");

        assertThat(found).isEmpty();
    }

    @Test
    void findFirstByNameDoesNotFailWhenTwoAuthorsShareAName() {
        authorRepository.save(new Author("John Smith", "United Kingdom", 1950));
        authorRepository.save(new Author("John Smith", "Canada", 1980));

        Optional<Author> found = authorRepository.findFirstByName("John Smith");

        assertThat(found).isPresent();
    }
}