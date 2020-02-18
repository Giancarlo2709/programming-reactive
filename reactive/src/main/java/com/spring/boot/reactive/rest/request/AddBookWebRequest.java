package com.spring.boot.reactive.rest.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AddBookWebRequest {
    private String title;
    private Integer authorId;
}
