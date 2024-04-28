package com.user.management.mapper;

import com.user.management.dao.User;
import com.user.management.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface UserMapper {

    UserDto mapUserToUserDto(User user);

    @Mapping(target = "uuid", expression = "java(java.util.UUID.randomUUID())")
    User mapUserDtoToUser(UserDto userDto);
}
