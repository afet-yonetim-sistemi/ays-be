//package com.ays.user.service.impl;
//
//import com.ays.BaseServiceTest;
//import com.ays.user.controller.dto.request.UserUpdateRequest;
//import com.ays.user.controller.payload.request.SignUpRequestBuilder;
//import com.ays.user.model.User;
//import com.ays.user.model.UserBuilder;
//import com.ays.user.model.entity.UserEntity;
//import com.ays.user.model.entity.UserEntityBuilder;
//import com.ays.user.model.enums.UserRole;
//import com.ays.user.model.enums.UserStatus;
//import com.ays.user.repository.UserRepository;
//import com.ays.user.util.exception.AysUserNotExistByUsernameException;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.when;
//
//class UserServiceImplTest extends BaseServiceTest {
//
//    @Mock
//    private UserRepository userRepository;
//
//    @Mock
//    private UserMapper userMapper;
//
//    @InjectMocks
//    private UserServiceImpl userService;
//
//
//    @Test
//    void shouldCreateUser() {
//        // given
//        AdminSignUpRequest signUpRequest = new SignUpRequestBuilder()
//                .withStatusId(UserStatus.PASSIVE.ordinal()).build();
//
//        UserEntity user = new UserEntityBuilder()
//                .withSignUpRequest(signUpRequest)
//                .withUserRole(UserRole.VOLUNTEER).build();
//
//        when(userRepository.save(any(UserEntity.class))).thenReturn(user);
//
//        // when
//        User savedUser = userService.saveUser(signUpRequest);
//
//        // then
//        assertEquals(signUpRequest.getUsername(), savedUser.getUsername());
//        assertEquals(savedUser.getRole(), user.getRole());
//        assertEquals(signUpRequest.getCountryCode(), savedUser.getCountryCode());
//        assertEquals(signUpRequest.getLineNumber(), savedUser.getLineNumber());
//        assertEquals(signUpRequest.getStatusId(), savedUser.getStatus().ordinal());
//    }
//
//    @Test
//    public void shouldGetAllUsers() {
//
//        // given
//        List<UserEntity> userList = new UserEntityBuilder().getUsers();
//        List<User> userDtoList = new UserBuilder().getUsers();
//
//        // Create a Page object with the sample users
//        Page<UserEntity> users = new PageImpl<>(userList);
//
//        // mock the mapper to return non-null UserDTO objects
//        for (int i = 0; i < userList.size(); i++) {
//            when(userMapper.mapUserEntityToUser(userList.get(i))).thenReturn(userDtoList.get(i));
//        }
//
//
//        // when
//        when(userRepository.findAll(any(Pageable.class))).thenReturn(users);
//
//        // Call the getAllUsers method of the userService
//        Page<User> result = userService.getAllUsers(PageRequest.of(1, 10));
//
//        // then
//        assertEquals(result.getContent().size(), 2);
//        assertEquals(result.getContent().get(0).getUsername(), userDtoList.get(0).getUsername());
//        assertEquals(result.getContent().get(0).getFirstName(), userDtoList.get(0).getFirstName());
//        assertEquals(result.getContent().get(0).getLastName(), userDtoList.get(0).getLastName());
//        assertEquals(result.getContent().get(1).getUsername(), userDtoList.get(1).getUsername());
//        assertEquals(result.getContent().get(1).getFirstName(), userDtoList.get(1).getFirstName());
//        assertEquals(result.getContent().get(1).getLastName(), userDtoList.get(1).getLastName());
//
//    }
//
//    @Test
//    public void shouldGetUserDTOById() {
//
//        // given
//        AdminSignUpRequest signUpRequest = new SignUpRequestBuilder()
//                .withStatusId(UserStatus.WAITING.ordinal()).build();
//
//        UserEntity user = new UserEntityBuilder()
//                .withSignUpRequest(signUpRequest)
//                .withUserRole(UserRole.VOLUNTEER).build();
//
//        User mockUserDto = User.builder()
//                .username(user.getUsername())
//                .firstName(user.getFirstName())
//                .lastName(user.getLastName())
//                .role(user.getRole())
//                .countryCode(user.getCountryCode())
//                .lineNumber(user.getLineNumber())
//                .status(user.getStatus())
//                .email(user.getEmail())
//                .lastLoginDate(user.getLastLoginDate())
//                .build();
//
//        // mock the mapper to return non-null UserDTO objects
//        when(userMapper.mapUserEntityToUser(any(UserEntity.class))).thenReturn(mockUserDto);
//
//
//        // when
//        when(userRepository.findByIdAndStatusNot(1L, UserStatus.PASSIVE)).thenReturn(Optional.of(user));
//
//        User userDto = userService.getUserById(1L);
//
//        // then
//        assertEquals(user.getUsername(), userDto.getUsername());
//        assertEquals(user.getFirstName(), userDto.getFirstName());
//        assertEquals(user.getLastName(), userDto.getLastName());
//        assertEquals(user.getRole().ordinal(), userDto.getRole().ordinal());
//        assertEquals(user.getCountryCode(), userDto.getCountryCode());
//        assertEquals(user.getLineNumber(), userDto.getLineNumber());
//        assertEquals(user.getStatus(), userDto.getStatus());
//        assertEquals(user.getEmail(), userDto.getEmail());
//        assertEquals(user.getLastLoginDate(), userDto.getLastLoginDate());
//    }
//
//    @Test
//    public void shouldThrowUserNotFoundExceptionWhenUserNotExists() {
//
//        Long id = -1L;
//
//        // given
//        AysUserNotExistByUsernameException expectedError = new AysUserNotExistByUsernameException("User with id -1 not found");
//
//        // when -  action or the behaviour that we are going test
//        when(userRepository.findByIdAndStatusNot(id, UserStatus.PASSIVE)).thenReturn(Optional.empty());
//
//        AysUserNotExistByUsernameException actual = assertThrows(AysUserNotExistByUsernameException.class, () -> userService.getUserById(-1L));
//
//        // then
//        assertEquals(expectedError.getMessage(), actual.getMessage());
//
//    }
//
//
//    @Test
//    void shouldSoftDeleteUserById() {
//        Long id = 1L;
//
//        // given
//        UserEntity userInfoWithWaitingStatus = new UserEntityBuilder().getUserSamplewithWaitingStatus();
//        UserEntity userInfoWithPassiveStatus = new UserEntityBuilder().getUserSamplewithPassiveStatus();
//        User userDTOInfoWithPassiveStatus = new UserBuilder().getUserDTOwithPassiveStatus();
//
//        // when
//        when(userRepository.findById(id)).thenReturn(Optional.of(userInfoWithWaitingStatus));
//        when(userRepository.save(any(UserEntity.class))).thenReturn(userInfoWithPassiveStatus);
//
//        userService.deleteUser(id);
//
//        // then
//        assertEquals(UserStatus.DELETED, userInfoWithWaitingStatus.getStatus());
//
//    }
//
//    @Test
//    void shouldNotSoftDeleteUserWhenIdInvalidId() {
//        Long id = -1L;
//
//        // given
//        AysUserNotExistByUsernameException expectedError = new AysUserNotExistByUsernameException("User with id -1 not found");
//
//        // when
//        when(userRepository.findById(id)).thenReturn(Optional.empty());
//
//        AysUserNotExistByUsernameException actual = assertThrows(AysUserNotExistByUsernameException.class, () -> userService.deleteUser(id));
//
//        // then
//        assertEquals(expectedError.getMessage(), actual.getMessage());
//    }
//
//    @Test
//    void shouldUpdateUserById() {
//        Long id = 1L;
//
//        // given
//        UserEntity sampleUser = new UserEntityBuilder().getUserSample();
//        UserUpdateRequest userUpdateRequest = UserUpdateRequest.builder()
//                .id(id)
//                .username("updatedusername")
//                .firstName("updatedfirstname")
//                .lastName("updatedlastname")
//                .userRole(UserRole.VOLUNTEER)
//                .userStatus(UserStatus.VERIFIED)
//                .build();
//
//        UserEntity updatedUser = new UserEntityBuilder().getUpdatedUser();
//        User updatedDTO = new UserBuilder().getUpdatedUserDTO();
//
//
//        // when
//        when(userRepository.findById(1L)).thenReturn(Optional.of(sampleUser));
//        when(userRepository.save(any(UserEntity.class))).thenReturn(updatedUser);
//        when(userMapper.mapUserEntityToUser(any(UserEntity.class))).thenReturn(updatedDTO);
//        when(userMapper.mapUpdateRequestToUser(eq(userUpdateRequest), eq(sampleUser))).thenReturn(updatedUser);
//
//        User updatedUserDTO = userService.updateUser(userUpdateRequest);
//
//        // then
//        assertEquals(updatedDTO.getUsername(), updatedUserDTO.getUsername());
//        assertEquals(updatedDTO.getFirstName(), updatedUserDTO.getFirstName());
//        assertEquals(updatedDTO.getLastName(), updatedUserDTO.getLastName());
//        assertEquals(updatedDTO.getRole().ordinal(), updatedUserDTO.getRole().ordinal());
//        assertEquals(updatedDTO.getStatus().ordinal(), updatedUserDTO.getStatus().ordinal());
//    }
//
//    @Test
//    void shouldNotUpdateUserWhenIdInvalidId() {
//        Long id = -1L;
//
//        // given
//        AysUserNotExistByUsernameException expectedError = new AysUserNotExistByUsernameException("User with id -1 not found");
//        UserUpdateRequest userUpdateRequest = UserUpdateRequest.builder()
//                .id(id)
//                .username("updatedusername")
//                .firstName("updatedfirstname")
//                .lastName("updatedlastname")
//                .userRole(UserRole.VOLUNTEER)
//                .userStatus(UserStatus.VERIFIED)
//                .build();
//
//        // when
//        when(userRepository.findById(id)).thenReturn(Optional.empty());
//
//        AysUserNotExistByUsernameException actual = assertThrows(AysUserNotExistByUsernameException.class, () -> userService.updateUser(userUpdateRequest));
//
//        // then
//        assertEquals(expectedError.getMessage(), actual.getMessage());
//    }
//}
