package org.ays.auth.util;

import org.ays.AysUnitTest;
import org.ays.auth.util.exception.AysKeyReadException;
import org.ays.encryption.utility.AysPrivateKeyEncryptionUtil;
import org.ays.encryption.utility.AysPublicKeyEncryptionUtil;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringReader;
import java.security.PrivateKey;
import java.security.PublicKey;

class AysKeyConverterTest extends AysUnitTest {

    @Test
    void givenValidStringPrivateKeyWithPemFormat_whenStringParsed_thenReturnPrivateKey() throws IOException {
        // Given
        String mockEncryptedPrivateKeyPem = TestData.VALID_ENCRYPTED_PRIVATE_KEY_PEM;
        String mockDecryptedPrivateKeyPem = AysPrivateKeyEncryptionUtil.decrypt(mockEncryptedPrivateKeyPem);
        StringReader keyReader = new StringReader(mockDecryptedPrivateKeyPem);
        PrivateKeyInfo privateKeyInfo = PrivateKeyInfo
                .getInstance(new PEMParser(keyReader).readObject());
        PrivateKey mockPrivateKey = new JcaPEMKeyConverter().getPrivateKey(privateKeyInfo);

        // When
        PrivateKey privateKey = AysKeyConverter.convertPrivateKey(mockEncryptedPrivateKeyPem);

        // Then
        Assertions.assertEquals(mockPrivateKey, privateKey);
    }

    @Test
    void givenInvalidStringPrivateKeyWithPemFormat_whenStringNotParsed_thenThrowKeyReadException() {
        // Given
        String mockPrivateKeyPem = TestData.INVALID_ENCRYPTED_PRIVATE_KEY_PEM;

        // Then
        Assertions.assertThrows(AysKeyReadException.class, () -> AysKeyConverter.convertPrivateKey(mockPrivateKeyPem));
    }

    @Test
    void givenValidStringPublicKeyWithPemFormat_whenStringParsed_thenReturnPublicKey() throws IOException {
        // Given
        String mockEncryptedPublicKeyPem = TestData.VALID_ENCRYPTED_PUBLIC_KEY_PEM;
        String mockDecryptedPublicKeyPem = AysPublicKeyEncryptionUtil.decrypt(mockEncryptedPublicKeyPem);
        StringReader keyReader = new StringReader(mockDecryptedPublicKeyPem);
        SubjectPublicKeyInfo publicKeyInfo = SubjectPublicKeyInfo
                .getInstance(new PEMParser(keyReader).readObject());
        PublicKey mockPublicKey = new JcaPEMKeyConverter().getPublicKey(publicKeyInfo);

        // When
        PublicKey publicKey = AysKeyConverter.convertPublicKey(mockEncryptedPublicKeyPem);

        // Then
        Assertions.assertEquals(mockPublicKey, publicKey);
    }

    @Test
    void givenInvalidStringPublicKeyWithPemFormat_whenStringNotParsed_thenThrowKeyReadException() {
        // Given
        String mockPublicKeyPem = TestData.INVALID_ENCRYPTED_PUBLIC_KEY_PEM;

        // Then
        Assertions.assertThrows(AysKeyReadException.class, () -> AysKeyConverter.convertPublicKey(mockPublicKeyPem));
    }


