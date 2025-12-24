package ru.binarysimple.auth.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.binarysimple.auth.client.UsersServiceClient;
import ru.binarysimple.auth.dto.*;
import ru.binarysimple.auth.filter.UserFilter;
import ru.binarysimple.auth.mapper.UserMapper;
import ru.binarysimple.auth.model.Roles;
import ru.binarysimple.auth.model.User;
import ru.binarysimple.auth.repository.UserRepository;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    private final UserRepository userRepository;

    private final ObjectMapper objectMapper;

    private final UsersServiceClient usersServiceClient;

    @Override
    public Page<UserDtoAll> getAll(UserFilter filter, Pageable pageable) {
        Specification<User> spec = filter.toSpecification();
        Page<User> users = userRepository.findAll(spec, pageable);
        return users.map(userMapper::toUserDtoAll);
    }

    @Override
    public UserDtoAll getOne(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        return userMapper.toUserDtoAll(userOptional.orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id))));
    }

    @Override
    public List<UserDtoAll> getMany(List<Long> ids) {
        List<User> users = userRepository.findAllById(ids);
        return users.stream()
                .map(userMapper::toUserDtoAll)
                .toList();
    }

    @Override
    public UserDtoAll create(UserDtoAll dto) {
        User user = userMapper.toEntity(dto);
        User resultUser = userRepository.save(user);
        return userMapper.toUserDtoAll(resultUser);
    }

    @Override
    public UserDtoAll patch(Long id, JsonNode patchNode) throws IOException {
        User user = userRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id)));

        UserDtoAll userDtoAll = userMapper.toUserDtoAll(user);
        objectMapper.readerForUpdating(userDtoAll).readValue(patchNode);
        userMapper.updateWithNull(userDtoAll, user);

        User resultUser = userRepository.save(user);
        return userMapper.toUserDtoAll(resultUser);
    }

    @Override
    public List<Long> patchMany(List<Long> ids, JsonNode patchNode) throws IOException {
        Collection<User> users = userRepository.findAllById(ids);

        for (User user : users) {
            UserDtoAll userDtoAll = userMapper.toUserDtoAll(user);
            objectMapper.readerForUpdating(userDtoAll).readValue(patchNode);
            userMapper.updateWithNull(userDtoAll, user);
        }

        List<User> resultUsers = userRepository.saveAll(users);
        return resultUsers.stream()
                .map(User::getId)
                .toList();
    }

    @Override
    public UserDtoAll delete(Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            userRepository.delete(user);
        }
        return userMapper.toUserDtoAll(user);
    }

    @Override
    public void deleteMany(List<Long> ids) {
        userRepository.deleteAllById(ids);
    }

    @Override
    public UserDto register(RegisterRequestDto dto) {
        User user = userMapper.toEntity(dto);
        user.setRoles(Set.of(Roles.USER.getRole()));
        User resultUser = userRepository.save(user);

        CreateUserExternalDto externalDto = userMapper.toCreateUserExternalDto(dto);
        // Создаем пользователя в users-service через REST API
        try {
            usersServiceClient.createUser(externalDto);
        } catch (Exception e) {
            // При ошибке создания в users-service удаляем пользователя из auth-service
            userRepository.delete(resultUser);
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Failed to create user in users service");
        }

        return userMapper.toUserDto(resultUser);
    }

    @Override
    public UserDto login(LoginRequestDto loginRequestDto) {
        Optional<User> userOptional = userRepository.findByUsernameAndPassword(loginRequestDto.getUsername(), loginRequestDto.getPassword());
        return userMapper.toUserDto(userOptional.orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "User with username `%s` not found".formatted(loginRequestDto.getUsername()))));
    }
}
