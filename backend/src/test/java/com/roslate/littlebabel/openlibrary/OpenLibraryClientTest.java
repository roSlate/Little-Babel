package com.roslate.littlebabel.openlibrary;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.queryParam;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

/**
 * Tests {@link OpenLibraryClient} against a fake server, so no real request
 * ever leaves the machine.
 */
class OpenLibraryClientTest {

    private static final String BASE_URL = "https://openlibrary.org";
    private static final String USER_AGENT = "little-babel-test/0.1";

    private MockRestServiceServer server;
    private OpenLibraryClient client;

    @BeforeEach
    void setUp() {
        RestClient.Builder builder = RestClient.builder();
        server = MockRestServiceServer.bindTo(builder).build();
        client = new OpenLibraryClient(builder, BASE_URL, USER_AGENT);
    }

    @Test
    void sendsExpectedRequestAndReturnsMappedResults() {
        server.expect(method(HttpMethod.GET))
                .andExpect(queryParam("q", "dispossessed"))
                .andExpect(queryParam("fields",
                        "key,title,author_name,first_publish_year,cover_i,number_of_pages_median"))
                .andExpect(header(HttpHeaders.USER_AGENT, USER_AGENT))
                .andRespond(withSuccess("""
                        {
                          "numFound": 1,
                          "docs": [
                            {
                              "key": "/works/OL59863W",
                              "title": "The Dispossessed",
                              "author_name": ["Ursula K. Le Guin"]
                            }
                          ]
                        }
                        """, MediaType.APPLICATION_JSON));

        List<OpenLibraryDoc> results = client.search("dispossessed");

        server.verify();
        assertThat(results).hasSize(1);
        assertThat(results.get(0).title()).isEqualTo("The Dispossessed");
        assertThat(results.get(0).authorNames()).containsExactly("Ursula K. Le Guin");
    }

    @Test
    void returnsEmptyListWhenNothingMatches() {
        server.expect(method(HttpMethod.GET))
                .andRespond(withSuccess("""
                        { "numFound": 0, "docs": [] }
                        """, MediaType.APPLICATION_JSON));

        List<OpenLibraryDoc> results = client.search("zzzzzz");

        assertThat(results).isEmpty();
    }

    @Test
    void returnsEmptyListWhenResponseHasNoBody() {
        server.expect(method(HttpMethod.GET))
                .andRespond(withSuccess());

        List<OpenLibraryDoc> results = client.search("anything");

        assertThat(results).isEmpty();
    }

    // The fake server reports the query exactly as it travels over the wire,
    // so special characters show up in their encoded form (%20 is a space,
    // %26 is an ampersand).
    @Test
    void searchTextWithSpacesAndAmpersandIsEncoded() {
        server.expect(queryParam("q", "the%20dispossessed%20%26%20more"))
                .andRespond(withSuccess("""
                        { "numFound": 0, "docs": [] }
                        """, MediaType.APPLICATION_JSON));

        client.search("the dispossessed & more");

        server.verify();
    }

    @Test
    void searchTextWithPlusSignIsEncoded() {
        server.expect(queryParam("q", "c%2B%2B"))
                .andRespond(withSuccess("""
                        { "numFound": 0, "docs": [] }
                        """, MediaType.APPLICATION_JSON));

        client.search("c++");

        server.verify();
    }
}