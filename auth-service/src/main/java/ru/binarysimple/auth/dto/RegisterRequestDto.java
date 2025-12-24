package ru.binarysimple.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Value;
import ru.binarysimple.auth.model.User;

/**
 * DTO for {@link User}
 */
@Value
public class RegisterRequestDto {
    @NotBlank
    private String username;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String phone;

    @NotBlank
    private String password;

    private String firstName;

    private String lastName;
}
