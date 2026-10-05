package com.roslate.littlebabel.repository;

import com.roslate.littlebabel.domain.Author;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Saves, finds and deletes {@link Author} records in the database.
 */
public interface AuthorRepository extends JpaRepository<Author, Long> {

    /**
     * Finds an existing author by exact name, so one can be reused instead of
     * creating a duplicate. Two different people can share a name, so if
     * several authors match, one of them is returned rather than failing.
     *
     * @param name the author's full name
     * @return a matching author, or empty if there is none
     */
    Optional<Author> findFirstByName(String name);
}
