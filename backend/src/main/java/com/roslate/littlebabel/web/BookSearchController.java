package com.roslate.littlebabel.web;

import com.roslate.littlebabel.openLibrary.OpenLibraryClient;
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

    @GetMapping("/api/books/search")
    public List<BookSearchResult> search(@RequestParam String q) {
        return openLibraryClient.search(q).stream()
                .map(BookSearchResult::from)
                .toList();
    }
}