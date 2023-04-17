package com.ays.auth.util;

import com.ays.auth.util.exception.KeyReadException;
import lombok.experimental.UtilityClass;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;

import java.io.IOException;
import java.io.StringReader;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * Utility class for converting public and private keys in PEM format to Java objects.
 */
@UtilityClass
public class KeyConverter {

    /**
     * Converts a private key in PEM format to a Java PrivateKey object.
     *
     * @param privateKeyPem the private key in PEM format.
     * @return the corresponding Java PrivateKey object.
     * @throws KeyReadException if an error occurs while parsing the key.
     */
    public static PrivateKey convertPrivateKey(String privateKeyPem) {
        final String formattedPrivateKeyPem = privateKeyPem.replace("             ", "\n");
        StringReader keyReader = new StringReader(formattedPrivateKeyPem);
        try {
            PrivateKeyInfo privateKeyInfo = PrivateKeyInfo
                    .getInstance(new PEMParser(keyReader).readObject());
            return new JcaPEMKeyConverter().getPrivateKey(privateKeyInfo);
        } catch (IOException exception) {
            throw new KeyReadException(exception);
        }
    }

    /**
     * Converts a public key in PEM format to a Java PublicKey object.
     *
     * @param publicKeyPem the public key in PEM format.
     * @return the corresponding Java PublicKey object.
     * @throws KeyReadException if an error occurs while parsing the key.
     */
    public static PublicKey convertPublicKey(String publicKeyPem) {
        final String formattedPublicKeyPem = publicKeyPem.replace("             ", "\n");
        StringReader keyReader = new StringReader(formattedPublicKeyPem);
        try {
            SubjectPublicKeyInfo publicKeyInfo = SubjectPublicKeyInfo
                    .getInstance(new PEMParser(keyReader).readObject());
            return new JcaPEMKeyConverter().getPublicKey(publicKeyInfo);
        } catch (IOException exception) {
            throw new KeyReadException(exception);
        }
    }

}
