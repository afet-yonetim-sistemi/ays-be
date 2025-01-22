package org.ays.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.ays.AysUnitTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

@Slf4j
@SuppressWarnings("java:S5976")
class AysMaskUtilTest extends AysUnitTest {

    @Test
    void givenValidJsonWithAccessTokenAndRefreshToken_whenMasked_thenOnlyFirst20CharsRemainUnmasked() throws JsonProcessingException {

        // Given
        String mockRawJson = """
                {
                  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c",
                  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c"
                }
                """;

        // When
        JsonNode jsonNode = new ObjectMapper().readTree(mockRawJson);
        AysMaskUtil.mask(jsonNode);

        // Then
        String mockMaskedJson = jsonNode.toString();

        log.info("Raw JSON: {}", mockRawJson);
        log.info("Masked JSON: {}", mockMaskedJson);

        Assertions.assertTrue(mockMaskedJson.contains("\"accessToken\":\"eyJhbGciOiJIUzI1NiIs******\""));
        Assertions.assertTrue(mockMaskedJson.contains("\"refreshToken\":\"eyJhbGciOiJIUzI1NiIs******\""));
    }

    @Test
    void givenValidJsonWith20CharsAccessTokenAnd20CharRefreshToken_whenMasked_thenOnlyFirst20CharsRemainUnmasked() throws JsonProcessingException {

        // Given
        String mockRawJson = """
                {
                  "accessToken": "eyJhbGciOiJIUzI1NiIs",
                  "refreshToken": "eyJhbGciOiJIUzI1NiIs"
                }
                """;

        // When
        JsonNode jsonNode = new ObjectMapper().readTree(mockRawJson);
        AysMaskUtil.mask(jsonNode);

        // Then
        String mockMaskedJson = jsonNode.toString();

        log.info("Raw JSON: {}", mockRawJson);
        log.info("Masked JSON: {}", mockMaskedJson);

        Assertions.assertTrue(mockMaskedJson.contains("\"accessToken\":\"eyJhbGciOiJIUzI1NiIs\""));
        Assertions.assertTrue(mockMaskedJson.contains("\"refreshToken\":\"eyJhbGciOiJIUzI1NiIs\""));
    }


    @Test
    void givenValidJsonWithPassword_whenMasked_thenAllCharsAreMasked() throws JsonProcessingException {

        // Given
        String mockRawJson = """
                {
                  "password": "123456789"
                }
                """;

        // When
        JsonNode jsonNode = new ObjectMapper().readTree(mockRawJson);
        AysMaskUtil.mask(jsonNode);

        // Then
        String mockMaskedJson = jsonNode.toString();

        log.info("Raw JSON: {}", mockRawJson);
        log.info("Masked JSON: {}", mockMaskedJson);

        Assertions.assertTrue(mockMaskedJson.contains("\"password\":\"******\""));
    }


    @Test
    void givenValidJsonWith32CharsEmailAddress_whenMasked_thenOnlyMaskFirstAndLast3CharsRemainUnmasked() throws JsonProcessingException {

        // Given
        String mockRawJson = """
                {
                  "emailAddress": "john.doe@afetyonetimsistemi.test"
                }
                """;

        // When
        JsonNode jsonNode = new ObjectMapper().readTree(mockRawJson);
        AysMaskUtil.mask(jsonNode);

        // Then
        String mockMaskedJson = jsonNode.toString();

        log.info("Raw JSON: {}", mockRawJson);
        log.info("Masked JSON: {}", mockMaskedJson);

        Assertions.assertTrue(mockMaskedJson.contains("\"emailAddress\":\"joh******est\""));
    }

    @Test
    void givenValidJsonWith3CharsEmailAddress_whenMasked_thenRemainsUnmasked() throws JsonProcessingException {

        // Given
        String mockRawJson = """
                {
                  "emailAddress": "j@t"
                }
                """;

        // When
        JsonNode jsonNode = new ObjectMapper().readTree(mockRawJson);
        AysMaskUtil.mask(jsonNode);

        // Then
        String mockMaskedJson = jsonNode.toString();

        log.info("Raw JSON: {}", mockRawJson);
        log.info("Masked JSON: {}", mockMaskedJson);

        Assertions.assertTrue(mockMaskedJson.contains("\"emailAddress\":\"j@t\""));
    }


