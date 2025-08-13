package com.darknash.blog.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppResponse <T> {
    private int code;
    private String msg;
    private T data;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<ErrorResponse> errors;
}
