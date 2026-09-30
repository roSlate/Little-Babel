package com.roslate.littlebabel.domain;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class BookTest {

    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterAll
    static void closeValidator() {
        validatorFactory.close();
    }

    @Test
    void constructorSetsTitleAndDefaultsStatusToWantToRead() {
        Book book = new Book("The Dispossessed");

        assertThat(book.getTitle()).isEqualTo("The Dispossessed");
        assertThat(book.getStatus()).isEqualTo(ReadingStatus.WANT_TO_READ);
    }

    @Test
    void idIsNullBeforePersistence() {
        Book book = new Book("The Dispossessed");

        assertThat(book.getId()).isNull();
    }

    @Test
    void authorsIsEmptySetByDefault() {
        Book book = new Book("The Dispossessed");

        assertThat(book.getAuthors()).isEmpty();
    }

    @Test
    void blankTitleFailsValidation() {
        Book book = new Book("   ");

        Set<ConstraintViolation<Book>> violations = validator.validate(book);

        assertThat(violations)
                .extracting(v -> v.getPropertyPath().toString())
                .containsExactly("title");
    }

    @Test
    void bookWithOnlyTitleIsValid() {
        Book book = new Book("The Dispossessed");

        Set<ConstraintViolation<Book>> violations = validator.validate(book);

        assertThat(violations).isEmpty();
    }

    @Test
    void ratingWithinRangeAndOneDecimalIsValid() {
        Book book = new Book("The Dispossessed");
        book.setRating(3.4);

        Set<ConstraintViolation<Book>> violations = validator.validate(book);

        assertThat(violations).isEmpty();
    }

    @Test
    void ratingBelowOneFailsValidation() {
        Book book = new Book("The Dispossessed");
        book.setRating(0.5);

        Set<ConstraintViolation<Book>> violations = validator.validate(book);

        assertThat(violations)
                .extracting(v -> v.getPropertyPath().toString())
                .containsExactly("rating");
    }

    @Test
    void ratingAboveFiveFailsValidation() {
        Book book = new Book("The Dispossessed");
        book.setRating(5.1);

        Set<ConstraintViolation<Book>> violations = validator.validate(book);

        assertThat(violations)
                .extracting(v -> v.getPropertyPath().toString())
                .containsExactly("rating");
    }

    @Test
    void ratingWithTwoDecimalPlacesFailsValidation() {
        Book book = new Book("The Dispossessed");
        book.setRating(3.45);

        Set<ConstraintViolation<Book>> violations = validator.validate(book);

        assertThat(violations)
                .extracting(v -> v.getPropertyPath().toString())
                .containsExactly("rating");
    }

    @Test
    void settersUpdateFields() {
        Book book = new Book("Original Title");

        book.setTitle("Updated Title");
        book.setStatus(ReadingStatus.READING);
        book.setPageCount(352);
        book.setPublishedYear(1974);

        assertThat(book.getTitle()).isEqualTo("Updated Title");
        assertThat(book.getStatus()).isEqualTo(ReadingStatus.READING);
        assertThat(book.getPageCount()).isEqualTo(352);
        assertThat(book.getPublishedYear()).isEqualTo(1974);
    }
}