package com.roslate.littlebabel.repository;

import com.roslate.littlebabel.domain.Book;
import com.roslate.littlebabel.domain.ReadingStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

import org.springframework.dao.DataIntegrityViolationException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Test
    void findByExternalKeyReturnsTheMatchingBook() {
        saveBook("The Dispossessed", "/works/OL59863W", ReadingStatus.FINISHED);
        saveBook("The Lathe of Heaven", "/works/OL59858W", ReadingStatus.FINISHED);

        Optional<Book> found = bookRepository.findByExternalKey("/works/OL59863W");

        assertThat(found).isPresent();
        assertThat(found.get().getTitle()).isEqualTo("The Dispossessed");
    }

    @Test
    void findByExternalKeyReturnsEmptyWhenBookIsNotOnTheShelf() {
        saveBook("The Dispossessed", "/works/OL59863W", ReadingStatus.FINISHED);

        Optional<Book> found = bookRepository.findByExternalKey("/works/OL1W");

        assertThat(found).isEmpty();
    }

    @Test
    void findByStatusReturnsOnlyBooksWithThatStatus() {
        saveBook("Reading A", "/works/OL1W", ReadingStatus.READING);
        saveBook("Reading B", "/works/OL2W", ReadingStatus.READING);
        saveBook("Finished", "/works/OL3W", ReadingStatus.FINISHED);
        saveBook("Wishlist", "/works/OL4W", ReadingStatus.WANT_TO_READ);

        List<Book> reading = bookRepository.findByStatus(ReadingStatus.READING);

        assertThat(reading)
                .extracting(Book::getTitle)
                .containsExactlyInAnyOrder("Reading A", "Reading B");
    }

    @Test
    void findByStatusReturnsEmptyListWhenNoBookHasThatStatus() {
        saveBook("Finished", "/works/OL3W", ReadingStatus.FINISHED);

        List<Book> abandoned = bookRepository.findByStatus(ReadingStatus.DID_NOT_FINISH);

        assertThat(abandoned).isEmpty();
    }

    private void saveBook(String title, String externalKey, ReadingStatus status) {
        Book book = new Book(title);
        book.setExternalKey(externalKey);
        book.setStatus(status);
        bookRepository.save(book);
    }

    @Test
    void savingTwoBooksWithTheSameExternalKeyIsRejected() {
        saveBook("The Dispossessed", "/works/OL59863W", ReadingStatus.FINISHED);

        assertThatThrownBy(() -> {
            saveBook("The Dispossessed again", "/works/OL59863W", ReadingStatus.WANT_TO_READ);
            bookRepository.flush();
        }).isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void booksWithoutAnExternalKeyCanCoexist() {
        bookRepository.save(new Book("Handwritten A"));
        bookRepository.save(new Book("Handwritten B"));
        bookRepository.flush();

        assertThat(bookRepository.count()).isEqualTo(2);
    }
}