package com.user.management.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.user.management.dto.UserDto;
import com.user.management.dto.UserUpdateDto;
import com.user.management.service.UserService;
import static org.hamcrest.Matchers.containsString;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.Assertions.assertThat;

@WebMvcTest(UserController.class)
@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    private static final String USER_URL = "/api/users";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    private UserDto userDto;

    private UserUpdateDto userUpdateDto;

    private String userId;

    @Value("${user.minAge}")
    private int minAge;

    @BeforeEach
    public void setUpData() {
        userId = UUID.randomUUID().toString();
        userDto = new UserDto(UUID.fromString(userId), "testEmail@gmail.com", "testFirstName",
                "testLastName", LocalDate.parse("2000-10-10"), null, null);
        userUpdateDto = new UserUpdateDto(UUID.fromString(userId), "email@i.ua", "firstName",
                "lastName", null, null, null);
    }

    @Test
    void registerUser_ShouldSuccessfullyRegisterUser() throws Exception {
        when(userService.registerUser(userDto)).thenReturn(UUID.fromString(userId));

        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post(USER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isCreated())
                .andReturn();

        UUID actualUuid = objectMapper.readValue(response.getResponse().getContentAsString(), UUID.class);

        assertThat(actualUuid).isEqualTo(UUID.fromString(userId));
    }

    @Test
    void registerUser_ShouldThrow400BadRequestWhenRequestDtoNotValid() throws Exception {
        UUID uuid = UUID.randomUUID();
        UserDto userDto = new UserDto(uuid, null, "testFirstName", "testLastName", LocalDate.parse("2000-01-01"), null, null);

        when(userService.registerUser(userDto)).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.post(USER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].title").value("Validation exception"))
                .andExpect(jsonPath("$[0].error").value("[email: 'Field email can't be a blank']"));
    }

    @Test
    void updateUserPartially_ShouldUpdateSomeUserFields() throws Exception {
        String uuid = UUID.randomUUID().toString();

        UserDto expectedResponse = new UserDto(UUID.fromString(uuid), "email@i.ua", "firstName",
                "lastName", null, null, null);

        when(userService.updateUserPartially(uuid, userUpdateDto)).thenReturn(expectedResponse);

        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.patch(USER_URL + "/{id}", uuid)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userUpdateDto)))
                .andExpect(status().isOk())
                .andReturn();

        UserDto actualResponse = objectMapper.readValue(response.getResponse().getContentAsString(), UserDto.class);

        assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    @Test
    void updateUserPartially_shouldThrowExceptionWhenUserLessThanAllowedNumberYears() throws Exception {
        String uuid = UUID.randomUUID().toString();
        userUpdateDto = new UserUpdateDto(UUID.fromString(uuid), null, "testFirstName",
                "testLastName", LocalDate.parse("2019-04-04"), null, null);

        when(userService.updateUserPartially(uuid, userUpdateDto)).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.patch(USER_URL + "/{id}", uuid)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userUpdateDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].title").value("Validation exception"))
                .andExpect(jsonPath("$[0].error").value(containsString("To register on this site, you must be over " + minAge)));
    }

    @Test
    void updateUserPartially_shouldThrowException400WhenUuidSomeText() throws Exception {
        String uuid = "text";
        UserUpdateDto userUpdateDto = new UserUpdateDto(UUID.fromString(userId), "email@i.ua", "firstName",
                "lastName", LocalDate.parse("2001-05-05"), null, null);

        when(userService.updateUserPartially(uuid, userUpdateDto)).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.patch(USER_URL + "/{id}", uuid)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].title").value("Validation exception"))
                .andExpect(jsonPath("$[0].error").value("[updateUserPartially.id: 'must be a valid UUID']"));
    }

    @Test
    void updateUserPartially_shouldThrowException400WhenUuidIncorrect() throws Exception {
        UserUpdateDto userUpdateDto = new UserUpdateDto(UUID.fromString(userId), "email@i.ua", "firstName",
                "lastName", LocalDate.parse("2001-01-01"), null, null);

        when(userService.updateUserPartially(userId + "s", userUpdateDto)).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.patch(USER_URL + "/{id}", userId + "s")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].title").value("Validation exception"))
                .andExpect(jsonPath("$[0].error").value("[partialUpdateUser.id: 'must be a valid UUID']"));
    }

    @Test
    void updateUserAllFields_shouldUpdateAllFields() throws Exception {
        UserDto expectedResponse = new UserDto(UUID.fromString(userId), "testEmail@gmail.com", "testFirstName",
                "testLastName", LocalDate.parse("2000-01-01"), null, null);

        when(userService.updateUserAllFields(userId, userDto)).thenReturn(expectedResponse);

        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.put(USER_URL + "/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andReturn();

        UserDto actualResponse = objectMapper.readValue(response.getResponse().getContentAsString(), UserDto.class);

        assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    @Test
    void deleteUser() throws Exception {
        userService.deleteUser(userId);

        mockMvc.perform(MockMvcRequestBuilders.delete(USER_URL + "/{id}", userId))
                .andExpect(status().isNoContent());
    }

    @Test
    void getUsersByBirthdayBetweenFromBirthdayAndToBirthday_shouldGetUsersListBetweenFromBirthdayAndToBirthday() throws Exception {
        LocalDate fromBirthday = LocalDate.parse("2000-01-01");
        LocalDate toBirthday = LocalDate.parse("2000-12-31");

        List<UserDto> expectedResponse = List.of(userDto);

        when(userService.getUsersByBirthdayBetweenFromBirthdayAndToBirthday(fromBirthday, toBirthday))
                .thenReturn(expectedResponse);

        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get(USER_URL)
                        .param("fromBirthday", fromBirthday.toString())
                        .param("toBirthday", toBirthday.toString()))
                .andExpect(status().isOk())
                .andReturn();

        List<UserDto> actualResponse = objectMapper.readValue(response.getResponse().getContentAsString(), new TypeReference<>() {
        });

        assertThat(actualResponse).isEqualTo(expectedResponse);
    }
}
