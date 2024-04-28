package com.user.management.validation;

import com.user.management.dto.UserDto;

public interface UserValidator {

    void validate(UserDto userDto);
}
