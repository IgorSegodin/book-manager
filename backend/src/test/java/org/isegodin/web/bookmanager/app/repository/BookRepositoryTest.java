package org.isegodin.web.bookmanager.app.repository;

import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.assertj.core.api.Assertions;
import org.isegodin.web.bookmanager.app.data.Book;
import org.isegodin.web.bookmanager.app.data.request.BookCreateDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author isegodin
 */
@SpringBootTest
@AutoConfigureEmbeddedDatabase(provider = AutoConfigureEmbeddedDatabase.DatabaseProvider.ZONKY)
class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Test
    void testCreate() {
        long bookId = bookRepository.createBook(
                BookCreateDto.builder()
                        .name("Book 1")
                        .build()
        );

        Book book = bookRepository.getBook(bookId);

        Assertions.assertThat(book)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(
                        Book.builder()
                                .name("Book 1")
                                .build()
                );
    }

}