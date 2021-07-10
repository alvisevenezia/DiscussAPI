package fr.alvisevenezia.encryption.asymmetrical;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

public class AsymmetricalEncryptionMessage {

    public static KeyPair getKeyPair(){

        KeyPairGenerator keyPairGenerator = null;
        try {
            keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        assert keyPairGenerator != null;

        return keyPairGenerator.generateKeyPair();
    }
    
    public static byte[] getEncyptedMessage(String message,String key){

        byte[] array = new byte[0];
        
        try {
            
            Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, getKeyPair().getPublic());
            array = cipher.doFinal(message.getBytes(StandardCharsets.UTF_16LE));
            
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }

        
        return array;

    }


}
