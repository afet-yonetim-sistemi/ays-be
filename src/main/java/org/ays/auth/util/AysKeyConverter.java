package org.ays.auth.util;

import lombok.experimental.UtilityClass;
import org.ays.auth.exception.AysKeyReadException;
import org.ays.encryption.utility.AysPrivateKeyEncryptionUtil;
import org.ays.encryption.utility.AysPublicKeyEncryptionUtil;
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
public class AysKeyConverter {

    /**
     * Converts an encrypted private key in PEM format to a Java PrivateKey object.
     *
     * @param encryptedPrivateKeyPem the encrypted private key in PEM format.
     * @return the corresponding Java PrivateKey object.
     * @throws AysKeyReadException if an error occurs while parsing the key.
     */
    public static PrivateKey convertPrivateKey(String encryptedPrivateKeyPem) {
        final String decryptedPrivateKeyPem = AysPrivateKeyEncryptionUtil.decrypt(encryptedPrivateKeyPem);
        final String formattedPrivateKeyPem = decryptedPrivateKeyPem.replace("             ", "\n");
        StringReader keyReader = new StringReader(formattedPrivateKeyPem);
        try {
            PrivateKeyInfo privateKeyInfo = PrivateKeyInfo
                    .getInstance(new PEMParser(keyReader).readObject());
            return new JcaPEMKeyConverter().getPrivateKey(privateKeyInfo);
        } catch (IOException exception) {
            throw new AysKeyReadException(exception);
        }
    }

    /**
     * Converts an encrypted public key in PEM format to a Java PublicKey object.
     *
     * @param encryptedPublicKeyPem the encrypted public key in PEM format.
     * @return the corresponding Java PublicKey object.
     * @throws AysKeyReadException if an error occurs while parsing the key.
     */
    public static PublicKey convertPublicKey(String encryptedPublicKeyPem) {
        final String decryptedPublicKeyPem = AysPublicKeyEncryptionUtil.decrypt(encryptedPublicKeyPem);
        final String formattedPublicKeyPem = decryptedPublicKeyPem.replace("             ", "\n");
        StringReader keyReader = new StringReader(formattedPublicKeyPem);
        try {
            SubjectPublicKeyInfo publicKeyInfo = SubjectPublicKeyInfo
                    .getInstance(new PEMParser(keyReader).readObject());
            return new JcaPEMKeyConverter().getPublicKey(publicKeyInfo);
        } catch (IOException exception) {
            throw new AysKeyReadException(exception);
        }
    }

}
