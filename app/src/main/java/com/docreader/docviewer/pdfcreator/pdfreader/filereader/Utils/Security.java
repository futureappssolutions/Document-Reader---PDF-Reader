package com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils;

import android.text.TextUtils;
import android.util.Base64;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

public class Security {
    private static final String KEY_FACTORY_ALGORITHM = "RSA";
    private static final String SIGNATURE_ALGORITHM = "SHA1withRSA";


    public static boolean verifyPurchase(String base64PublicKey, String signedData, String signature) throws IOException {
        if (TextUtils.isEmpty(signedData) || TextUtils.isEmpty(base64PublicKey) || TextUtils.isEmpty(signature)) {
            //Purchase verification failed: missing data
            return false;
        }

        PublicKey key = generatePublicKey(base64PublicKey);
        return verify(key, signedData, signature);
    }

    public static PublicKey generatePublicKey(String encodedPublicKey) throws IOException {
        try {
            byte[] decodedKey = Base64.decode(encodedPublicKey, Base64.DEFAULT);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_FACTORY_ALGORITHM);
            return keyFactory.generatePublic(new X509EncodedKeySpec(decodedKey));
        } catch (NoSuchAlgorithmException e) {
            // "RSA" is guaranteed to be available.
            throw new RuntimeException(e);
        } catch (InvalidKeySpecException e) {
            String msg = "Invalid key specification: " + e;
            throw new IOException(msg);
        }
    }

    public static boolean verify(PublicKey publicKey, String signedData, String signature) {
        byte[] signatureBytes;
        try {
            signatureBytes = Base64.decode(signature, Base64.DEFAULT);
        } catch (IllegalArgumentException e) {
            //Base64 decoding failed
            return false;
        }
        try {
            Signature signatureAlgorithm = Signature.getInstance(SIGNATURE_ALGORITHM);
            signatureAlgorithm.initVerify(publicKey);
            signatureAlgorithm.update(signedData.getBytes());
            if (!signatureAlgorithm.verify(signatureBytes)) {
                //Signature verification failed
                return false;
            }
            return true;
        } catch (NoSuchAlgorithmException e) {
            // "RSA" is guaranteed to be available
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            //Invalid key specification
        } catch (SignatureException e) {
            //Signature exception
        }
        return false;
    }
}
