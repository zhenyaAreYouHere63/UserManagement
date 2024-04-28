package com.user.management.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.user.management.dao.User;
import com.user.management.dto.UserDto;
import com.user.management.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.Assertions.assertThat;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

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

    private UUID uuid;

    @BeforeEach
    public void setUpData() {
        uuid = UUID.randomUUID();
        userDto = new UserDto(uuid, "testEmail@gmail.com", "testFirstName",
                "testLastName", LocalDate.parse("2000-10-10"), null, null);
    }

    @Test
    void registerUser_ShouldSuccessfullyRegisterUser() throws Exception {
        when(userService.registerUser(userDto)).thenReturn(uuid);

        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post(USER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isCreated())
                .andReturn();

        UUID actualUuid = objectMapper.readValue(response.getResponse().getContentAsString(), UUID.class);

        assertThat(actualUuid).isEqualTo(uuid);
    }

    @Test
    void registerUser_ShouldThrow400BadRequestWhenRequestDtoNotValid() throws Exception {
        UUID uuid = UUID.randomUUID();
        UserDto userDto = new UserDto(uuid, null, null, "testLastName", LocalDate.parse("2000-01-01"), null, null);

        when(userService.registerUser(userDto)).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.post(USER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].title").value("Validation exception"))
                .andExpect(jsonPath("$[0].error").value("[email: 'Field email can't be a blank']"))
                .andExpect(jsonPath("$[1].title").value("Validation exception"))
                .andExpect(jsonPath("$[1].error").value("[firstName: 'Field firstName can't be a blank']"));
    }

    @Test
    void partialUpdateUser_ShouldUpdateSomeUserFields() throws Exception {
        UUID uuid = UUID.randomUUID();
        UserDto requestUser = new UserDto(uuid, "email@i.ua", "firstName", "lastName", LocalDate.parse("2001-01-01"), null, null);

        User expectedResponse = new User(uuid, "email@i.ua", "firstName",
                "lastName", LocalDate.parse("2000-01-01"), null, null);

        when(userService.partialUpdateUser(uuid.toString(), requestUser)).thenReturn(expectedResponse);

        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.patch(USER_URL + "/{id}", uuid.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestUser)))
                .andExpect(status().isOk())
                .andReturn();

        User actualResponse = objectMapper.readValue(response.getResponse().getContentAsString(), User.class);

        assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    @Test
    void partialUpdateUser_shouldThrowException400WhenUuidSomeText() throws Exception {
        String uuid = "text";

        when(userService.partialUpdateUser(uuid, userDto)).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.patch(USER_URL + "/{id}", uuid)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].title").value("Validation exception"))
                .andExpect(jsonPath("$[0].error").value("[partialUpdateUser.id: 'must be a valid UUID']"));
    }

    @Test
    void partialUpdateUser_shouldThrowException400WhenUuidIncorrect() throws Exception {
        when(userService.partialUpdateUser(uuid.toString() + "s", userDto)).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.patch(USER_URL + "/{id}", uuid.toString() + "s")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].title").value("Validation exception"))
                .andExpect(jsonPath("$[0].error").value("[partialUpdateUser.id: 'must be a valid UUID']"));
    }

    @Test
    void updateUserAllFields_shouldUpdateAllFields() throws Exception {
        User expectedResponse = new User(uuid, "testEmail@gmail.com", "testFirstName",
                "testLastName", LocalDate.parse("2000-01-01"), null, null);

        when(userService.updateUserAllFields(uuid.toString(), userDto)).thenReturn(expectedResponse);

        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.put(USER_URL + "/{id}", uuid.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andReturn();

        User actualResponse = objectMapper.readValue(response.getResponse().getContentAsString(), User.class);

        assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    @Test
    void deleteUser() throws Exception {
        userService.deleteUser(uuid.toString());

        mockMvc.perform(MockMvcRequestBuilders.delete(USER_URL + "/{id}", uuid.toString()))
                .andExpect(status().isNoContent());
    }

    @Test
    void getUsersByBirthdayBetweenFromBirthdayAndToBirthday_shouldGetUsersListBetweenFromBirthdayAndToBirthday() throws Exception {
        LocalDate fromBirthday = LocalDate.parse("2000-01-01");
        LocalDate toBirthday = LocalDate.parse("2000-12-31");

        User user = new User(uuid, "testEmail@gmail.com", "testFirstName",
                "testLastName", LocalDate.parse("2000-01-01"), null, null);

        List<User> expectedResponse = List.of(user);

        when(userService.getUsersByBirthdayBetweenFromBirthdayAndToBirthday(fromBirthday, toBirthday))
                .thenReturn(expectedResponse);

        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get(USER_URL)
                        .param("fromBirthday", fromBirthday.toString())
                        .param("toBirthday", toBirthday.toString()))
                .andExpect(status().isOk())
                .andReturn();

        List<User> actualResponse = objectMapper.readValue(response.getResponse().getContentAsString(), new TypeReference<>() {
        });

        assertThat(actualResponse).isEqualTo(expectedResponse);
    }
}
