package ru.binarysimple.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Value;
import ru.binarysimple.auth.model.User;

/**
 * DTO for {@link User}
 */
@Value
public class CreateUserExternalDto {
    @NotNull
    @Size(max = 256)
    @NotEmpty
    String username;
    String firstName;
    String lastName;
    @NotNull
    @Email
    @NotEmpty
    String email;
    String phone;
}