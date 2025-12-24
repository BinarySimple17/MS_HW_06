package ru.binarysimple.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * DTO for {@link ru.binarysimple.auth.model.User}
 */
@Value
public class UserDtoAll {
    @Size(max = 256)
    String username;
    @NotBlank
    String password;
    boolean enabled;
    Set<String> roles;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}