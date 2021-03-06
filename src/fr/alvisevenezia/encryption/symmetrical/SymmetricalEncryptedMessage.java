package fr.alvisevenezia.encryption.symmetrical;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class SymmetricalEncryptedMessage {

    public static byte[] clearByteArray(byte[] array){

        int newSize = array.length;

        for(int i = array.length-1;i > 1;i -= 2){

            if(array[i] == 0 & array[i-1] == 0) newSize -= 2;

        }

        byte[] newArray = Arrays.copyOf(array,newSize);

        return newArray;

    }

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

    public static byte[] getEncryptedMessage(String message,String key){

        Cipher cipher;
        byte[] msg = Arrays.copyOf(message.getBytes(StandardCharsets.UTF_16LE),message.length()*2);

        try {
            cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, getKey(key));

            byte[] cryptedByteArray = cipher.doFinal(msg);

            System.out.println("ENCRYPTED MSG : "+new String(cryptedByteArray));

            return cryptedByteArray;
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }

        return null;

    }

    public static String getDecryptedMessage(byte[] message,String key){

        Cipher cipher = null;

        System.out.println("DECRYPTED MSG LENGTH : "+message.length);

        try {
            cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, getKey(key));
            byte[] array = clearByteArray(cipher.doFinal(message));
            return new String(array,StandardCharsets.UTF_16LE);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }

        return null;

    }

}
