package com.roslate.littlebabel.web;

import com.roslate.littlebabel.openlibrary.OpenLibraryDoc;

import java.util.List;

/**
 * One book found by a search, in the shape this application's API returns it.
 * <p>
 * Deliberately separate from the external service's own format so that
 * clients of this API don't depend on how Open Library names its fields.
 * Field names match the corresponding fields on {@code Book}.
 *
 * @param externalKey   identifier of the work at the source (Open Library), e.g. {@code "/works/OL59863W"}
 * @param title         the book's title
 * @param authors       author names, empty if the source has none
 * @param publishedYear year the book was first published, or {@code null} if unknown
 * @param coverId       cover image id, or {@code null} if there is no cover
 * @param pageCount     typical page count, or {@code null} if unknown
 */
public record BookSearchResult(
        String externalKey,
        String title,
        List<String> authors,
        Integer publishedYear,
        Integer coverId,
        Integer pageCount
) {

    /**
     * Converts one Open Library search result into this API's own shape.
     * A missing author list becomes an empty list.
     *
     * @param doc a result as read from Open Library
     * @return the same book, in this application's format
     */
    public static BookSearchResult from(OpenLibraryDoc doc) {
        return new BookSearchResult(
                doc.key(),
                doc.title(),
                doc.authorNames() != null ? doc.authorNames() : List.of(),
                doc.firstPublishYear(),
                doc.coverId(),
                doc.pageCountMedian()
        );
    }
}