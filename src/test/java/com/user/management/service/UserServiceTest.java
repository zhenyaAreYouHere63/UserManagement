package com.user.management.service;

import com.user.management.dao.User;
import com.user.management.dto.UserDto;
import com.user.management.dto.UserUpdateDto;
import com.user.management.mapper.UserMapper;
import com.user.management.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import java.util.UUID;
import java.util.Arrays;
import java.util.List;
import java.util.Collections;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository<User> userRepository;

    @Mock
    private UserMapper userMapper;

    private UUID userId;

    private User user;

    private UserDto userDto;

    private UserUpdateDto userUpdateDto;

    @BeforeEach
    public void setUp() {
        userId = UUID.randomUUID();

        user = new User(userId, "testEmail@gmail.com", "testFirstName",
                "testLastName", LocalDate.parse("2000-10-10"), null, null);

        userDto = new UserDto(null, "testEmail@gmail.com", "testFirstName",
                "testLastName", LocalDate.parse("2000-10-10"), null, null);

        userUpdateDto = new UserUpdateDto(null, "newEmail@gmail.com", null,
                null, null, null, null);
    }

    @Test
    void registerUser_ShouldAddNewUser() {
        when(userRepository.saveUser(user)).thenReturn(userId);

        UUID actualUuid = userService.registerUser(userDto);

        assertThat(actualUuid).isEqualTo(userId);
    }

    @Test
    void updateUserPartially_shouldUpdateSomeUserFields() {
        UserDto expectedResponse =  new UserDto(user.getUuid(), "newEmail@gmail.com",
                "testFirstName", "testLastName", LocalDate.parse("2000-10-10"), null, null);

        when(userRepository.findUserByUuid(userId)).thenReturn(user);

        UserDto actualResponse = userService.updateUserPartially(userId.toString(), userUpdateDto);

        assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    @Test
    void updateUserAllFields_shouldUpdateUserAllFields() {
        UserDto request = new UserDto(null, "updateEmail@gmail.com", "updateFirstName",
                "updateLastName", LocalDate.parse("2002-05-05"), null, null);

        UserDto expectedResponse = new UserDto(user.getUuid(), "updateEmail@gmail.com", "updateFirstName",
                "updateLastName", LocalDate.parse("2002-05-05"), null, null);

        when(userRepository.findUserByUuid(userId)).thenReturn(user);

        UserDto actualResponse = userService.updateUserAllFields(userId.toString(), request);

        assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    @Test
    void deleteUser_shouldDeleteUser() {
        userService.deleteUser(userId.toString());

        verify(userRepository).deleteUser(userId);
    }

    @Test
    void getUsersByBirthdayBetweenFromBirthdayAndToBirthday_shouldGetUsersListBetweenFromBirthdayAndToBirthday() {
        LocalDate fromBirthday = LocalDate.parse("2000-01-01");
        LocalDate toBirthday = LocalDate.parse("2000-12-31");

        User user2 = new User(UUID.randomUUID(), "testEmail2@gmail.com", "testFirstName2",
                "testLastName2", LocalDate.parse("2000-08-08"), null, null);
        List<User> users = Arrays.asList(user, user2);

        UserDto userDto1 = new UserDto(user.getUuid(), "testEmail@gmail.com", "testFirstName",
                "testLastName", LocalDate.parse("2000-10-10"), null, null);
        UserDto userDto2 = new UserDto(user2.getUuid(), "testEmail2@gmail.com", "testFirstName2",
                "testLastName2", LocalDate.parse("2000-08-08"), null, null);
        List<UserDto> expectedUserDtoList = Arrays.asList(userDto1, userDto2);

        when(userRepository.getUsersByBirthdayBetweenFromBirthdayAndToBirthday(fromBirthday, toBirthday))
                .thenReturn(users);

        List<UserDto> actualUserList = userService.getUsersByBirthdayBetweenFromBirthdayAndToBirthday(fromBirthday, toBirthday);

        assertThat(actualUserList).isEqualTo(expectedUserDtoList);
    }

    @Test
    void getUsersByBirthdayBetweenFromBirthdayAndToBirthday_shouldGetEmptyUsersListBetweenFromBirthdayAndToBirthday() {
        LocalDate fromBirthday = LocalDate.parse("2010-01-01");
        LocalDate toBirthday = LocalDate.parse("2010-12-31");

        when(userRepository.getUsersByBirthdayBetweenFromBirthdayAndToBirthday(fromBirthday, toBirthday))
                .thenReturn(Collections.emptyList());

        List<UserDto> actualUserDtoList = userService.getUsersByBirthdayBetweenFromBirthdayAndToBirthday(fromBirthday, toBirthday);

        assertThat(actualUserDtoList.isEmpty()).isTrue();
    }
}
