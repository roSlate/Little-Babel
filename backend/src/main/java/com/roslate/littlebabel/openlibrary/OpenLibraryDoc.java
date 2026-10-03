package com.roslate.littlebabel.openlibrary;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * One matching work from Open Library's search API, as returned by
 * {@code /search.json}.
 * <p>
 * A "work" is Open Library's term for a book independent of any specific
 * edition or printing. Field names mirror Open Library's JSON where they
 * already match; snake_case fields are mapped explicitly with
 * {@link JsonProperty}.
 *
 * @param key              Open Library work key, e.g. {@code "/works/OL59863W"}
 * @param title            the work's title
 * @param authorNames      names of the author(s), or {@code null} if Open Library has none
 * @param firstPublishYear year the work was first published, or {@code null} if unknown
 * @param coverId          cover image id, or {@code null} if there is no cover; the image URL is
 *                         {@code https://covers.openlibrary.org/b/id/{coverId}-{S|M|L}.jpg}
 * @param pageCountMedian  median page count across the work's editions, or {@code null} if unknown
 */
public record OpenLibraryDoc(
        String key,
        String title,
        @JsonProperty("author_name") List<String> authorNames,
        @JsonProperty("first_publish_year") Integer firstPublishYear,
        @JsonProperty("cover_i") Integer coverId,
        @JsonProperty("number_of_pages_median") Integer pageCountMedian
) {
}
