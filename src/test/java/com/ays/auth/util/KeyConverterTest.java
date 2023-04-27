package com.ays.auth.util;

import com.ays.AbstractUnitTest;
import com.ays.auth.util.exception.KeyReadException;
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

class KeyConverterTest extends AbstractUnitTest {

    @Test
    void givenValidStringPrivateKeyWithPemFormat_whenStringParsed_thenReturnPrivateKey() throws IOException {
        // Given
        String mockPrivateKeyPem = TestData.VALID_PRIVATE_KEY_PEM;
        StringReader keyReader = new StringReader(mockPrivateKeyPem);
        PrivateKeyInfo privateKeyInfo = PrivateKeyInfo
                .getInstance(new PEMParser(keyReader).readObject());
        PrivateKey mockPrivateKey = new JcaPEMKeyConverter().getPrivateKey(privateKeyInfo);

        // When
        PrivateKey privateKey = KeyConverter.convertPrivateKey(mockPrivateKeyPem);

        // Then
        Assertions.assertEquals(mockPrivateKey, privateKey);
    }

    @Test
    void givenInvalidStringPrivateKeyWithPemFormat_whenStringNotParsed_thenThrowKeyReadException() {
        // Given
        String mockPrivateKeyPem = TestData.INVALID_PRIVATE_KEY_PEM;

        // Then
        Assertions.assertThrows(KeyReadException.class, () -> KeyConverter.convertPrivateKey(mockPrivateKeyPem));
    }

    @Test
    void givenValidStringPublicKeyWithPemFormat_whenStringParsed_thenReturnPublicKey() throws IOException {
        // Given
        String mockPublicKeyPem = TestData.VALID_PUBLIC_KEY_PEM;
        StringReader keyReader = new StringReader(mockPublicKeyPem);
        SubjectPublicKeyInfo publicKeyInfo = SubjectPublicKeyInfo
                .getInstance(new PEMParser(keyReader).readObject());
        PublicKey mockPublicKey = new JcaPEMKeyConverter().getPublicKey(publicKeyInfo);

        // When
        PublicKey publicKey = KeyConverter.convertPublicKey(mockPublicKeyPem);

        // Then
        Assertions.assertEquals(mockPublicKey, publicKey);
    }

    @Test
    void givenInvalidStringPublicKeyWithPemFormat_whenStringNotParsed_thenThrowKeyReadException() {
        // Given
        String mockPublicKeyPem = TestData.INVALID_PUBLIC_KEY_PEM;

        // Then
        Assertions.assertThrows(KeyReadException.class, () -> KeyConverter.convertPublicKey(mockPublicKeyPem));
    }


