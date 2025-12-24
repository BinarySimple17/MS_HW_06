package ru.binarysimple.auth.dto;

import jakarta.validation.constraints.Size;
import lombok.Value;
import ru.binarysimple.auth.model.User;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * DTO for {@link User}
 */
@Value
public class UserDto {
    @Size(max = 256)
    String username;
    boolean enabled;
    Set<String> roles;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}