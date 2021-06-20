package org.isegodin.web.bookmanager.app.repository;

import lombok.RequiredArgsConstructor;
import org.isegodin.web.bookmanager.app.data.Book;
import org.isegodin.web.bookmanager.app.data.request.BookCreateDto;
import org.isegodin.web.bookmanager.transaction.TransactionService;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * @author isegodin
 */
@RequiredArgsConstructor
@Repository
public class BookRepository {

    private final DatabaseClient databaseClient;
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final TransactionService transactionService;

    public long createBook(BookCreateDto createDto) {
        return transactionService.writeTransaction(() -> {
            GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(
                    "INSERT INTO book (name, id_author) VALUES (:name, :authorId)",
                    new BeanPropertySqlParameterSource(createDto),
                    keyHolder,
                    new String[]{"id"}
            );
            return keyHolder.getKey().longValue();
        });
    }

    public Book getBook(long bookId) {
        return transactionService.readOnlyTransaction(() -> jdbcTemplate.queryForObject(
                """
                        SELECT
                             b.id,
                             b.name,
                             a.id AS "author.id",
                             a.name AS "author.name"
                        FROM book b
                        LEFT JOIN author a ON a.id = b.id_author
                        WHERE b.id = :bookId
                        """,
                Map.of("bookId", bookId),
                new BeanPropertyRowMapper<>(Book.class)
        ));
    }

}
