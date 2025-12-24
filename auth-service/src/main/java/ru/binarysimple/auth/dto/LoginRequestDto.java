package ru.binarysimple.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Value;
import ru.binarysimple.auth.model.User;

/**
 * DTO for {@link User}
 */
@Value
public class LoginRequestDto {
    @NotBlank
    private String username;

    @NotBlank
    private String password;
}
