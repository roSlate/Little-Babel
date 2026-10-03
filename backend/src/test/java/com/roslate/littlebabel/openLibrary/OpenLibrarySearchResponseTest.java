// backend/src/test/java/com/roslate/littlebabel/openLibrary/OpenLibrarySearchResponseTest.java
package com.roslate.littlebabel.openLibrary;

import org.junit.jupiter.api.Test;
import tools.jackson.databind.json.JsonMapper;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Checks that JSON shaped like Open Library's search response is read into an
 * {@link OpenLibrarySearchResponse} correctly. Uses canned JSON, so no network
 * call is made.
 */
class OpenLibrarySearchResponseTest {

    private final JsonMapper jsonMapper = JsonMapper.builder().build();

    @Test
    void holdsTotalCountAndListOfDocs() {
        String json = """
                {
                  "numFound": 603,
                  "docs": [
                    { "key": "/works/OL1W", "title": "First" },
                    { "key": "/works/OL2W", "title": "Second" }
                  ]
                }
                """;

        OpenLibrarySearchResponse response = jsonMapper.readValue(json, OpenLibrarySearchResponse.class);

        assertThat(response.numFound()).isEqualTo(603);
        assertThat(response.docs())
                .extracting(OpenLibraryDoc::title)
                .containsExactly("First", "Second");
    }

    @Test
    void fieldsNotDeclaredInTheRecordsAreIgnored() {
        String json = """
                {
                  "numFound": 1,
                  "start": 0,
                  "q": "dune",
                  "docs": [
                    { "key": "/works/OL1W", "title": "Dune", "ebook_access": "borrowable" }
                  ]
                }
                """;

        OpenLibrarySearchResponse response = jsonMapper.readValue(json, OpenLibrarySearchResponse.class);

        assertThat(response.docs()).hasSize(1);
        assertThat(response.docs().get(0).title()).isEqualTo("Dune");
    }
}