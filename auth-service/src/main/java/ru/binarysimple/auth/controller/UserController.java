package ru.binarysimple.auth.controller;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.web.bind.annotation.*;
import ru.binarysimple.auth.dto.LoginRequestDto;
import ru.binarysimple.auth.dto.RegisterRequestDto;
import ru.binarysimple.auth.dto.UserDto;
import ru.binarysimple.auth.dto.UserDtoAll;
import ru.binarysimple.auth.filter.UserFilter;
import ru.binarysimple.auth.service.UserService;

import java.io.IOException;

@RestController
@RequestMapping("/auth/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public PagedModel<UserDtoAll> getAll(@ParameterObject @ModelAttribute UserFilter filter, @ParameterObject Pageable pageable) {
        Page<UserDtoAll> userDtoAlls = userService.getAll(filter, pageable);
        return new PagedModel<>(userDtoAlls);
    }


//    @GetMapping
//    public UserDtoAll getOne(@RequestParam Long id) {
//        return userService.getOne(id);
//    }

//    @GetMapping("/by-ids")
//    public List<UserDtoAll> getMany(@RequestParam List<Long> ids) {
//        return userService.getMany(ids);
//    }

    @PostMapping
    public UserDtoAll create(@RequestBody @Valid UserDtoAll dto) {
        return userService.create(dto);
    }

    @PatchMapping
    public UserDtoAll patch(@RequestParam Long id, @RequestBody JsonNode patchNode) throws IOException {
        return userService.patch(id, patchNode);
    }

//    @PatchMapping
//    public List<Long> patchMany(@RequestParam @Valid List<Long> ids, @RequestBody JsonNode patchNode) throws IOException {
//        return userService.patchMany(ids, patchNode);
//    }

    @DeleteMapping
    public UserDtoAll delete(@RequestParam Long id) {
        return userService.delete(id);
    }

//    @DeleteMapping
//    public void deleteMany(@RequestParam List<Long> ids) {
//        userService.deleteMany(ids);
//    }

    @PostMapping("/register")
    public UserDto register(@RequestBody @Valid RegisterRequestDto dto) {
        return userService.register(dto);
    }

    @GetMapping("/login")
    public UserDto login(@RequestBody LoginRequestDto loginRequestDto) {
        return userService.login(loginRequestDto);
    }
}
