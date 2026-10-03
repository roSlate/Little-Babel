package com.roslate.littlebabel.openLibrary;

import org.junit.jupiter.api.Test;
import tools.jackson.databind.json.JsonMapper;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Checks that JSON shaped like one result from Open Library's search is read
 * into an {@link OpenLibraryDoc} correctly. Uses canned JSON, so no network
 * call is made.
 */
class OpenLibraryDocTest {

    private final JsonMapper jsonMapper = JsonMapper.builder().build();

    @Test
    void mapsSnakeCaseJsonFieldsToRecordFields() {
        String json = """
                {
                  "key": "/works/OL59863W",
                  "title": "The Dispossessed",
                  "author_name": ["Ursula K. Le Guin"],
                  "first_publish_year": 1974,
                  "cover_i": 6979680,
                  "number_of_pages_median": 352
                }
                """;

        OpenLibraryDoc doc = jsonMapper.readValue(json, OpenLibraryDoc.class);

        assertThat(doc.key()).isEqualTo("/works/OL59863W");
        assertThat(doc.title()).isEqualTo("The Dispossessed");
        assertThat(doc.authorNames()).containsExactly("Ursula K. Le Guin");
        assertThat(doc.firstPublishYear()).isEqualTo(1974);
        assertThat(doc.coverId()).isEqualTo(6979680);
        assertThat(doc.pageCountMedian()).isEqualTo(352);
    }

    @Test
    void fieldsMissingFromJsonBecomeNull() {
        String json = """
                {
                  "key": "/works/OL1W",
                  "title": "Obscure Book"
                }
                """;

        OpenLibraryDoc doc = jsonMapper.readValue(json, OpenLibraryDoc.class);

        assertThat(doc.title()).isEqualTo("Obscure Book");
        assertThat(doc.authorNames()).isNull();
        assertThat(doc.firstPublishYear()).isNull();
        assertThat(doc.coverId()).isNull();
        assertThat(doc.pageCountMedian()).isNull();
    }
}