    private static class TestData {
        private static final String VALID_PRIVATE_KEY_PEM = """
                -----BEGIN PRIVATE KEY-----
                MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDH+MUShIcE42+v
                eixv8iUJ0+Pd76HWgX2iHhaSftXQs9O5doCOKCMVVkK1H8ORN6ZgDIVIhao5MTCP
                wPvH85YU2Y2ukILfXps2PCdLMQg45b2t8HFqJJAaqzQQ5ACAVWx3hbVWFQs5g7yL
                eB/tagY+QaElDXpNk85Xh6EIIOjQLyqSHa12Fhi9PRaqJM5kC/HQY+E8YtHOAout
                aUG/C78l/sTMg3sPGB/4AcTd3cKiHu3Aauy0pstqdzJ6niMSO1L004MXYaMmBl8I
                g9b60JwVxF2fC1ZHCH+BlckYbQUJ9JCqhr7qnoFbg7ngLe3iqcLmLmPowNm8kpLK
                hkV5AwdPAgMBAAECggEARs+hwW/qe+Gpv+Ksb6u4T+WXcBSWI2ZRPaIX7iI5xqCX
                HbqHxU8TNVAJaSfpUbf6E1L7s3WZlI0FnDIDNofcIl/zWthTb5OJtMfSRj8DoVpB
                M6HMF4EBAmCTnFOQleEp+pz/XI8xHVm3309XRvPfaBZHYN6H64amb7pYXI+CwY0A
                xTAntPO4uKwvrzTc+rHtVLRjA2fRUHZ+QJ+IObXwVxFUFMGS7b98jk2LuzTsQoLx
                dNFaE3XtEpUNyKePnfly2lmBv6obIuxr3Dnppzdye6+u3iJ+TD5uNFAT/ZF24pE/
                oP71rmdPhJCuxK219AJoC15Z5dsBOm1za/synNEnwQKBgQD/VzizN6z1Rt9rk9Vt
                X1apiLpgWhx5USDBcTwya03AtZTrH7hVqkpLFnCOFnyprV3MauztNSpWXOWLljs4
                MtJO+Vfx4YzRS013w8bSp7ZrcFfZNXKa0zkx4rCiaUxzC5LKSN0q3NZxKjC37OYS
                cUHS5knpV19UNSj2sunC3xUj4QKBgQDIfPMnWVgTscqvbdjm/yNwbSYrbYXduuPm
                axrcMe4LiHQ6nabNSM1otkH/tsrQnrhfMDKtsWnDu40Jb8iFvoO8I3/vOhdI3oLG
                oZcPXYljKtrbE0NHt2aRiANto+/GRRy6kH6EKjseerksq4SLLCBbEgjNKrnhViPR
                OXswhK+RLwKBgC5TMrRBG538V7h6v7PyIhTr+3RTpOrVry2pT5SOJzMZPoVR4e2Y
                0ZXB4nXE2qUmEOhvVcDLbnzwqayjeub9QW6WikAV/ahTEyDxYfcB+nSPk0CTE9HH
                FI9aY1Vz6SzOIrmUcpu+KSGq19/mmO8roReUNECjW2Y5ps7rMsHqGznBAoGALzln
                WPRtj64ITQw11Cty4I+FNyOELbdQ6Tx/RjConqTUo93wpVgpsimEIEShP0SzrxA+
                T7WDcSxjSz6+X+qBQzalcucfBvYKndkAKQliTC6TAJln9qOXkF4WWKQN3Yj3/GX+
                twjvhf1oUsJP5SxOrsTvt8wBnrdYlnbQspv+ctcCgYEA4M/Wvbo0MAurrtJ76tfE
                lzvj79/cGU7xNBKODn/PBzgdbE8QnQ5Je+V9ItFPUaT6gUr8zHGyKwcBpZDAsBZk
                fMhHTTgkOI/loUvUo6RuJk+HZm0sWkVk1BvIs478HHgu+8RYVqmkHVhGcFcc3Ghr
                7400PzFngOk2CgDXBijQS+Y=
                -----END PRIVATE KEY-----
                """;

