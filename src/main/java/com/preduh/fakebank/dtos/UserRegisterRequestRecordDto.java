package com.preduh.fakebank.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRegisterRequestRecordDto(@NotBlank(message = "Missing field: username") String username,
                                           @NotBlank(message = "Missing field: email") @Email(message = "Invalid email") String email,
                                           @NotBlank(message = "Missing field: password") @Size(min = 8, message = "Invalid password: password must be at least 8 characters long") String password) {
}
