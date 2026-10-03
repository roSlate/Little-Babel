package com.roslate.littlebabel.web;

import com.roslate.littlebabel.openlibrary.OpenLibraryDoc;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class BookSearchResultTest {

    @Test
    void fromCopiesEachFieldUnderTheApiName() {
        OpenLibraryDoc doc = new OpenLibraryDoc(
                "/works/OL59863W", "The Dispossessed", List.of("Ursula K. Le Guin"), 1974,
                6979680, 352);

        BookSearchResult result = BookSearchResult.from(doc);

        assertThat(result.externalKey()).isEqualTo("/works/OL59863W");
        assertThat(result.title()).isEqualTo("The Dispossessed");
        assertThat(result.authors()).containsExactly("Ursula K. Le Guin");
        assertThat(result.publishedYear()).isEqualTo(1974);
        assertThat(result.coverId()).isEqualTo(6979680);
        assertThat(result.pageCount()).isEqualTo(352);
    }

    @Test
    void fromTurnsMissingAuthorsIntoEmptyList() {
        OpenLibraryDoc doc = new OpenLibraryDoc("/works/OL1W", "Anonymous Work", null,
                null, null, null);

        BookSearchResult result = BookSearchResult.from(doc);

        assertThat(result.authors()).isNotNull().isEmpty();
    }

    @Test
    void fromKeepsMissingYearCoverAndPageCountAsNull() {
        OpenLibraryDoc doc = new OpenLibraryDoc("/works/OL1W", "Obscure Book", List.of("Someone"),
                null, null, null);

        BookSearchResult result = BookSearchResult.from(doc);

        assertThat(result.publishedYear()).isNull();
        assertThat(result.coverId()).isNull();
        assertThat(result.pageCount()).isNull();
    }
}