    @Test
    void givenValidJsonWith18CharsAddress_whenMasked_thenOnlyMaskFirstAndLast3CharsRemainUnmasked() throws JsonProcessingException {

        // Given
        String mockRawJson = """
                {
                  "address": "Springfield Street"
                }
                """;

        // When
        JsonNode jsonNode = new ObjectMapper().readTree(mockRawJson);
        AysMaskUtil.mask(jsonNode);

        // Then
        String mockMaskedJson = jsonNode.toString();

        log.info("Raw JSON: {}", mockRawJson);
        log.info("Masked JSON: {}", mockMaskedJson);

        Assertions.assertTrue(mockMaskedJson.contains("\"address\":\"S******\""));
    }

    @Test
    void givenValidJsonWith28CharsAddress_whenMasked_thenOnlyMaskFirstAndLast5CharsRemainUnmasked() throws JsonProcessingException {

        // Given
        String mockRawJson = """
                {
                  "address": "123 Main Street, Springfield"
                }
                """;

        // When
        JsonNode jsonNode = new ObjectMapper().readTree(mockRawJson);
        AysMaskUtil.mask(jsonNode);

        // Then
        String mockMaskedJson = jsonNode.toString();

        log.info("Raw JSON: {}", mockRawJson);
        log.info("Masked JSON: {}", mockMaskedJson);

        Assertions.assertTrue(mockMaskedJson.contains("\"address\":\"123 M******field\""));
    }


    @Test
    void givenValidJsonWith10CharsLineNumber_whenMasked_thenLast4CharsOfLineNumberAreUnmasked() throws JsonProcessingException {

        // Given
        String mockRawJson = """
                {
                  "phoneNumber": {
                    "countryCode": "90",
                    "lineNumber": "5012345678"
                  }
                }
                """;

        // When
        JsonNode jsonNode = new ObjectMapper().readTree(mockRawJson);
        AysMaskUtil.mask(jsonNode);

        // Then
        String mockMaskedJson = jsonNode.toString();

        log.info("Raw JSON: {}", mockRawJson);
        log.info("Masked JSON: {}", mockMaskedJson);

        Assertions.assertTrue(mockMaskedJson.contains("\"phoneNumber\":{\"countryCode\":\"90\",\"lineNumber\":\"******5678\"}"));
    }

    @Test
    void givenValidJsonWith4CharsLineNumber_whenMasked_thenLineNumberRemainsUnmasked() throws JsonProcessingException {

        // Given
        String mockRawJson = """
                {
                  "phoneNumber": {
                    "countryCode": "90",
                    "lineNumber": "5678"
                  }
                }
                """;

        // When
        JsonNode jsonNode = new ObjectMapper().readTree(mockRawJson);
        AysMaskUtil.mask(jsonNode);

        // Then
        String mockMaskedJson = jsonNode.toString();

        log.info("Raw JSON: {}", mockRawJson);
        log.info("Masked JSON: {}", mockMaskedJson);

        Assertions.assertTrue(mockMaskedJson.contains("\"phoneNumber\":{\"countryCode\":\"90\",\"lineNumber\":\"5678\"}"));
    }


    @Test
    void givenValidJsonWithFirstNameAndLastName_whenMasked_thenOnlyFirstCharRemainsUnmasked() throws JsonProcessingException {

        // Given
        String mockRawJson = """
                {
                  "firstName": "John",
                  "lastName": "Doe",
                  "applicantFirstName": "Jane",
                  "applicantLastName": "Doe"
                }
                """;

        // When
        JsonNode jsonNode = new ObjectMapper().readTree(mockRawJson);
        AysMaskUtil.mask(jsonNode);

        // Then
        String mockMaskedJson = jsonNode.toString();

        log.info("Raw JSON: {}", mockRawJson);
        log.info("Masked JSON: {}", mockMaskedJson);

        Assertions.assertTrue(mockMaskedJson.contains("\"firstName\":\"J******\""));
        Assertions.assertTrue(mockMaskedJson.contains("\"lastName\":\"D******\""));
        Assertions.assertTrue(mockMaskedJson.contains("\"applicantFirstName\":\"J******\""));
        Assertions.assertTrue(mockMaskedJson.contains("\"applicantLastName\":\"D******\""));
    }

