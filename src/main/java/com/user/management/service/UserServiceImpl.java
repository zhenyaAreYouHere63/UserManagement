package com.user.management.service;

import com.user.management.dao.User;
import com.user.management.dto.UserDto;
import com.user.management.mapper.UserMapper;
import com.user.management.repository.UserRepository;
import com.user.management.validation.UserValidator;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository<User> userRepository;
    private final UserValidator userValidator;
    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @Override
    public UUID registerUser(UserDto userDto) {
        userValidator.validate(userDto);

        User userToSave = userMapper.mapUserDtoToUser(userDto);

        return userRepository.saveUser(userToSave);
    }

    @Override
    public User partialUpdateUser(String id, UserDto userDto) {
        User userByUuid = userRepository.findUserByUuid(UUID.fromString(id));

        userValidator.validate(userDto);

        Optional.ofNullable(userDto.firstName()).ifPresent(userByUuid::setFirstName);
        Optional.ofNullable(userDto.lastName()).ifPresent(userByUuid::setLastName);
        Optional.ofNullable(userDto.birthday()).ifPresent(userByUuid::setBirthday);
        Optional.ofNullable(userDto.address()).ifPresent(userByUuid::setAddress);
        Optional.ofNullable(userDto.phoneNumber()).ifPresent(userByUuid::setPhoneNumber);

        return userByUuid;
    }

    @Override
    public User updateUserAllFields(String id, UserDto userDto) {
        userValidator.validate(userDto);

        User existingUser = userRepository.findUserByUuid(UUID.fromString(id));

        existingUser.setFirstName(userDto.firstName());
        existingUser.setLastName(userDto.lastName());
        existingUser.setBirthday(userDto.birthday());
        existingUser.setAddress(userDto.address());
        existingUser.setPhoneNumber(userDto.phoneNumber());
        // Буде в репозиторії

        return existingUser;
    }

    @Override
    public void deleteUser(String id) {
        userRepository.deleteUser(UUID.fromString(id));
    }

    @Override
    public List<User> getUsersByBirthdayBetweenFromBirthdayAndToBirthday(LocalDate fromBirthday, LocalDate toBirthday) {
        return userRepository.getUsersByBirthdayBetweenFromBirthdayAndToBirthday(fromBirthday, toBirthday);
    }
}
