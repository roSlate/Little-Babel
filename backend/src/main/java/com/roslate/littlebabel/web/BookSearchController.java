package com.roslate.littlebabel.web;

import com.roslate.littlebabel.openLibrary.OpenLibraryClient;
import com.roslate.littlebabel.openLibrary.OpenLibraryDoc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * HTTP endpoint for searching books. Passes the query on to Open Library
 * and returns what it finds.
 */
@RestController
public class BookSearchController {

    private final OpenLibraryClient openLibraryClient;

    public BookSearchController(OpenLibraryClient openLibraryClient) {
        this.openLibraryClient = openLibraryClient;
    }

    /**
     * Searches for books matching a free-text query.
     *
     * @param q the search text, e.g. a title or an author's name
     * @return matching works, most relevant first
     */
    @GetMapping("/api/books/search")
    public List<OpenLibraryDoc> search(@RequestParam String q) {
        return openLibraryClient.search(q);
    }
}
