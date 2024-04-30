package com.user.management.service;

import com.user.management.dao.User;
import com.user.management.dto.UserDto;
import com.user.management.dto.UserUpdateDto;
import com.user.management.mapper.UserMapper;
import com.user.management.repository.UserRepository;
import org.mapstruct.factory.Mappers;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository<User> userRepository;
    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @Override
    public UUID registerUser(UserDto userDto) {
        User userToSave = userMapper.mapUserDtoToUser(userDto);

        return userRepository.saveUser(userToSave);
    }

    @Override
    public UserDto partialUpdateUser(String id, UserUpdateDto userDto) {
        User existingUser = userRepository.findUserByUuid(UUID.fromString(id));

        Optional.ofNullable(userDto.email()).ifPresent(existingUser::setEmail);
        Optional.ofNullable(userDto.firstName()).ifPresent(existingUser::setFirstName);
        Optional.ofNullable(userDto.lastName()).ifPresent(existingUser::setLastName);
        Optional.ofNullable(userDto.birthday()).ifPresent(existingUser::setBirthday);
        Optional.ofNullable(userDto.address()).ifPresent(existingUser::setAddress);
        Optional.ofNullable(userDto.phoneNumber()).ifPresent(existingUser::setPhoneNumber);

        return userMapper.mapUserToUserDto(existingUser);
    }

    @Override
    public UserDto updateUserAllFields(String id, UserDto userDto) {
        User existingUser = userRepository.findUserByUuid(UUID.fromString(id));

        existingUser.setEmail(userDto.email());
        existingUser.setFirstName(userDto.firstName());
        existingUser.setLastName(userDto.lastName());
        existingUser.setBirthday(userDto.birthday());
        existingUser.setAddress(userDto.address());
        existingUser.setPhoneNumber(userDto.phoneNumber());

        return userMapper.mapUserToUserDto(existingUser);
    }

    @Override
    public void deleteUser(String id) {
        userRepository.deleteUser(UUID.fromString(id));
    }

    @Override
    public List<UserDto> getUsersByBirthdayBetweenFromBirthdayAndToBirthday(LocalDate fromBirthday, LocalDate toBirthday) {
        return userRepository.getUsersByBirthdayBetweenFromBirthdayAndToBirthday(fromBirthday, toBirthday)
                .stream().map(userMapper::mapUserToUserDto)
                .toList();
    }
}
