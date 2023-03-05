package com.ays.backend.user.service;

import com.ays.backend.base.BaseServiceTest;
import com.ays.backend.mapper.UserMapper;
import com.ays.backend.user.controller.payload.request.SignUpRequest;
import com.ays.backend.user.controller.payload.request.SignUpRequestBuilder;
import com.ays.backend.user.exception.UserNotFoundException;
import com.ays.backend.user.model.entities.User;
import com.ays.backend.user.model.entities.UserBuilder;
import com.ays.backend.user.model.enums.UserRole;
import com.ays.backend.user.model.enums.UserStatus;
import com.ays.backend.user.repository.UserRepository;
import com.ays.backend.user.service.dto.UserDTO;
import com.ays.backend.user.service.dto.UserDTOBuilder;
import com.ays.backend.user.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class UserServiceTest extends BaseServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;


    @Test
    void shouldCreateUser() {
        // given
        SignUpRequest signUpRequest = new SignUpRequestBuilder()
                .withStatusId(UserStatus.PASSIVE.ordinal()).build();

        User user = new UserBuilder()
                .withSignUpRequest(signUpRequest)
                .withUserRole(UserRole.ROLE_VOLUNTEER).build();

        when(userRepository.save(any(User.class))).thenReturn(user);

        // when
        UserDTO savedUserDTO = userService.saveUser(signUpRequest);

        // then
        assertEquals(signUpRequest.getUsername(), savedUserDTO.getUsername());
        assertEquals(savedUserDTO.getUserRole(), user.getUserRole());
        assertEquals(signUpRequest.getCountryCode(), savedUserDTO.getCountryCode());
        assertEquals(signUpRequest.getLineNumber(), savedUserDTO.getLineNumber());
        assertEquals(signUpRequest.getStatusId(), savedUserDTO.getUserStatus().ordinal());
    }

    @Test
    public void shouldGetAllUsers() {

        // given
        List<User> userList = new UserBuilder().getUsers();
        List<UserDTO> userDtoList = new UserDTOBuilder().getUserDtos();

        // Create a Page object with the sample users
        Page<User> users = new PageImpl<>(userList);

        // mock the mapper to return non-null UserDTO objects
        for (int i = 0; i < userList.size(); i++) {
            when(userMapper.mapUsertoUserDTO(userList.get(i))).thenReturn(userDtoList.get(i));
        }

        // when
        when(userRepository.findAll(any(Pageable.class))).thenReturn(users);

        // Call the getAllUsers method of the userService
        Page<UserDTO> result = userService.getAllUsers(PageRequest.of(1, 10));

        // then
        assertEquals(result.getContent().size(),2);
        assertEquals(result.getContent().get(0).getUsername(), userDtoList.get(0).getUsername());
        assertEquals(result.getContent().get(0).getFirstName(), userDtoList.get(0).getFirstName());
        assertEquals(result.getContent().get(0).getLastName(), userDtoList.get(0).getLastName());
        assertEquals(result.getContent().get(1).getUsername(), userDtoList.get(1).getUsername());
        assertEquals(result.getContent().get(1).getFirstName(), userDtoList.get(1).getFirstName());
        assertEquals(result.getContent().get(1).getLastName(), userDtoList.get(1).getLastName());

    }

    @Test
    public void shouldGetUserDTOById() {

        // given
        SignUpRequest signUpRequest = new SignUpRequestBuilder()
                .withStatusId(UserStatus.PASSIVE.ordinal()).build();

        User user = new UserBuilder()
                .withSignUpRequest(signUpRequest)
                .withUserRole(UserRole.ROLE_VOLUNTEER).build();

        UserDTO mockUserDto = UserDTO.builder()
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .userRole(user.getUserRole())
                .countryCode(user.getCountryCode())
                .lineNumber(user.getLineNumber())
                .userStatus(user.getStatus())
                .email(user.getEmail())
                .lastLoginDate(user.getLastLoginDate())
                .build();

        // mock the mapper to return non-null UserDTO objects
        when(userMapper.mapUsertoUserDTO(any(User.class))).thenReturn(mockUserDto);

        // when
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserDTO userDto = userService.getUserById(1L);

        assertEquals(user.getUsername(), userDto.getUsername());
        assertEquals(user.getFirstName(), userDto.getFirstName());
        assertEquals(user.getLastName(), userDto.getLastName());
        assertEquals(user.getUserRole().ordinal(), userDto.getUserRole().ordinal());
        assertEquals(user.getCountryCode(), userDto.getCountryCode());
        assertEquals(user.getLineNumber(), userDto.getLineNumber());
        assertEquals(user.getStatus(), userDto.getUserStatus());
        assertEquals(user.getEmail(), userDto.getEmail());
        assertEquals(user.getLastLoginDate(), userDto.getLastLoginDate());
    }

    @Test
    public void shouldThrowUserNotFoundExceptionWhenUserNotExists() {

        // given
        UserNotFoundException expectedError = new UserNotFoundException("User with id -1 not found");

        // when -  action or the behaviour that we are going test
        when(userRepository.findById(-1L)).thenReturn(Optional.empty());

        UserNotFoundException actual = assertThrows(UserNotFoundException.class, () -> userService.getUserById(-1L));

        assertEquals(expectedError.getMessage(), actual.getMessage());

    }
}
