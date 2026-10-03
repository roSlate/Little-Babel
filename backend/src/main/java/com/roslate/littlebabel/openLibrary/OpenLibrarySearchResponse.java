package com.roslate.littlebabel.openLibrary;

import java.util.List;

/**
 * Top-level shape of a response from Open Library's {@code /search.json}.
 * <p>
 * Only the fields this app uses are declared; anything else in the response
 * is ignored when the JSON is read.
 *
 * @param numFound total number of matches Open Library has for the query,
 *                 which can be larger than the size of {@code docs}
 * @param docs     the page of matching works returned in this response
 */
public record OpenLibrarySearchResponse(
        int numFound,
        List<OpenLibraryDoc> docs
) {
}
