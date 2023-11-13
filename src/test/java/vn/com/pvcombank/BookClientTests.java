package vn.com.pvcombank;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import vn.com.pvcombank.book.Book;
import vn.com.pvcombank.book.BookClient;

import java.io.IOException;

@TestMethodOrder(MethodOrderer.Random.class)
class BookClientTests {

    private MockWebServer mockWebServer;

    private BookClient bookClient;

    @BeforeEach
    public void setUp() throws IOException {

        this.mockWebServer = new MockWebServer();
        this.mockWebServer.start();
        WebClient webClient = WebClient.builder()
                .baseUrl(mockWebServer.url("/").uri().toString()).build();
        this.bookClient = new BookClient(webClient);
    }

    @Test
    void whenBookExistsThenReturnBook() {

        String bookIsbn = "1234567890";
        var mockResponse = new MockResponse()
                .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody("""
							{
								"isbn": %s,
								"title": "Title",
								"author": "Author",
								"price": 9.90,
								"publisher": "PVcomBank"
							}
						""".formatted(bookIsbn));

        mockWebServer.enqueue(mockResponse);

        Mono<Book> book = bookClient.getBookByIsbn(bookIsbn);
        StepVerifier.create(book)
                .expectNextMatches(b -> b.isbn().equals(bookIsbn))
                .verifyComplete();
    }

    @AfterEach
    void clean() throws IOException {
        this.mockWebServer.shutdown();
    }
 }
