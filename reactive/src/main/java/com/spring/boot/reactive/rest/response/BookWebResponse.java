package com.spring.boot.reactive.rest.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookWebResponse {

    private Integer id;
    private String title;
    private String authorName;
}
