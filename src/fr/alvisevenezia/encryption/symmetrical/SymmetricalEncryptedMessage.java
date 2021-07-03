package fr.alvisevenezia.encryption.symmetrical;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.security.auth.kerberos.EncryptionKey;
import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

public class SymmetricalEncryptedMessage {

    public static final IvParameterSpec IV_PARAMETER_SPEC = new IvParameterSpec("cacacaca".getBytes(StandardCharsets.UTF_16LE));

    public static SecretKey getKey(String key){

        byte[] keyByteArray;
        SecretKey secretKey = null;
        try {
            MessageDigest sha = MessageDigest.getInstance("SHA-256");
            keyByteArray = sha.digest(key.getBytes(StandardCharsets.UTF_16));

            System.out.print("KEY BYTE : ");

            for(byte b : keyByteArray){

                System.out.print(b);

            }

            System.out.println('\n');

            secretKey = new SecretKeySpec(keyByteArray,"AES");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return secretKey;
    }

    public static String getEncryptedMessage(String message,String key){

        Cipher cipher;
        byte[] msg = Arrays.copyOf(message.getBytes(StandardCharsets.UTF_16),message.length()+(16-message.length()%16));

        try {
            cipher = Cipher.getInstance("AES/CBC/NoPadding ");
            cipher.init(Cipher.ENCRYPT_MODE, getKey(key),IV_PARAMETER_SPEC);

            String s = new String(cipher.doFinal(msg),StandardCharsets.UTF_16LE);

            System.out.print("ENCRYPTED MSG : "+s);

            return s;
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException | InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }

        return null;

    }

    public static String getDecryptedMessage(byte[] message,String key){

        Cipher cipher = null;

        System.out.println("DECRYPTED MSG LENGTH : "+message.length);

        try {
            cipher = Cipher.getInstance("AES/CBC/NoPadding ");
            cipher.init(Cipher.DECRYPT_MODE, getKey(key),IV_PARAMETER_SPEC);
            return new String(cipher.doFinal(message),StandardCharsets.UTF_16);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException | InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }

        return null;

    }

}