        private static final String INVALID_PRIVATE_KEY_PEM = """
                MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDH+MUShIcE42+v
                eixv8iUJ0+Pd76HWgX2iHhaSftXQs9O5doCOKCMVVkK1H8ORN6ZgDIVIhao5MTCP
                wPvH85YU2Y2ukILfXps2PCdLMQg45b2t8HFqJJAaqzQQ5ACAVWx3hbVWFQs5g7yL
                eB/tagY+QaElDXpNk85Xh6EIIOjQLyqSHa12Fhi9PRaqJM5kC/HQY+E8YtHOAout
                aUG/C78l/sTMg3sPGB/4AcTd3cKiHu3Aauy0pstqdzJ6niMSO1L004MXYaMmBl8I
                g9b60JwVxF2fC1ZHCH+BlckYbQUJ9JCqhr7qnoFbg7ngLe3iqcLmLmPowNm8kpLK
                hkV5AwdPAgMBAAECggEARs+hwW/qe+Gpv+Ksb6u4T+WXcBSWI2ZRPaIX7iI5xqCX
                HbqHxU8TNVAJaSfpUbf6E1L7s3WZlI0FnDIDNofcIl/zWthTb5OJtMfSRj8DoVpB
                M6HMF4EBAmCTnFOQleEp+pz/XI8xHVm3309XRvPfaBZHYN6H64amb7pYXI+CwY0A
                xTAntPO4uKwvrzTc+rHtVLRjA2fRUHZ+QJ+IObXwVxFUFMGS7b98jk2LuzTsQoLx
                dNFaE3XtEpUNyKePnfly2lmBv6obIuxr3Dnppzdye6+u3iJ+TD5uNFAT/ZF24pE/
                oP71rmdPhJCuxK219AJoC15Z5dsBOm1za/synNEnwQKBgQD/VzizN6z1Rt9rk9Vt
                X1apiLpgWhx5USDBcTwya03AtZTrH7hVqkpLFnCOFnyprV3MauztNSpWXOWLljs4
                MtJO+Vfx4YzRS013w8bSp7ZrcFfZNXKa0zkx4rCiaUxzC5LKSN0q3NZxKjC37OYS
                cUHS5knpV19UNSj2sunC3xUj4QKBgQDIfPMnWVgTscqvbdjm/yNwbSYrbYXduuPm
                axrcMe4LiHQ6nabNSM1otkH/tsrQnrhfMDKtsWnDu40Jb8iFvoO8I3/vOhdI3oLG
                oZcPXYljKtrbE0NHt2aRiANto+/GRRy6kH6EKjseerksq4SLLCBbEgjNKrnhViPR
                OXswhK+RLwKBgC5TMrRBG538V7h6v7PyIhTr+3RTpOrVry2pT5SOJzMZPoVR4e2Y
                0ZXB4nXE2qUmEOhvVcDLbnzwqayjeub9QW6WikAV/ahTEyDxYfcB+nSPk0CTE9HH
                FI9aY1Vz6SzOIrmUcpu+KSGq19/mmO8roReUNECjW2Y5ps7rMsHqGznBAoGALzln
                WPRtj64ITQw11Cty4I+FNyOELbdQ6Tx/RjConqTUo93wpVgpsimEIEShP0SzrxA+
                T7WDcSxjSz6+X+qBQzalcucfBvYKndkAKQliTC6TAJln9qOXkF4WWKQN3Yj3/GX+
                twjvhf1oUsJP5SxOrsTvt8wBnrdYlnbQspv+ctcCgYEA4M/Wvbo0MAurrtJ76tfE
                lzvj79/cGU7xNBKODn/PBzgdbE8QnQ5Je+V9ItFPUaT6gUr8zHGyKwcBpZDAsBZk
                fMhHTTgkOI/loUvUo6RuJk+HZm0sWkVk1BvIs478HHgu+8RYVqmkHVhGcFcc3Ghr
                7400PzFngOk2CgDXBijQS+Y=
                """;

        private static final String VALID_PUBLIC_KEY_PEM = """
                -----BEGIN PUBLIC KEY-----
                MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAx/jFEoSHBONvr3osb/Il
                CdPj3e+h1oF9oh4Wkn7V0LPTuXaAjigjFVZCtR/DkTemYAyFSIWqOTEwj8D7x/OW
                FNmNrpCC316bNjwnSzEIOOW9rfBxaiSQGqs0EOQAgFVsd4W1VhULOYO8i3gf7WoG
                PkGhJQ16TZPOV4ehCCDo0C8qkh2tdhYYvT0WqiTOZAvx0GPhPGLRzgKLrWlBvwu/
                Jf7EzIN7Dxgf+AHE3d3Coh7twGrstKbLancyep4jEjtS9NODF2GjJgZfCIPW+tCc
                FcRdnwtWRwh/gZXJGG0FCfSQqoa+6p6BW4O54C3t4qnC5i5j6MDZvJKSyoZFeQMH
                TwIDAQAB
                -----END PUBLIC KEY-----
                """;

        private static final String INVALID_PUBLIC_KEY_PEM = """
                MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAx/jFEoSHBONvr3osb/Il
                CdPj3e+h1oF9oh4Wkn7V0LPTuXaAjigjFVZCtR/DkTemYAyFSIWqOTEwj8D7x/OW
                FNmNrpCC316bNjwnSzEIOOW9rfBxaiSQGqs0EOQAgFVsd4W1VhULOYO8i3gf7WoG
                PkGhJQ16TZPOV4ehCCDo0C8qkh2tdhYYvT0WqiTOZAvx0GPhPGLRzgKLrWlBvwu/
                Jf7EzIN7Dxgf+AHE3d3Coh7twGrstKbLancyep4jEjtS9NODF2GjJgZfCIPW+tCc
                FcRdnwtWRwh/gZXJGG0FCfSQqoa+6p6BW4O54C3t4qnC5i5j6MDZvJKSyoZFeQMH
                TwIDAQAB
                """;
    }

}