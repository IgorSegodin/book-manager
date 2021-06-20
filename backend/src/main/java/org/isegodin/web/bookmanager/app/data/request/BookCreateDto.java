package org.isegodin.web.bookmanager.app.data.request;

import lombok.Builder;
import lombok.Data;

/**
 * @author isegodin
 */
@Data
@Builder
public class BookCreateDto {

    private String name;
    private Long authorId;
}
