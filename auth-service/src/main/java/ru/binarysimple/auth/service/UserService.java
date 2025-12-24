package ru.binarysimple.auth.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.binarysimple.auth.dto.LoginRequestDto;
import ru.binarysimple.auth.dto.RegisterRequestDto;
import ru.binarysimple.auth.dto.UserDto;
import ru.binarysimple.auth.filter.UserFilter;
import ru.binarysimple.auth.dto.UserDtoAll;

import java.io.IOException;
import java.util.List;

public interface UserService {
    Page<UserDtoAll> getAll(UserFilter filter, Pageable pageable);

    UserDtoAll getOne(Long id);

    List<UserDtoAll> getMany(List<Long> ids);

    UserDtoAll create(UserDtoAll dto);

    UserDtoAll patch(Long id, JsonNode patchNode) throws IOException;

    List<Long> patchMany(List<Long> ids, JsonNode patchNode) throws IOException;

    UserDtoAll delete(Long id);

    void deleteMany(List<Long> ids);

    UserDto register(RegisterRequestDto dto);

    UserDto login(LoginRequestDto loginRequestDto);
}