    private static class TestData {
        private static final String VALID_ENCRYPTED_PRIVATE_KEY_PEM = "dkNHbEVaSW9rdGQ1cVBKek03dHJqT2daMmdIbEpUSEw=LS0tLS1CRUdJTiBQUklWQVRFIEtFWS0tLS0tCk1JSUV2QUlCQURBTkJna3Foa2lHOXcwQkFRRUZBQVNDQktZd2dnU2lBZ0VBQW9JQkFRQ0puK0lSRUhmclFsWjEKY0RkbllKcndUL1pvV1hick13aGt1enBXdUJVRDJHdTVteU1DdCtyT0p1S3N4VXFIL2IyZGFlZW81dUdndWY2cwpKcjI5bzZLM1drVVBMdG9uNk5GZUhKZEwwcWs5NmVlWDM5UVhFUHYzdS8yRGJXZTJRWk83aW4wNnhLSTQyOUgwCmg4cjdCT0NzemE1WFpkbjVCUHdmZ0pveUFLbkdGODNPMGpUT0Qzem8zV0VvbGEreW1YYXVoUHorUFl0dndSWncKSkZvQW91MTVZM2Z1SDBna3B4NDdRd0tnVFRwNUl4d1I4RnNxVkVOK1JaNFMzcWhxSktOaGY2SVkwS1RjaHphQQpmelp3QnRON0E1eWI4TURMRnRJTHlmRkdhYW0wbXMzUHJWb0J3LzhzYmxsSGlEMTdOVCtXcmNNVkJRRjJTRFlwCmR3WENMM1JiQWdNQkFBRUNnZ0VBS3FzK2JPbjlOQnlTTDhFRi9IQXdPWGVoUHdNVjRxQWs1dzVCYlNlUHBHeVMKSWE2ZXNUWVNmNjRBczI1THlGUDhXUFMvMVZjWDl6d1RZSTUyWDNoL2QzZHVWK0cvMDRYVWUraERaRWZCSHlnSgpITVpSdklFUWplTmtHejV0WEUyQ255KzEyZVdqSWh2TlFaSmtkV1V5djREWm45RTlQbjYwS0pRM3VtOElOQmt3CjZLNUFIMGxiYy8yMEtFYWZpMUsyMzBTUVFCeFp4bEN6bkNJSmNQYlZGSmxwVUNOM1JkaDFJSkdYZUlLT0NRbjkKQnB4Z2ZKVC9KUFo1cWVXamF1OUYxVWlCbWYyZjFzNnlDSXB2aDVsYUhKMXRuU1NuQXlKalkzYzFFUk1GMHBDSgpwUUtIMlEyaXFEYUxGNGdRQWIyQlRnYm85NWVja1RZaFdPVUlQQUt3UVFLQmdRRGhUbXhvTlQwRXlQWEptVFRPClJvdUNwSW4ycmlldGcwUXdYVVVVSDhMMHNJRUVqRDFpeXp5NVBvNk55OWN1QVNYSXBRM05RaDExZ2k1MTJXNmYKYXIyYWtXdXVRK01ENmRNM3RCa0xQSlJITVQ1SmVJbDBQQTJXc2tvMktTcmZtbUI1TFI5MzdpeFNjZmhvajY4bQpWSHBGQ2FhRDBtdVVzUHZYVVR0ZmhMV1ZZUUtCZ1FDY1g0ekllNS9HSWk3Z0pMZTZZTm5ITmprUmhIb3VreEVECm1HbjB2WlRlUzh1SXZrL1JQL2dMdjIwN3hIOXcwMEc1cS9BbytnWG1DV3pQWlhybmR1SVVZbUxwZ2NKVDh5bWMKQVovSWhsbkU4azBxTHlleWRtWmV0NHNXcXY3NkFseFlhNTdFZ3daaTNMQ1Z3OFBIVnIwb1pHR09vTGd3a3g3QgpRV2V4cXJ4bk93S0JnRXlGNlZYL2R3a1FCRU1Ea1NiYVdQbjNUcENGR0I3YnJhWkxsM0c5VStidHAvUldlV2I3CnBsVTRoUXh1QmxpdXRSbVB6YjlBVEdjajN3blIzcnV3Y2xOMFBzR0NkekZXRXBJaHpqdTl5SkxoaThsQ2NsVVQKTEg1WmNkRXhiRWxqMG81MW4vR0k2RzdjSE1YT3YydGlWK0RvNVRCeW9HMXhLeWczZzlYdWFnb2hBb0dBUkJ0Wgp0ZmdpSHFuRXdOczlLbkFFYWorem0yMlh5YkZFTjh5cVdXNDQ2SmthalBSV3oweU5QSkNqZ3VTU25SRm1EdmhVCklZVEVETzBOOTBhN3dSU0dZMXAydWoxSjVrYUNXUEJjSjNwY251cnBzUFhZMUdHOU5JTzhrS0xwYXZxY1BlYWgKdi9WUlVyM01LMjZZVnJud3FTY1BWbytwcVg1cVpzR1Y2RXYwd3dFQ2dZQkdSckF2cEptVWluazVoc0ZldzRvaAozclMwRTlqQklqekszWU9WWTJJVWxydmJYd2N2OWJ0UGpaVkd4VzZ1UEt0UEtPNTFaUExzRS94YWRiUGllOGhmCmM1aTB3LzVPK24rYlc3TlpmcWwvUFk4OTB5ajZJWHlzZElYMVRwQ1NST1Uzb1ptUWZBTG9WWWNBNHU3QWxpY0sKYnBKbTYwampiMHNNejRIVU9CaVZIUT09Ci0tLS0tRU5EIFBSSVZBVEUgS0VZLS0tLS0KNjNLcEhJM2FXeUtrOHNsVkFiaGY3UjBvcURVeHh5RUk=";

        private static final String INVALID_ENCRYPTED_PRIVATE_KEY_PEM = "dkNHbEVaSW9rdGQ1cVBKek03dHJqT2daMmdIbEpUSEw=SGVsbG8gV29ybGQhNjNLcEhJM2FXeUtrOHNsVkFiaGY3UjBvcURVeHh5RUk=";

        private static final String VALID_ENCRYPTED_PUBLIC_KEY_PEM = "cExiWUJBSVFTbHVwOXFXSkxSTDhsOGRDVkh1VkxyQ0E=LS0tLS1CRUdJTiBQVUJMSUMgS0VZLS0tLS0KTUlJQklqQU5CZ2txaGtpRzl3MEJBUUVGQUFPQ0FROEFNSUlCQ2dLQ0FRRUFpWi9pRVJCMzYwSldkWEEzWjJDYQo4RS8yYUZsMjZ6TUlaTHM2VnJnVkE5aHJ1WnNqQXJmcXppYmlyTVZLaC8yOW5Xbm5xT2Job0xuK3JDYTl2YU9pCnQxcEZEeTdhSitqUlhoeVhTOUtwUGVubmw5L1VGeEQ3OTd2OWcyMW50a0dUdTRwOU9zU2lPTnZSOUlmSyt3VGcKck0ydVYyWForUVQ4SDRDYU1nQ3B4aGZOenRJMHpnOTg2TjFoS0pXdnNwbDJyb1Q4L2oyTGI4RVdjQ1JhQUtMdAplV04zN2g5SUpLY2VPME1Db0UwNmVTTWNFZkJiS2xSRGZrV2VFdDZvYWlTallYK2lHTkNrM0ljMmdIODJjQWJUCmV3T2NtL0RBeXhiU0M4bnhSbW1wdEpyTno2MWFBY1AvTEc1WlI0ZzllelUvbHEzREZRVUJka2cyS1hjRndpOTAKV3dJREFRQUIKLS0tLS1FTkQgUFVCTElDIEtFWS0tLS0tCg==SlhaNTl3eFBsWTJEWkNSWDdlZFRlaFhPaFhZRzlBN1Q=";

        private static final String INVALID_ENCRYPTED_PUBLIC_KEY_PEM = "cExiWUJBSVFTbHVwOXFXSkxSTDhsOGRDVkh1VkxyQ0E=SGVsbG8gV29ybGQhSlhaNTl3eFBsWTJEWkNSWDdlZFRlaFhPaFhZRzlBN1Q=";
    }

}
