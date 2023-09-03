package com.preduh.fakebank.dtos;

import jakarta.validation.constraints.NotBlank;

public record UserLoginRequestRecordDto(@NotBlank(message = "Missing field: username") String username,
                                        @NotBlank(message = "Missing field: password") String password) {
}
