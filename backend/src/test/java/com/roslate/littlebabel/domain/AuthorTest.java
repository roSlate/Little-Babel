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

class AuthorTest {

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
    void constructorSetsAllFields() {
        Author author = new Author("Ursula K. Le Guin", "United States", 1929);

        assertThat(author.getName()).isEqualTo("Ursula K. Le Guin");
        assertThat(author.getCountry()).isEqualTo("United States");
        assertThat(author.getBirthYear()).isEqualTo(1929);
    }

    @Test
    void idIsNullBeforePersistence() {
        Author author = new Author("Ursula K. Le Guin", "United States", 1929);

        assertThat(author.getId()).isNull();
    }

    @Test
    void countryAndBirthYearAreOptional() {
        Author author = new Author("Anonymous", null, null);

        Set<ConstraintViolation<Author>> violations = validator.validate(author);

        assertThat(violations).isEmpty();
    }

    @Test
    void blankNameFailsValidation() {
        Author author = new Author("   ", "France", 1900);

        Set<ConstraintViolation<Author>> violations = validator.validate(author);

        assertThat(violations)
                .extracting(v -> v.getPropertyPath().toString())
                .containsExactly("name");
    }

    @Test
    void nullNameFailsValidation() {
        Author author = new Author(null, "France", 1900);

        Set<ConstraintViolation<Author>> violations = validator.validate(author);

        assertThat(violations).isNotEmpty();
    }

    @Test
    void settersUpdateFields() {
        Author author = new Author("Original Name", "Original Country", 1900);

        author.setName("Updated Name");
        author.setCountry("Updated Country");
        author.setBirthYear(2000);

        assertThat(author.getName()).isEqualTo("Updated Name");
        assertThat(author.getCountry()).isEqualTo("Updated Country");
        assertThat(author.getBirthYear()).isEqualTo(2000);
    }
}