    @Test
    void givenValidJsonWith1CharFirstNameAnd1CharLastName_whenMasked_thenFirstNameAndLastNameRemainsUnmasked() throws JsonProcessingException {

        // Given
        String mockRawJson = """
                {
                  "firstName": "J",
                  "lastName": "D",
                  "applicantFirstName": "J",
                  "applicantLastName": "D"
                }
                """;

        // When
        JsonNode jsonNode = new ObjectMapper().readTree(mockRawJson);
        AysMaskUtil.mask(jsonNode);

        // Then
        String mockMaskedJson = jsonNode.toString();

        log.info("Raw JSON: {}", mockRawJson);
        log.info("Masked JSON: {}", mockMaskedJson);

        Assertions.assertTrue(mockMaskedJson.contains("\"firstName\":\"J\""));
        Assertions.assertTrue(mockMaskedJson.contains("\"lastName\":\"D\""));
        Assertions.assertTrue(mockMaskedJson.contains("\"applicantFirstName\":\"J\""));
        Assertions.assertTrue(mockMaskedJson.contains("\"applicantLastName\":\"D\""));
    }


    @Test
    void givenValidJsonWithArray_whenMasked_thenRestrictedFieldsMasked() throws JsonProcessingException {

        // Given
        String mockRawJson = """
                {
                    "time": "2025-01-04T07:40:40.852764358",
                    "code": "afdc5f5b-65b0-4870-9f94-8ceb8c3b926d",
                    "isSuccess": true,
                    "response": {
                        "content": [
                            {
                                "id": "000342f7-2fcd-46fd-a184-a2f077a64da5",
                                "referenceNumber": "5302137178",
                                "firstName": "Refugio",
                                "lastName": "Kilback",
                                "phoneNumber": {
                                    "countryCode": "90",
                                    "lineNumber": "2627881496"
                                },
                                "applicantFirstName": "Geraldo",
                                "applicantLastName": "Muller",
                                "applicantPhoneNumber": {
                                    "countryCode": "90",
                                    "lineNumber": "5312621398"
                                },
                                "seatingCount": 986,
                                "status": "PENDING",
                                "isInPerson": false,
                                "createdAt": "2024-12-04T19:15:35"
                            }
                        ]
                    }
                }
                """;

        // When
        JsonNode jsonNode = new ObjectMapper().readTree(mockRawJson);
        AysMaskUtil.mask(jsonNode);

        // Then
        String mockMaskedJson = jsonNode.toString();

        log.info("Raw JSON: {}", mockRawJson);
        log.info("Masked JSON: {}", mockMaskedJson);

        Assertions.assertTrue(mockMaskedJson.contains("\"firstName\":\"R******\""));
        Assertions.assertTrue(mockMaskedJson.contains("\"lastName\":\"K******\""));
        Assertions.assertTrue(mockMaskedJson.contains("\"lineNumber\":\"******1496\""));
        Assertions.assertTrue(mockMaskedJson.contains("\"applicantFirstName\":\"G******\""));
        Assertions.assertTrue(mockMaskedJson.contains("\"applicantLastName\":\"M******\""));
        Assertions.assertTrue(mockMaskedJson.contains("\"lineNumber\":\"******1398\""));
    }


    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {
            "",
            "null"
    })
    void givenFieldAndValue_whenValueIsNotValidToMask_thenReturnValue(String mockValue) {

        // Given
        String mockField = "mockField";

        // Then
        String maskedValue = AysMaskUtil.mask(mockField, mockValue);

        Assertions.assertEquals(mockValue, maskedValue);
    }

}
