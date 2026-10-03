package com.roslate.littlebabel.openlibrary;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

/**
 * Talks to Open Library's search API
 * (see <a href="https://openlibrary.org/dev/docs/api/search">docs</a>).
 * <p>
 * No API key is needed. Open Library asks callers to identify themselves with
 * a descriptive User-Agent header, which is set once here and sent with every request.
 */
@Component
public class OpenLibraryClient {

    private static final String SEARCH_FIELDS =
            "key,title,author_name,first_publish_year,cover_i,number_of_pages_median";

    private final RestClient restClient;

    public OpenLibraryClient(
            RestClient.Builder restClientBuilder,
            @Value("${open-library.base-url}") String baseUrl,
            @Value("${open-library.user-agent}") String userAgent
    ) {

        this.restClient = restClientBuilder
                .baseUrl(baseUrl)
                .defaultHeader("User-Agent", userAgent)
                .build();
    }

    /**
     * Searches Open Library's catalog with a free-text query (title, author, etc.).
     *
     * @param query free-text search query, passed to Open Library as-is
     * @return matching works, most relevant first; empty if none were found
     */
    public List<OpenLibraryDoc> search(String query) {
        OpenLibrarySearchResponse response = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/search.json")
                        .queryParam("q", "{q}")
                        .queryParam("fields", SEARCH_FIELDS)
                        .build(query))
                .retrieve()
                .body(OpenLibrarySearchResponse.class);

        return response != null ? response.docs() : List.of();
    }
}
