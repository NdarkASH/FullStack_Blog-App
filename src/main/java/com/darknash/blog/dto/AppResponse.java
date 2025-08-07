package com.darknash.blog.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppResponse {
    private int code;
    private String msg;
    private List<ErrorResponse> errors;
}
