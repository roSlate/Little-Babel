package com.roslate.littlebabel.web;

import com.roslate.littlebabel.openLibrary.OpenLibraryClient;
import com.roslate.littlebabel.openLibrary.OpenLibraryDoc;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Tests {@link BookSearchController} through a simulated web request, with
 * {@link OpenLibraryClient} replaced by a stand-in so nothing talks to Open Library.
 */
@WebMvcTest(BookSearchController.class)
class BookSearchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OpenLibraryClient openLibraryClient;

    @Test
    void searchReturnsWhatTheClientFoundAsJson() throws Exception {
        given(openLibraryClient.search("dune")).willReturn(List.of(
                new OpenLibraryDoc("/works/OL893415W", "Dune", List.of("Frank Herbert"),
                        1965, 123, 412)
        ));

        mockMvc.perform(get("/api/books/search").param("q", "dune"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].externalKey").value("/works/OL893415W"))
                .andExpect(jsonPath("$[0].title").value("Dune"))
                .andExpect(jsonPath("$[0].authors[0]").value("Frank Herbert"))
                .andExpect(jsonPath("$[0].publishedYear").value(1965))
                .andExpect(jsonPath("$[0].coverId").value(123))
                .andExpect(jsonPath("$[0].pageCount").value(412))
                .andExpect(jsonPath("$[0].author_name").doesNotExist());
    }

    @Test
    void searchReturnsEmptyJsonListWhenNothingFound() throws Exception {
        given(openLibraryClient.search("zzzzzz")).willReturn(List.of());

        mockMvc.perform(get("/api/books/search").param("q", "zzzzzz"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void searchWithoutQueryParameterIsRejected() throws Exception {
        mockMvc.perform(get("/api/books/search"))
                .andExpect(status().isBadRequest());
    }
}