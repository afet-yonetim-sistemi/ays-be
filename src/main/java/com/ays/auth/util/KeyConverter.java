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

@UtilityClass
public class KeyConverter {

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
