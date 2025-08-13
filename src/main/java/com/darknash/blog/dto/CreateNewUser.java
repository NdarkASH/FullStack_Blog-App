package com.darknash.blog.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateNewUser {
    @Size(min = 2, max = 50, message = "firstname must have between {min} and {max}")
    private String firstName;
    @Size(min = 2, max = 50, message = "firstname must have between {min} and {max}")
    private String lastName;
    @Email(message = "Please input the valid email")
    @Valid
    private String email;

    @NotNull
    private String password;
}
