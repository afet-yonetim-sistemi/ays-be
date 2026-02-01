package org.ays.auth.controller;

import org.ays.AysRestControllerTest;
import org.ays.auth.model.AysUser;
import org.ays.auth.model.AysUserBuilder;
import org.ays.auth.model.AysUserFilter;
import org.ays.auth.model.mapper.AysUserToResponseMapper;
import org.ays.auth.model.mapper.AysUserToUsersResponseMapper;
import org.ays.auth.model.request.AysUserCreateRequest;
import org.ays.auth.model.request.AysUserCreateRequestBuilder;
import org.ays.auth.model.request.AysUserListRequest;
import org.ays.auth.model.request.AysUserListRequestBuilder;
import org.ays.auth.model.request.AysUserUpdateRequest;
import org.ays.auth.model.request.AysUserUpdateRequestBuilder;
import org.ays.auth.model.response.AysUserResponse;
import org.ays.auth.model.response.AysUsersResponse;
import org.ays.auth.service.AysUserCreateService;
import org.ays.auth.service.AysUserReadService;
import org.ays.auth.service.AysUserUpdateService;
import org.ays.common.model.AysPage;
import org.ays.common.model.AysPageBuilder;
import org.ays.common.model.AysPageableBuilder;
import org.ays.common.model.request.AysPhoneNumberRequest;
import org.ays.common.model.request.AysPhoneNumberRequestBuilder;
import org.ays.common.model.response.AysErrorResponse;
import org.ays.common.model.response.AysErrorResponseBuilder;
import org.ays.common.model.response.AysPageResponse;
import org.ays.common.model.response.AysResponse;
import org.ays.common.model.response.AysResponseBuilder;
import org.ays.common.util.AysRandomUtil;
import org.ays.util.AysMockMvcRequestBuilders;
import org.ays.util.AysMockResultMatchersBuilders;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class AysUserControllerTest extends AysRestControllerTest {

    @MockitoBean
    private AysUserReadService userReadService;

    @MockitoBean
    private AysUserCreateService userCreateService;

    @MockitoBean
    private AysUserUpdateService userUpdateService;


    private final AysUserToUsersResponseMapper userToUsersResponseMapper = AysUserToUsersResponseMapper.initialize();
    private final AysUserToResponseMapper userToResponseMapper = AysUserToResponseMapper.initialize();


    private static final String BASE_PATH = "/api/institution/v1";


    @Test
    void givenValidUserListRequest_whenUsersFound_thenReturnAysPageResponseOfUsersResponse() throws Exception {
        // Given
        AysUserListRequest mockListRequest = new AysUserListRequestBuilder()
                .withValidValues()
                .build();

        //When
        List<AysUser> mockUsers = List.of(
                new AysUserBuilder().withValidValues().build()
        );

        AysPage<AysUser> mockUserPage = AysPageBuilder
                .from(mockUsers, mockListRequest.getPageable());

        Mockito.when(userReadService.findAll(Mockito.any(AysUserListRequest.class)))
                .thenReturn(mockUserPage);

        // Then
        String endpoint = BASE_PATH.concat("/users");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockSuperAdminToken.getAccessToken(), mockListRequest);

        List<AysUsersResponse> mockUsersResponse = userToUsersResponseMapper.map(mockUsers);
        AysPageResponse<AysUsersResponse> pageOfResponse = AysPageResponse.<AysUsersResponse>builder()
                .of(mockUserPage)
                .content(mockUsersResponse)
                .build();
        AysResponse<AysPageResponse<AysUsersResponse>> mockResponse = AysResponse
                .successOf(pageOfResponse);

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .isNotEmpty());

        // Verify
        Mockito.verify(userReadService, Mockito.times(1))
                .findAll(Mockito.any(AysUserListRequest.class));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "Lorem ipsum dolor sit amet consectetur adipiscing elit Integer nec odio Praesent libero Sed cursus ante dapibus diam Sed nisi Nulla quis sem at nibh",
            "Test user 1234",
            "User *^%$#",
            " Test",
            "? User",
            "J",
            "J----",
            "Martin-Luther--King",
            "John  Doe"
    })
    void givenUserListRequest_whenFirstNameDoesNotValid_thenReturnValidationError(String invalidName) throws Exception {

        // Given
        AysUserListRequest mockListRequest = new AysUserListRequestBuilder()
                .withValidValues()
                .withFirstName(invalidName)
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/users");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockSuperAdminToken.getAccessToken(), mockListRequest);

        AysErrorResponse mockErrorResponse = AysErrorResponseBuilder.VALIDATION_ERROR;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isBadRequest())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .isNotEmpty());

        // Verify
        Mockito.verify(userReadService, Mockito.never())
                .findAll(Mockito.any(AysUserListRequest.class));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "Lorem ipsum dolor sit amet consectetur adipiscing elit Integer nec odio Praesent libero Sed cursus ante dapibus diam Sed nisi Nulla quis sem at nibh",
            "Test user 1234",
            "User *^%$#",
            " Test",
            "? User",
            "J",
            "J----",
            "Martin-Luther--King",
            "John  Doe"
    })
    void givenUserListRequest_whenLastNameDoesNotValid_thenReturnValidationError(String invalidName) throws Exception {

        // Given
        AysUserListRequest mockListRequest = new AysUserListRequestBuilder()
                .withValidValues()
                .withLastName(invalidName)
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/users");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockSuperAdminToken.getAccessToken(), mockListRequest);

        AysErrorResponse mockErrorResponse = AysErrorResponseBuilder.VALIDATION_ERROR;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isBadRequest())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .isNotEmpty());

        // Verify
        Mockito.verify(userReadService, Mockito.never())
                .findAll(Mockito.any(AysUserListRequest.class));
    }

    @CsvSource({
            "must be integer, 321.",
            "must be integer, 12.34",
            "must be integer, '12,34'",
            "must be integer, 1 234",
            "must be integer, 12a34",
            "must be integer, #",
            "must be integer, '    '",
            "must be positive integer, -321",
            "must be positive integer, 0",
            "size must be between 1 and 10, ''",
            "size must be between 1 and 10, 12345678912367",
            "size must be between 1 and 10, 12345678912367564656464858",
    })
    @ParameterizedTest
    void givenUserListRequest_whenLineNumberIsNotValid_thenReturnValidationError(String mockSubErrorMessage,
                                                                                 String mockLineNumber) throws Exception {

        // Given
        AysUserFilter.PhoneNumber mockPhoneNumber = new AysUserFilter.PhoneNumber();
        mockPhoneNumber.setLineNumber(mockLineNumber);

        AysUserListRequest mockListRequest = new AysUserListRequestBuilder()
                .withValidValues()
                .withPhoneNumber(mockPhoneNumber)
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/users");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockSuperAdminToken.getAccessToken(), mockListRequest);

        AysErrorResponse mockErrorResponse = AysErrorResponseBuilder.VALIDATION_ERROR;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isBadRequest())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .isArray())
                .andExpect(AysMockResultMatchersBuilders.subErrorsSize()
                        .value(1))
                .andExpect(AysMockResultMatchersBuilders.subErrors("[*].message")
                        .value(mockSubErrorMessage))
                .andExpect(AysMockResultMatchersBuilders.subErrors("[*].field")
                        .value("lineNumber"));

        // Verify
        Mockito.verify(userReadService, Mockito.never())
                .findAll(Mockito.any(AysUserListRequest.class));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "",
            "bekeleandreaevelynirenealexandrascottmirasoniamustafahuivladimirmarcoyolandaraymondakhtermichaeldennistatianayuliyagangmargaretthomassumanjeanamymostafasaidrubenchenedithjumasitimeilucasgaryghulamminaxiaohongmarcosrafaelamyantoniamohamadfatmaahmed@aystest.org"
    })
    void givenUserListRequest_whenEmailAddressDoesNotValid_thenReturnValidationError(String mockEmailAddress) throws Exception {

        // Given
        AysUserListRequest mockListRequest = new AysUserListRequestBuilder()
                .withValidValues()
                .withEmailAddress(mockEmailAddress)
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/users");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockSuperAdminToken.getAccessToken(), mockListRequest);

        AysErrorResponse mockErrorResponse = AysErrorResponseBuilder.VALIDATION_ERROR;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isBadRequest())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .isNotEmpty());

        // Verify
        Mockito.verify(userReadService, Mockito.never())
                .findAll(Mockito.any(AysUserListRequest.class));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "City user 1234",
            "City *^%$#",
            " Test",
            "? User",
            "J",
            "J----",
            "City--King",
            "John  Doe"
    })
    void givenUserListRequest_whenCityDoesNotValid_thenReturnValidationError(String invalidName) throws Exception {

        // Given
        AysUserListRequest mockListRequest = new AysUserListRequestBuilder()
                .withValidValues()
                .withCity(invalidName)
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/users");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockSuperAdminToken.getAccessToken(), mockListRequest);

        AysErrorResponse mockErrorResponse = AysErrorResponseBuilder.VALIDATION_ERROR;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isBadRequest())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .isNotEmpty());

        // Verify
        Mockito.verify(userReadService, Mockito.never())
                .findAll(Mockito.any(AysUserListRequest.class));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "1",
            "109",
            "99999",
            "15"
    })
    void givenInvalidUserListRequest_whenPageSizeNotTen_thenReturnValidationError(int invalidPageSize) throws Exception {

        // Given
        AysUserListRequest mockListRequest = new AysUserListRequestBuilder()
                .withPageable(new AysPageableBuilder()
                        .withPage(1)
                        .withPageSize(invalidPageSize)
                        .build()
                )
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/users");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockSuperAdminToken.getAccessToken(), mockListRequest);

        AysErrorResponse mockErrorResponse = AysErrorResponseBuilder.VALIDATION_ERROR;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isBadRequest())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .isNotEmpty());

        // Verify
        Mockito.verify(userReadService, Mockito.never())
                .findAll(Mockito.any(AysUserListRequest.class));
    }

    @Test
    void givenValidUserListRequest_whenUserUnauthorized_thenReturnAccessDeniedException() throws Exception {
        // Given
        AysUserListRequest mockListRequest = new AysUserListRequestBuilder()
                .withValidValues()
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/users");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockUserToken.getAccessToken(), mockListRequest);

        AysErrorResponse mockErrorResponse = AysErrorResponseBuilder.FORBIDDEN;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isForbidden())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .doesNotExist());

        // Verify
        Mockito.verify(userReadService, Mockito.never())
                .findAll(Mockito.any(AysUserListRequest.class));
    }


    @Test
    void givenValidUserId_whenUserFound_thenReturnAysUserResponse() throws Exception {

        // Given
        String mockUserId = AysRandomUtil.generateUUID();

        // When
        AysUser mockUser = new AysUserBuilder()
                .withValidValues()
                .withId(mockUserId)
                .build();

        Mockito.when(userReadService.findById(mockUserId))
                .thenReturn(mockUser);

        // Then
        String endpoint = BASE_PATH.concat("/user/").concat(mockUserId);
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .get(endpoint, mockAdminToken.getAccessToken());

        AysUserResponse mockUserResponse = userToResponseMapper
                .map(mockUser);
        AysResponse<AysUserResponse> mockResponse = AysResponse
                .successOf(mockUserResponse);

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .isNotEmpty());

        // Verify
        Mockito.verify(userReadService, Mockito.times(1))
                .findById(mockUserId);
    }

    @Test
    void givenUserId_whenUnauthorizedForGettingUserById_thenReturnAccessDeniedException() throws Exception {

        // Given
        String mockUserId = AysRandomUtil.generateUUID();

        // Then
        String endpoint = BASE_PATH.concat("/user/".concat(mockUserId));
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .get(endpoint, mockUserToken.getAccessToken());

        AysErrorResponse mockErrorResponse = AysErrorResponseBuilder.FORBIDDEN;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isForbidden())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .doesNotExist());

        // Verify
        Mockito.verify(userReadService, Mockito.never())
                .findById(mockUserId);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "A",
            "493268349068342"
    })
    void givenInvalidId_whenIdNotValid_thenReturnValidationError(String invalidId) throws Exception {

        // Then
        String endpoint = BASE_PATH.concat("/user/").concat(invalidId);
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .get(endpoint, mockAdminToken.getAccessToken());

        AysErrorResponse mockErrorResponse = AysErrorResponseBuilder.VALIDATION_ERROR;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isBadRequest())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .isNotEmpty());

        // Verify
        Mockito.verify(userReadService, Mockito.never())
                .findById(Mockito.anyString());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "Su",
            "Çağla",
            "Robert William Floyd",
            "Dr. Ahmet",
            "Ahmet-Mehmet",
            "Ahmet - Mehmet",
            "O'Connor",
            "ya",
            "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean"
             })
    void givenUserCreateRequest_whenUserCreatedWithValidFirstNameAndLastName_thenReturnSuccess(String mockValidName) throws Exception {

        // Given
        AysUserCreateRequest mockCreateRequest = new AysUserCreateRequestBuilder()
                .withValidValues()
                .withFirstName(mockValidName)
                .withLastName(mockValidName)
                .build();

        // When
        Mockito.doNothing()
                .when(userCreateService)
                .create(Mockito.any(AysUserCreateRequest.class));

        // Then
        String endpoint = BASE_PATH.concat("/user");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockAdminToken.getAccessToken(), mockCreateRequest);

        AysResponse<Void> mockResponse = AysResponseBuilder.success();

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .doesNotExist());

        // Verify
        Mockito.verify(userCreateService, Mockito.times(1))
                .create(Mockito.any(AysUserCreateRequest.class));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "a@b.co",
            "abcdef@mail.com",
            "abc+def@archive.com",
            "john.doe123@example.co.uk",
            "admin_123@example.org",
            "admin-test@ays.com",
            "johndoe@gmail.com",
            "janedoe123@yahoo.com",
            "michael.jordan@nba.com",
            "alice.smith@company.co.uk",
            "info@mywebsite.org",
            "support@helpdesk.net",
            "rajeshmehmetjosephanastasiyahamidjianguonalalitachunoscarmanojfelixmichaelhugoaslambeatrizsergeyemmaricardohenrymunnigaryrobertorosehungabdullahramaisaaclijunxinchonadiaqiangyuliyabrendapauljeanlyubovpablogiuseppexuanchaosimakevinminlongperez@aystest.org"
    })
    void givenUserCreateRequest_whenUserCreated_thenReturnSuccess(String mockEmailAddress) throws Exception {

        // Given
        AysUserCreateRequest mockCreateRequest = new AysUserCreateRequestBuilder()
                .withValidValues()
                .withEmailAddress(mockEmailAddress)
                .build();

        // When
        Mockito.doNothing()
                .when(userCreateService)
                .create(Mockito.any(AysUserCreateRequest.class));

        // Then
        String endpoint = BASE_PATH.concat("/user");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockAdminToken.getAccessToken(), mockCreateRequest);

        AysResponse<Void> mockResponse = AysResponseBuilder.success();

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .doesNotExist());

        // Verify
        Mockito.verify(userCreateService, Mockito.times(1))
                .create(Mockito.any(AysUserCreateRequest.class));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "Ankara",
            "İstanbul",
            "Kadıköy",
            "İst",
            "St. Luis",
            "New York City",
            "An.kara",
            "An-kara",
            "An,kara",
            "An-ka.ra",
            "An-ka-ra",
            "An - kara",
            "Anka ra",
            "An .kara",
            "An'kara",
            "An",
            "One morning, when Gregor Samsa woke from troubled dreams, he found himself transformed in his bed"
    })
    void givenUserCreateRequest_whenUserCreatedWithValidCity_thenReturnSuccess(String mockCity) throws Exception {

        // Given
        AysUserCreateRequest mockCreateRequest = new AysUserCreateRequestBuilder()
                .withValidValues()
                .withCity(mockCity)
                .build();

        // When
        Mockito.doNothing()
                .when(userCreateService)
                .create(Mockito.any(AysUserCreateRequest.class));

        // Then
        String endpoint = BASE_PATH.concat("/user");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockAdminToken.getAccessToken(), mockCreateRequest);

        AysResponse<Void> mockResponse = AysResponseBuilder.success();

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .doesNotExist());

        // Verify
        Mockito.verify(userCreateService, Mockito.times(1))
                .create(Mockito.any(AysUserCreateRequest.class));
    }

    @Test
    void givenValidUserCreateRequest_whenUserUnauthorized_thenReturnAccessDeniedException() throws Exception {

        // Given
        AysUserCreateRequest mockCreateRequest = new AysUserCreateRequestBuilder()
                .withValidValues()
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/user");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockUserToken.getAccessToken(), mockCreateRequest);

        AysErrorResponse mockErrorResponse = AysErrorResponseBuilder.FORBIDDEN;
        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isForbidden())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .doesNotExist());

        // Verify
        Mockito.verify(userCreateService, Mockito.never())
                .create(Mockito.any(AysUserCreateRequest.class));
    }

    @Test
    void givenUserCreateRequest_whenRoleIdsAreNull_thenReturnValidationError() throws Exception {

        // Given
        AysUserCreateRequest mockCreateRequest = new AysUserCreateRequestBuilder()
                .withValidValues()
                .withRoleIds(null)
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/user");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockAdminToken.getAccessToken(), mockCreateRequest);

        AysErrorResponse mockErrorResponse = AysErrorResponseBuilder.VALIDATION_ERROR;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isBadRequest())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .isNotEmpty());

        // Verify
        Mockito.verify(userCreateService, Mockito.never())
                .create(Mockito.any(AysUserCreateRequest.class));
    }

    @Test
    void givenUserCreateRequest_whenRoleIdsAreEmpty_thenReturnValidationError() throws Exception {

        // Given
        AysUserCreateRequest mockCreateRequest = new AysUserCreateRequestBuilder()
                .withValidValues()
                .withRoleIds(Set.of())
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/user");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockAdminToken.getAccessToken(), mockCreateRequest);

        AysErrorResponse mockErrorResponse = AysErrorResponseBuilder.VALIDATION_ERROR;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isBadRequest())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .isNotEmpty());

        // Verify
        Mockito.verify(userCreateService, Mockito.never())
                .create(Mockito.any(AysUserCreateRequest.class));
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {
            "",
            "55aed4c4facb4b66bdb5-309eaaef4453"
    })
    void givenUserCreateRequest_whenRoleIdIsNotValid_thenReturnValidationError(String invalidRoleId) throws Exception {

        // Given
        AysUserCreateRequest mockCreateRequest = new AysUserCreateRequestBuilder()
                .withValidValues()
                .withRoleIds(Collections.singleton(invalidRoleId))
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/user");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockAdminToken.getAccessToken(), mockCreateRequest);

        AysErrorResponse mockErrorResponse = AysErrorResponseBuilder.VALIDATION_ERROR;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isBadRequest())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .isNotEmpty());

        // Verify
        Mockito.verify(userCreateService, Mockito.never())
                .create(Mockito.any(AysUserCreateRequest.class));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "",
            " ",
            ".Mehmet",
            ".,-'",
            "Mehmet-",
            "-Mehmet",
            "85263",
            "Osman2",
            "Ahmet&",
            "@sman",
            "Ahm@t",
            "Emin3e",
            "1lgaz",
            "#$½#$£",
            "Ali..Osman",
            "Ahme!",
            "Ayse##Nur",
            "Ahmet  Haşim",
            "Y',lmaz",
            "t",
            "  Ali",
            "Ali  ",
            "Aysel ",
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam In hac habitasse platea dictumst. Nullam in turpis at nunc ultrices.",
    })
    void givenUserCreateRequest_whenFirstNameNotValid_thenReturnValidationError(String mockFirstName) throws Exception {

        // Given
        AysUserCreateRequest mockCreateRequest = new AysUserCreateRequestBuilder()
                .withValidValues()
                .withFirstName(mockFirstName)
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/user");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockAdminToken.getAccessToken(), mockCreateRequest);

        AysErrorResponse mockErrorResponse = AysErrorResponseBuilder.VALIDATION_ERROR;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isBadRequest())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .isNotEmpty());

        // Verify
        Mockito.verify(userCreateService, Mockito.never())
                .create(Mockito.any(AysUserCreateRequest.class));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "",
            " ",
            ".Mehmet",
            ".,-'",
            "Mehmet-",
            "-Mehmet",
            "85263",
            "Osman2",
            "Ahmet&",
            "@sman",
            "Ahm@t",
            "Emin3e",
            "1lgaz",
            "#$½#$£",
            "Ali..Osman",
            "Ahme!",
            "Ayse##Nur",
            "Ahmet  Haşim",
            "Y',lmaz",
            "t",
            "  Ali",
            "Ali  ",
            "Aysel ",
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam In hac habitasse platea dictumst. Nullam in turpis at nunc ultrices.",
    })
    void givenUserCreateRequest_whenLastNameNotValid_thenReturnValidationError(String mockLastName) throws Exception {

        // Given
        AysUserCreateRequest mockCreateRequest = new AysUserCreateRequestBuilder()
                .withValidValues()
                .withLastName(mockLastName)
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/user");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockAdminToken.getAccessToken(), mockCreateRequest);

        AysErrorResponse mockErrorResponse = AysErrorResponseBuilder.VALIDATION_ERROR;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isBadRequest())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .isNotEmpty());

        // Verify
        Mockito.verify(userCreateService, Mockito.never())
                .create(Mockito.any(AysUserCreateRequest.class));
    }

    @CsvSource({
            "country code must be 90, ABC, ABC",
            "country code must be 90, 80, 1234567890",
            "country code must be 90, ABC, ABCDEFGHIJ",
            "country code must be 90, 456786745645, 6546467456435548676845321346656654",
            "line number length must be 10, 90, ABC",
            "line number length must be 10, 90, 123456789",
            "line number length must be 10, 90, 12345678901",
            "must be valid, 90, 1234567890",
            "must be valid, 90, 9104567890",
    })
    @ParameterizedTest
    void givenUserCreateRequest_whenPhoneNumberIsNotValid_thenReturnValidationError(String mockMessage,
                                                                                    String mockCountryCode,
                                                                                    String mockLineNumber) throws Exception {

        // Given
        AysPhoneNumberRequest mockPhoneNumberRequest = new AysPhoneNumberRequestBuilder()
                .withCountryCode(mockCountryCode)
                .withLineNumber(mockLineNumber)
                .build();
        AysUserCreateRequest mockCreateRequest = new AysUserCreateRequestBuilder()
                .withValidValues()
                .withPhoneNumber(mockPhoneNumberRequest)
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/user");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockAdminToken.getAccessToken(), mockCreateRequest);

        AysErrorResponse mockErrorResponse = AysErrorResponseBuilder.VALIDATION_ERROR;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isBadRequest())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.subErrors("[0].message")
                        .value(mockMessage))
                .andExpect(AysMockResultMatchersBuilders.subErrors("[0].field")
                        .value("phoneNumber"))
                .andExpect(AysMockResultMatchersBuilders.subErrors("[0].value")
                        .value(mockPhoneNumberRequest.getCountryCode() + mockPhoneNumberRequest.getLineNumber()));

        // Verify
        Mockito.verify(userCreateService, Mockito.never())
                .create(Mockito.any(AysUserCreateRequest.class));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "a@b.c",
            "plainaddress",
            "@missingusername.com",
            "username@.com",
            "username@gmail",
            "username@gmail..com",
            "username@gmail.c",
            "username@-gmail.com",
            "username@gmail-.com",
            "username@gmail.com.",
            "username@.gmail.com",
            "username@gmail@gmail.com",
            "username(john.doe)@gmail.com",
            "user@domain(comment).com",
            "usernamegmail.com",
            "username@gmail,com",
            "username@gmail space.co",
            "username@gmail..co.uk",
            "user#gmail.com",
            "bekeleandreaevelynirenealexandrascottmirasoniamustafahuivladimirmarcoyolandaraymondakhtermichaeldennistatianayuliyagangmargaretthomassumanjeanamymostafasaidrubenchenedithjumasitimeilucasgaryghulamminaxiaohongmarcosrafaelamyantoniamohamadfatmaahmed@aystest.org"
    })
    void givenUserCreateRequest_whenEmailNotValid_thenReturnValidationError(String mockEmailAddress) throws Exception {

        // Given
        AysUserCreateRequest mockCreateRequest = new AysUserCreateRequestBuilder()
                .withValidValues()
                .withEmailAddress(mockEmailAddress)
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/user");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockAdminToken.getAccessToken(), mockCreateRequest);

        AysErrorResponse mockErrorResponse = AysErrorResponseBuilder.VALIDATION_ERROR;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isBadRequest())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .isNotEmpty());

        // Verify
        Mockito.verify(userCreateService, Mockito.never())
                .create(Mockito.any(AysUserCreateRequest.class));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "Anka..ra",
            "Ankara-",
            "Ankara,",
            "Ank@ra",
            "12365",
            "Ankara06",
            "Bur#sa",
            "26Eskisehir",
            " ",
            " Kastamonu",
            "Antalya ",
            "A",
            "One morning, when Gregor Samsa woke from troubled dreams, he found himself transformed in his bed into"
    })
    void givenUserCreateRequest_whenCityNotValid_thenReturnValidationError(String mockCity) throws Exception {

        // Given
        AysUserCreateRequest mockCreateRequest = new AysUserCreateRequestBuilder()
                .withValidValues()
                .withCity(mockCity)
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/user");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockAdminToken.getAccessToken(), mockCreateRequest);

        AysErrorResponse mockErrorResponse = AysErrorResponseBuilder.VALIDATION_ERROR;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isBadRequest())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .isNotEmpty());

        // Verify
        Mockito.verify(userCreateService, Mockito.never())
                .create(Mockito.any(AysUserCreateRequest.class));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "Su",
            "Çağla",
            "Robert William Floyd",
            "Dr. Ahmet",
            "Ahmet-Mehmet",
            "Ahmet - Mehmet",
            "O'Connor",
            "ya",
            "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean"
    })
    void givenValidIdAndUserUpdateRequest_whenUserUpdatedWithValidFirstNameAndLastName_thenReturnSuccess(String mockValidName) throws Exception {

        // Given
        String mockId = "2cb9f39b-490f-4035-97ac-9afbb87506df";
        AysUserUpdateRequest mockUpdateRequest = new AysUserUpdateRequestBuilder()
                .withValidValues()
                .withFirstName(mockValidName)
                .withLastName(mockValidName)
                .build();

        // When
        Mockito.doNothing()
                .when(userUpdateService)
                .update(Mockito.any(), Mockito.any(AysUserUpdateRequest.class));

        // Then
        String endpoint = BASE_PATH.concat("/user/").concat(mockId);
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .put(endpoint, mockAdminToken.getAccessToken(), mockUpdateRequest);

        AysResponse<Void> mockResponse = AysResponseBuilder.success();

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .doesNotExist());

        // Verify
        Mockito.verify(userUpdateService, Mockito.times(1))
                .update(Mockito.anyString(), Mockito.any(AysUserUpdateRequest.class));
    }


    @ParameterizedTest
    @ValueSource(strings = {
            "a@b.co",
            "abcdef@mail.com",
            "abc+def@archive.com",
            "john.doe123@example.co.uk",
            "admin_123@example.org",
            "admin-test@ays.com",
            "johndoe@gmail.com",
            "janedoe123@yahoo.com",
            "michael.jordan@nba.com",
            "alice.smith@company.co.uk",
            "info@mywebsite.org",
            "support@helpdesk.net",
            "rajeshmehmetjosephanastasiyahamidjianguonalalitachunoscarmanojfelixmichaelhugoaslambeatrizsergeyemmaricardohenrymunnigaryrobertorosehungabdullahramaisaaclijunxinchonadiaqiangyuliyabrendapauljeanlyubovpablogiuseppexuanchaosimakevinminlongperez@aystest.org"
    })
    void givenValidIdAndUserUpdateRequest_whenUserUpdated_thenReturnSuccess(String mockEmailAddress) throws Exception {

        // Given
        String mockId = "2cb9f39b-490f-4035-97ac-9afbb87506df";
        AysUserUpdateRequest mockUpdateRequest = new AysUserUpdateRequestBuilder()
                .withValidValues()
                .withEmailAddress(mockEmailAddress)
                .build();

        // When
        Mockito.doNothing()
                .when(userUpdateService)
                .update(Mockito.any(), Mockito.any(AysUserUpdateRequest.class));

        // Then
        String endpoint = BASE_PATH.concat("/user/").concat(mockId);
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .put(endpoint, mockAdminToken.getAccessToken(), mockUpdateRequest);

        AysResponse<Void> mockResponse = AysResponseBuilder.success();

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .doesNotExist());

        // Verify
        Mockito.verify(userUpdateService, Mockito.times(1))
                .update(Mockito.anyString(), Mockito.any(AysUserUpdateRequest.class));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "Ankara",
            "İstanbul",
            "Kadıköy",
            "İst",
            "St. Luis",
            "New York City",
            "An.kara",
            "An-kara",
            "An,kara",
            "An-ka.ra",
            "An-ka-ra",
            "An - kara",
            "Anka ra",
            "An .kara",
            "An'kara",
            "An",
            "One morning, when Gregor Samsa woke from troubled dreams, he found himself transformed in his bed"
    })
    void givenValidIdAndUserUpdateRequest_whenUserUpdatedWithValidCity_thenReturnSuccess(String mockCity) throws Exception {

        // Given
        String mockId = "2cb9f39b-490f-4035-97ac-9afbb87506df";
        AysUserUpdateRequest mockUpdateRequest = new AysUserUpdateRequestBuilder()
                .withValidValues()
                .withCity(mockCity)
                .build();

        // When
        Mockito.doNothing()
                .when(userUpdateService)
                .update(Mockito.any(), Mockito.any(AysUserUpdateRequest.class));

        // Then
        String endpoint = BASE_PATH.concat("/user/")
                .concat(mockId);
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .put(endpoint, mockAdminToken.getAccessToken(), mockUpdateRequest);

        AysResponse<Void> mockResponse = AysResponseBuilder.success();

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .doesNotExist());

        // Verify
        Mockito.verify(userUpdateService)
                .update(Mockito.anyString(), Mockito.any(AysUserUpdateRequest.class));
    }

    @Test
    void givenValidIdAndUserUpdateRequest_whenUserUnauthorized_thenReturnAccessDeniedException() throws Exception {

        // Given
        String mockId = "0deb5f16-479f-4625-9354-7db29c861e85";
        AysUserUpdateRequest mockUpdateRequest = new AysUserUpdateRequestBuilder()
                .withValidValues()
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/user/").concat(mockId);
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .put(endpoint, mockUserToken.getAccessToken(), mockUpdateRequest);

        AysErrorResponse mockErrorResponse = AysErrorResponseBuilder.FORBIDDEN;
        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isForbidden())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .doesNotExist());

        // Verify
        Mockito.verify(userUpdateService, Mockito.never())
                .update(Mockito.anyString(), Mockito.any(AysUserUpdateRequest.class));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "A",
            "493268349068342"
    })
    void givenInvalidIdAndValidUserUpdateRequest_whenIdNotValid_thenReturnValidationError(String invalidId) throws Exception {

        // Given
        AysUserUpdateRequest mockUpdateRequest = new AysUserUpdateRequestBuilder()
                .withValidValues()
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/user/").concat(invalidId);
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .put(endpoint, mockAdminToken.getAccessToken(), mockUpdateRequest);

        AysErrorResponse mockErrorResponse = AysErrorResponseBuilder.VALIDATION_ERROR;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isBadRequest())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .isNotEmpty());

        // Verify
        Mockito.verify(userUpdateService, Mockito.never())
                .update(Mockito.anyString(), Mockito.any(AysUserUpdateRequest.class));
    }

    @Test
    void givenValidIdAndInvalidUserUpdateRequest_whenRoleIdsAreNull_thenReturnValidationError() throws Exception {

        // Given
        String mockId = "3b5a65ba-c90e-41ad-97ec-56c7dd4fe708";
        AysUserUpdateRequest mockUpdateRequest = new AysUserUpdateRequestBuilder()
                .withValidValues()
                .withRoleIds(null)
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/user/").concat(mockId);
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .put(endpoint, mockAdminToken.getAccessToken(), mockUpdateRequest);

        AysErrorResponse mockErrorResponse = AysErrorResponseBuilder.VALIDATION_ERROR;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isBadRequest())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .isNotEmpty());

        // Verify
        Mockito.verify(userUpdateService, Mockito.never())
                .update(Mockito.anyString(), Mockito.any(AysUserUpdateRequest.class));
    }

    @CsvSource({
            "country code must be 90, ABC, ABC",
            "country code must be 90, 80, 1234567890",
            "country code must be 90, ABC, ABCDEFGHIJ",
            "country code must be 90, 456786745645, 6546467456435548676845321346656654",
            "line number length must be 10, 90, ABC",
            "line number length must be 10, 90, 123456789",
            "line number length must be 10, 90, 12345678901",
            "must be valid, 90, 1234567890",
            "must be valid, 90, 9104567890",
    })
    @ParameterizedTest
    void givenValidIdAndUserUpdateRequest_whenPhoneNumberIsNotValid_thenReturnValidationError(String mockMessage,
                                                                                              String mockCountryCode,
                                                                                              String mockLineNumber) throws Exception {

        // Given
        String mockId = "0498acd6-103e-4e72-b6a5-7584bb1c7c93";
        AysPhoneNumberRequest mockPhoneNumberRequest = new AysPhoneNumberRequestBuilder()
                .withCountryCode(mockCountryCode)
                .withLineNumber(mockLineNumber)
                .build();
        AysUserUpdateRequest mockUpdateRequest = new AysUserUpdateRequestBuilder()
                .withValidValues()
                .withPhoneNumber(mockPhoneNumberRequest)
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/user/").concat(mockId);
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .put(endpoint, mockAdminToken.getAccessToken(), mockUpdateRequest);

        AysErrorResponse mockErrorResponse = AysErrorResponseBuilder.VALIDATION_ERROR;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isBadRequest())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.subErrors("[0].message")
                        .value(mockMessage))
                .andExpect(AysMockResultMatchersBuilders.subErrors("[0].field")
                        .value("phoneNumber"))
                .andExpect(AysMockResultMatchersBuilders.subErrors("[0].value")
                        .value(mockPhoneNumberRequest.getCountryCode() + mockPhoneNumberRequest.getLineNumber()));

        // Verify
        Mockito.verify(userUpdateService, Mockito.never())
                .update(Mockito.anyString(), Mockito.any(AysUserUpdateRequest.class));
    }

    @Test
    void givenValidIdAndInvalidUserUpdateRequest_whenRoleIdsAreEmpty_thenReturnValidationError() throws Exception {

        // Given
        String mockId = "daaa28f9-f7b3-4f43-a10f-a5fe2b0eb2fa";
        AysUserUpdateRequest mockUpdateRequest = new AysUserUpdateRequestBuilder()
                .withValidValues()
                .withRoleIds(Set.of())
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/user/").concat(mockId);
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .put(endpoint, mockAdminToken.getAccessToken(), mockUpdateRequest);

        AysErrorResponse mockErrorResponse = AysErrorResponseBuilder.VALIDATION_ERROR;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isBadRequest())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .isNotEmpty());

        // Verify
        Mockito.verify(userUpdateService, Mockito.never())
                .update(Mockito.anyString(), Mockito.any(AysUserUpdateRequest.class));
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {
            "",
            "55aed4c4facb4b66bdb5-309eaaef4453"
    })
    void givenValidIdAndInvalidUserUpdateRequest_whenRoleIdIsNotValid_thenReturnValidationError(String invalidRoleId) throws Exception {

        // Given
        String mockId = "b8758dce-ad8e-438d-b31c-b440b352068a";

        Set<String> mockRoleIds = new HashSet<>();
        mockRoleIds.add(invalidRoleId);
        AysUserUpdateRequest mockUpdateRequest = new AysUserUpdateRequestBuilder()
                .withValidValues()
                .withRoleIds(mockRoleIds)
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/user/").concat(mockId);
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .put(endpoint, mockAdminToken.getAccessToken(), mockUpdateRequest);

        AysErrorResponse mockErrorResponse = AysErrorResponseBuilder.VALIDATION_ERROR;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isBadRequest())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .isNotEmpty());

        // Verify
        Mockito.verify(userUpdateService, Mockito.never())
                .update(Mockito.anyString(), Mockito.any(AysUserUpdateRequest.class));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "",
            " ",
            ".Mehmet",
            ".,-'",
            "Mehmet-",
            "-Mehmet",
            "85263",
            "Osman2",
            "Ahmet&",
            "@sman",
            "Ahm@t",
            "Emin3e",
            "1lgaz",
            "#$½#$£",
            "Ali..Osman",
            "Ahme!",
            "Ayse##Nur",
            "Ahmet  Haşim",
            "Y',lmaz",
            "t",
            "  Ali",
            "Ali  ",
            "Aysel ",
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam In hac habitasse platea dictumst. Nullam in turpis at nunc ultrices.",
    })
    void givenValidIdAndInvalidUserUpdateRequest_whenFirstNameNotValid_thenReturnValidationError(String mockFirstName) throws Exception {
        // Given
        String mockId = "b66afcb0-3029-4968-a87b-e1d94fc75642";

        AysUserUpdateRequest mockUpdateRequest = new AysUserUpdateRequestBuilder()
                .withValidValues()
                .withFirstName(mockFirstName)
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/user/").concat(mockId);
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .put(endpoint, mockAdminToken.getAccessToken(), mockUpdateRequest);

        AysErrorResponse mockErrorResponse = AysErrorResponseBuilder.VALIDATION_ERROR;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isBadRequest())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .isNotEmpty());

        // Verify
        Mockito.verify(userUpdateService, Mockito.never())
                .update(Mockito.anyString(), Mockito.any(AysUserUpdateRequest.class));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "",
            " ",
            ".Mehmet",
            ".,-'",
            "Mehmet-",
            "-Mehmet",
            "85263",
            "Osman2",
            "Ahmet&",
            "@sman",
            "Ahm@t",
            "Emin3e",
            "1lgaz",
            "#$½#$£",
            "Ali..Osman",
            "Ahme!",
            "Ayse##Nur",
            "Ahmet  Haşim",
            "Y',lmaz",
            "t",
            "  Ali",
            "Ali  ",
            "Aysel ",
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam In hac habitasse platea dictumst. Nullam in turpis at nunc ultrices.",
    })
    void givenValidIdAndInvalidUserUpdateRequest_whenLastNameNotValid_thenReturnValidationError(String mockLastName) throws Exception {
        // Given
        String mockId = "b66afcb0-3029-4968-a87b-e1d94fc75642";

        AysUserUpdateRequest mockUpdateRequest = new AysUserUpdateRequestBuilder()
                .withValidValues()
                .withLastName(mockLastName)
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/user/").concat(mockId);
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .put(endpoint, mockAdminToken.getAccessToken(), mockUpdateRequest);

        AysErrorResponse mockErrorResponse = AysErrorResponseBuilder.VALIDATION_ERROR;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isBadRequest())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .isNotEmpty());

        // Verify
        Mockito.verify(userUpdateService, Mockito.never())
                .update(Mockito.anyString(), Mockito.any(AysUserUpdateRequest.class));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "a@b.c",
            "plainaddress",
            "@missingusername.com",
            "username@.com",
            "username@gmail",
            "username@gmail..com",
            "username@gmail.c",
            "username@-gmail.com",
            "username@gmail-.com",
            "username@gmail.com.",
            "username@.gmail.com",
            "username@gmail@gmail.com",
            "username(john.doe)@gmail.com",
            "user@domain(comment).com",
            "usernamegmail.com",
            "username@gmail,com",
            "username@gmail space.co",
            "username@gmail..co.uk",
            "user#gmail.com",
            "bekeleandreaevelynirenealexandrascottmirasoniamustafahuivladimirmarcoyolandaraymondakhtermichaeldennistatianayuliyagangmargaretthomassumanjeanamymostafasaidrubenchenedithjumasitimeilucasgaryghulamminaxiaohongmarcosrafaelamyantoniamohamadfatmaahmed@aystest.org"
    })
    void givenValidIdAndInvalidUserUpdateRequest_whenEmailNotValid_thenReturnValidationError(String mockEmailAddress) throws Exception {
        // Given
        String mockId = "b66afcb0-3029-4968-a87b-e1d94fc75642";

        AysUserUpdateRequest mockUpdateRequest = new AysUserUpdateRequestBuilder()
                .withValidValues()
                .withEmailAddress(mockEmailAddress)
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/user/").concat(mockId);
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .put(endpoint, mockAdminToken.getAccessToken(), mockUpdateRequest);

        AysErrorResponse mockErrorResponse = AysErrorResponseBuilder.VALIDATION_ERROR;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isBadRequest())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .isNotEmpty());

        // Verify
        Mockito.verify(userUpdateService, Mockito.never())
                .update(Mockito.anyString(), Mockito.any(AysUserUpdateRequest.class));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "Anka..ra",
            "Ankara-",
            "Ankara,",
            "Ank@ra",
            "12365",
            "Ankara06",
            "Bur#sa",
            "26Eskisehir",
            " ",
            " Kastamonu",
            "Antalya ",
            "A",
            "One morning, when Gregor Samsa woke from troubled dreams , he found himself transformed in his bed into"
    })
    void givenValidIdAndInvalidUserUpdateRequest_whenCityNotValid_thenReturnValidationError(String mockCity) throws Exception {
        // Given
        String mockId = "b66afcb0-3029-4968-a87b-e1d94fc75642";

        AysUserUpdateRequest mockUpdateRequest = new AysUserUpdateRequestBuilder()
                .withValidValues()
                .withCity(mockCity)
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/user/")
                .concat(mockId);
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .put(endpoint, mockAdminToken.getAccessToken(), mockUpdateRequest);

        AysErrorResponse mockErrorResponse = AysErrorResponseBuilder.VALIDATION_ERROR;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isBadRequest())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .isNotEmpty());

        // Verify
        Mockito.verify(userUpdateService, Mockito.never())
                .update(Mockito.anyString(), Mockito.any(AysUserUpdateRequest.class));
    }

    @Test
    void givenValidId_whenActivateUser_thenReturnSuccess() throws Exception {

        // Given
        String mockId = "793fcc5d-31cc-4704-9f0a-627ac7da517d";

        // When
        Mockito.doNothing()
                .when(userUpdateService)
                .activate(Mockito.anyString());

        // Then
        String endpoint = BASE_PATH.concat("/user/").concat(mockId).concat("/activate");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .patch(endpoint, mockAdminToken.getAccessToken());

        AysResponse<Void> mockResponse = AysResponseBuilder.success();

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .doesNotExist());

        // Verify
        Mockito.verify(userUpdateService, Mockito.times(1))
                .activate(Mockito.anyString());
    }

    @Test
    void givenValidId_whenUserUnauthorized_thenReturnAccessDeniedException() throws Exception {

        // Given
        String mockId = "201aec72-ecd8-49fc-86f5-2b5458871edb";

        // Then
        String endpoint = BASE_PATH.concat("/user/").concat(mockId).concat("/activate");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .patch(endpoint, mockUserToken.getAccessToken());

        AysErrorResponse mockErrorResponse = AysErrorResponseBuilder.FORBIDDEN;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isForbidden())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .doesNotExist());

        // Verify
        Mockito.verify(userUpdateService, Mockito.never())
                .activate(Mockito.anyString());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "A",
            "493268349068342"
    })
    void givenInvalidUserId_whenIdNotValid_thenReturnValidationError(String invalidId) throws Exception {

        // Then
        String endpoint = BASE_PATH.concat("/user/").concat(invalidId).concat("/activate");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .patch(endpoint, mockAdminToken.getAccessToken());

        AysErrorResponse mockErrorResponse = AysErrorResponseBuilder.VALIDATION_ERROR;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isBadRequest())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .isNotEmpty());

        // Verify
        Mockito.verify(userUpdateService, Mockito.never())
                .activate(Mockito.anyString());
    }


    @Test
    void givenValidId_whenUserDeleted_thenReturnSuccess() throws Exception {

        // Given
        String mockId = "2e574ecf-929c-4923-8aea-d061d29934da";

        // When
        Mockito.doNothing()
                .when(userUpdateService)
                .delete(Mockito.any());

        // Then
        String endpoint = BASE_PATH.concat("/user/").concat(mockId);
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .delete(endpoint, mockAdminToken.getAccessToken());

        AysResponse<Void> mockResponse = AysResponseBuilder.success();

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .doesNotExist());

        // Verify
        Mockito.verify(userUpdateService, Mockito.times(1))
                .delete(mockId);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "A",
            "493268349068342"
    })
    void givenId_whenIdDoesNotValid_thenReturnValidationError(String invalidId) throws Exception {

        // Then
        String endpoint = BASE_PATH.concat("/user/").concat(invalidId);
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .delete(endpoint, mockAdminToken.getAccessToken());

        AysErrorResponse mockErrorResponse = AysErrorResponseBuilder.VALIDATION_ERROR;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isBadRequest())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .isNotEmpty());

        // Verify
        Mockito.verify(userUpdateService, Mockito.never())
                .delete(Mockito.anyString());
    }

    @Test
    void givenUserDelete_whenUserUnauthorized_thenReturnAccessDeniedException() throws Exception {

        // Given
        String mockId = "45082f52-011b-41d1-b4bd-6eba4e1f1ea8";

        // Then
        String endpoint = BASE_PATH.concat("/user/").concat(mockId);
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .delete(endpoint, mockUserToken.getAccessToken());

        AysErrorResponse mockErrorResponse = AysErrorResponseBuilder.FORBIDDEN;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isForbidden())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .doesNotExist());

        // Verify
        Mockito.verify(userUpdateService, Mockito.never())
                .delete(Mockito.anyString());
    }


    @Test
    void givenValidId_whenPassivateUser_thenReturnSuccess() throws Exception {

        // Given
        String mockId = "894dcc5d-31cc-4704-9f0a-627ac7da517d";

        // When
        Mockito.doNothing()
                .when(userUpdateService)
                .passivate(Mockito.anyString());

        // Then
        String endpoint = BASE_PATH.concat("/user/").concat(mockId).concat("/passivate");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .patch(endpoint, mockAdminToken.getAccessToken());

        AysResponse<Void> mockResponse = AysResponseBuilder.success();

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .doesNotExist());

        // Verify
        Mockito.verify(userUpdateService, Mockito.times(1))
                .passivate(Mockito.anyString());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "A",
            "493268349068342"
    })
    void givenInvalidUserId_whenIdNotValid_thenReturnValidationErrorForPassivate(String invalidId) throws Exception {

        // Then
        String endpoint = BASE_PATH.concat("/user/").concat(invalidId).concat("/passivate");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .patch(endpoint, mockAdminToken.getAccessToken());

        AysErrorResponse mockErrorResponse = AysErrorResponseBuilder.VALIDATION_ERROR;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isBadRequest())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .isNotEmpty());

        // Verify
        Mockito.verify(userUpdateService, Mockito.never())
                .passivate(Mockito.anyString());
    }

    @Test
    void givenValidUserId_whenUserUnauthorized_thenReturnAccessDeniedException() throws Exception {

        // Given
        String mockId = "333aec72-ecd8-49fc-86f5-2b5458871edb";

        // Then
        String endpoint = BASE_PATH.concat("/user/").concat(mockId).concat("/passivate");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .patch(endpoint, mockUserToken.getAccessToken());

        AysErrorResponse mockErrorResponse = AysErrorResponseBuilder.FORBIDDEN;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isForbidden())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .doesNotExist());

        // Verify
        Mockito.verify(userUpdateService, Mockito.never())
                .passivate(Mockito.anyString());
    }
}
