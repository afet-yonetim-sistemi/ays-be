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
                  "password": "123456789",
                  "passwordRepeat": "123456789"
                }
                """;

        // When
        JsonNode jsonNode = new ObjectMapper().readTree(mockRawJson);
        AysMaskUtil.mask(jsonNode);

        // Then
        String mockMaskedJson = jsonNode.toString();

        log.info("Raw JSON: {}", mockRawJson);
        log.info("Masked JSON: {}", mockMaskedJson);

        Assertions.assertTrue(mockMaskedJson.contains("\"password\":\"******\","));
        Assertions.assertTrue(mockMaskedJson.contains("\"passwordRepeat\":\"******\""));
    }

    @Test
    void givenValidValidationErrorJsonWith130CharsInvalidPassword_whenMasked_thenAllCharsAreMasked() throws JsonProcessingException {

        // Given
        String mockRawJson = """
                {
                    "time": "2025-01-22T14:53:23.238373",
                    "code": "fd940bff-81a4-4249-a23e-04e47d119a8e",
                    "header": "VALIDATION ERROR",
                    "isSuccess": false,
                    "subErrors": [
                        {
                            "message": "size must be between 8 and 128 characters.",
                            "field": "password",
                            "value": "NatoquevitaeQuisqueornareSapiensenectusPercrasVulputateorciUrnfendacTemporvariusCuraebibendumVitaeacLiberomorbiIaculisinceptosEteu",
                            "type": "String"
                        }
                    ]
                }
                """;

        // When
        JsonNode jsonNode = new ObjectMapper().readTree(mockRawJson);
        AysMaskUtil.mask(jsonNode);

        // Then
        String mockMaskedJson = jsonNode.toString();

        log.info("Raw JSON: {}", mockRawJson);
        log.info("Masked JSON: {}", mockMaskedJson);

        Assertions.assertTrue(mockMaskedJson.contains("\"field\":\"password\",\"value\":\"******\""));
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
    void givenValidValidationErrorJsonWith34CharsInvalidEmailAddress_whenMasked_thenOnlyMaskFirstAndLast3CharsRemainUnmasked() throws JsonProcessingException {

        // Given
        String mockRawJson = """
                {
                    "time": "2025-01-22T14:51:15.825443",
                    "code": "69b57985-57bd-4fe0-9acd-b0a271b1637e",
                    "header": "VALIDATION ERROR",
                    "isSuccess": false,
                    "subErrors": [
                        {
                            "message": "must be valid",
                            "field": "emailAddress",
                            "value": "ahmet.mehmetafetyonetimsistemi.org",
                            "type": "String"
                        }
                    ]
                }
                """;

        // When
        JsonNode jsonNode = new ObjectMapper().readTree(mockRawJson);
        AysMaskUtil.mask(jsonNode);

        // Then
        String mockMaskedJson = jsonNode.toString();

        log.info("Raw JSON: {}", mockRawJson);
        log.info("Masked JSON: {}", mockMaskedJson);

        Assertions.assertTrue(mockMaskedJson.contains("\"field\":\"emailAddress\",\"value\":\"ahm******org\""));
    }

    @Test
    void givenValidConflictErrorJsonWithExistingEmailAddress_whenMasked_thenOnlyMaskFirstAndLast3CharsRemainUnmasked() throws JsonProcessingException {

        // Given
        String mockRawJson = """
                {
                    "time": "2025-01-25T01:36:50.785086214",
                    "code": "e9865a6d-ad22-4d07-927d-5b7e8121881f",
                    "header": "CONFLICT ERROR",
                    "message": "user already exist! emailAddress:ahmet.mehmet@afetyonetimsistemi.org",
                    "isSuccess": "false"
                }
                """;

        // When
        JsonNode jsonNode = new ObjectMapper().readTree(mockRawJson);
        AysMaskUtil.mask(jsonNode);

        // Then
        String mockMaskedJson = jsonNode.toString();

        log.info("Raw JSON: {}", mockRawJson);
        log.info("Masked JSON: {}", mockMaskedJson);

        Assertions.assertTrue(mockMaskedJson.contains("\"message\":\"user already exist! emailAddress:ahm******org\""));
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
    void givenValidValidationErrorJsonWith260CharsAddress_whenMasked_thenOnlyMaskFirstAndLast5CharsRemainUnmasked() throws JsonProcessingException {

        // Given
        String mockRawJson = """
                {
                    "time": "2025-01-22T15:01:50.905624",
                    "code": "a27330d8-3d2e-405f-8437-92f1fa5a5e90",
                    "header": "VALIDATION ERROR",
                    "isSuccess": false,
                    "subErrors": [
                        {
                            "message": "size must be between 20 and 250",
                            "field": "address",
                            "value": "Gravidatortor Adut Estdictum Parturientsapien Enimscelerisque Exaliquam Pharetraaenean Dignissimsuscipit Felisipsum Risusdis Volutpatgravida Ornarenulla Dolorlectus Mollisviverra Nectempor Gravidatortor Adut Estdictum Parturientsapien Enimscelerisque Exaliquam",
                            "type": "String"
                        }
                    ]
                }
                """;

        // When
        JsonNode jsonNode = new ObjectMapper().readTree(mockRawJson);
        AysMaskUtil.mask(jsonNode);

        // Then
        String mockMaskedJson = jsonNode.toString();

        log.info("Raw JSON: {}", mockRawJson);
        log.info("Masked JSON: {}", mockMaskedJson);

        Assertions.assertTrue(mockMaskedJson.contains("\"field\":\"address\",\"value\":\"Gravi******iquam\""));
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
    void givenValidValidationErrorJsonWith10CharsInvalidPhoneNumber_whenMasked_thenLast4CharsOfPhoneNumberAreUnmasked() throws JsonProcessingException {

        // Given
        String mockRawJson = """
                {
                    "time": "2025-01-22T14:48:45.779692",
                    "code": "380a8e4f-391f-43ef-b5ce-eb052de49c03",
                    "header": "VALIDATION ERROR",
                    "isSuccess": false,
                    "subErrors": [
                        {
                            "message": "must be valid",
                            "field": "phoneNumber",
                            "value": "905091111111",
                            "type": "AysPhoneNumberRequest"
                        }
                    ]
                }
                """;

        // When
        JsonNode jsonNode = new ObjectMapper().readTree(mockRawJson);
        AysMaskUtil.mask(jsonNode);

        // Then
        String mockMaskedJson = jsonNode.toString();

        log.info("Raw JSON: {}", mockRawJson);
        log.info("Masked JSON: {}", mockMaskedJson);

        Assertions.assertTrue(mockMaskedJson.contains("\"field\":\"phoneNumber\",\"value\":\"******1111\","));
    }

    @Test
    void givenValidConflictErrorJsonWithExistingPhoneNumber_whenMasked_thenLast4CharsOfLineNumberAreUnmasked() throws JsonProcessingException {

        // Given
        String mockRawJson = """
                {
                    "time": "2025-01-25T02:03:04.380344992",
                    "code": "d729f568-b5bd-41ac-800f-2cfc03146d85",
                    "header": "CONFLICT ERROR",
                    "message": "user already exist! countryCode:90, lineNumber:5051111111",
                    "isSuccess": "false"
                }
                """;

        // When
        JsonNode jsonNode = new ObjectMapper().readTree(mockRawJson);
        AysMaskUtil.mask(jsonNode);

        // Then
        String mockMaskedJson = jsonNode.toString();

        log.info("Raw JSON: {}", mockRawJson);
        log.info("Masked JSON: {}", mockMaskedJson);

        Assertions.assertTrue(mockMaskedJson.contains("\"message\":\"user already exist! countryCode:90, lineNumber:******1111\","));
    }

    @Test
    void givenValidConflictErrorJsonWithExistingPhoneNumberAndEmailAddress_whenMasked_thenMaskLineNumberAndEmailAddress() throws JsonProcessingException {

        // Given
        String mockRawJson = """
                {
                    "time": "2025-01-25T02:03:04.380344992",
                    "code": "d729f568-b5bd-41ac-800f-2cfc03146d85",
                    "header": "CONFLICT ERROR",
                    "message": "user already exist! countryCode:90, lineNumber:5051111111, emailAddress:demiragitrubar@gmail.com",
                    "isSuccess": "false"
                }
                """;

        // When
        JsonNode jsonNode = new ObjectMapper().readTree(mockRawJson);
        AysMaskUtil.mask(jsonNode);

        // Then
        String mockMaskedJson = jsonNode.toString();

        log.info("Raw JSON: {}", mockRawJson);
        log.info("Masked JSON: {}", mockMaskedJson);

        Assertions.assertTrue(mockMaskedJson.contains("\"message\":\"user already exist! countryCode:90, lineNumber:******1111, emailAddress:dem******com\","));
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
    void givenValidValidationErrorJsonWith120CharsFirstNameAnd120CharsLastName_whenMasked_thenOnlyFirstCharRemainsUnmasked() throws JsonProcessingException {

        // Given
        String mockRawJson = """
                {
                    "time": "2025-01-22T14:59:11.717531",
                    "code": "9b473494-c2ef-4793-84c0-6338d38ba33a",
                    "header": "VALIDATION ERROR",
                    "isSuccess": false,
                    "subErrors": [
                        {
                            "message": "size must be between 2 and 100",
                            "field": "firstName",
                            "value": "MonikaOliveira SilviaTu AndreaJean SushilaMalik MahmoudAbbas YueTao HectorRashid LinJones JianguoTaylor ChaoGu HalimaDas",
                            "type": "String"
                        },
                        {
                            "message": "size must be between 2 and 100",
                            "field": "lastName",
                            "value": "JianguoTaylor MonikaOliveira SilviaTu AndreaJean SushilaMalik MahmoudAbbas YueTao HectorRashid LinJones ChaoGu HalimaDas",
                            "type": "String"
                        },
                        {
                            "message": "size must be between 2 and 100",
                            "field": "applicantFirstName",
                            "value": "SilviaTu AndreaJean SushilaMalik MahmoudAbbas YueTao HectorRashid LinJones JianguoTaylor ChaoGu HalimaDas MonikaOliveira",
                            "type": "String"
                        },
                        {
                            "message": "size must be between 2 and 100",
                            "field": "applicantLastName",
                            "value": "HalimaDas MonikaOliveira SilviaTu AndreaJean SushilaMalik MahmoudAbbas YueTao HectorRashid LinJones JianguoTaylor ChaoGu",
                            "type": "String"
                        }
                    ]
                }
                """;

        // When
        JsonNode jsonNode = new ObjectMapper().readTree(mockRawJson);
        AysMaskUtil.mask(jsonNode);

        // Then
        String mockMaskedJson = jsonNode.toString();

        log.info("Raw JSON: {}", mockRawJson);
        log.info("Masked JSON: {}", mockMaskedJson);

        Assertions.assertTrue(mockMaskedJson.contains("\"field\":\"firstName\",\"value\":\"M******\""));
        Assertions.assertTrue(mockMaskedJson.contains("\"field\":\"lastName\",\"value\":\"J******\""));
        Assertions.assertTrue(mockMaskedJson.contains("\"field\":\"applicantFirstName\",\"value\":\"S******\""));
        Assertions.assertTrue(mockMaskedJson.contains("\"field\":\"applicantLastName\",\"value\":\"H******\""));
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
