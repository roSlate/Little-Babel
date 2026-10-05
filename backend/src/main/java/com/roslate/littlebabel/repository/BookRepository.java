package com.roslate.littlebabel.repository;

import com.roslate.littlebabel.domain.Book;
import com.roslate.littlebabel.domain.ReadingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Saves, finds and deletes {@link Book} records in the database.
 */
public interface BookRepository extends JpaRepository<Book, Long> {

    /**
     * Finds the book on the shelf that came from the given Open Library work.
     * Used to avoid adding the same book twice.
     *
     * @param externalKey Open Library work key, e.g. {@code "/works/OL59863W"}
     * @return the matching book, or empty if it isn't on the shelf
     */
    Optional<Book> findByExternalKey(String externalKey);

    /**
     * Finds all books with the given reading status.
     *
     * @param status the status to filter by
     * @return the matching books, empty if there are none
     */
    List<Book> findByStatus(ReadingStatus status);
}
