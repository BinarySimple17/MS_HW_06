package ru.binarysimple.auth.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import ru.binarysimple.auth.dto.LoginRequestDto;
import ru.binarysimple.auth.dto.RegisterRequestDto;
import ru.binarysimple.auth.dto.UserDto;
import ru.binarysimple.auth.dto.UserDtoAll;
import ru.binarysimple.auth.model.User;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    User toEntity(UserDto userDto);

    UserDto toUserDto(User user);

    User toEntity(UserDtoAll userDtoAll);

    UserDtoAll toUserDtoAll(User user);

    User updateWithNull(UserDtoAll userDtoAll, @MappingTarget User user);

    User toEntity(LoginRequestDto userDto);

    LoginRequestDto toLoginRequestDto(User user);

    User toEntity(RegisterRequestDto userDto);

    RegisterRequestDto toRegisterRequestDto(User user);
}