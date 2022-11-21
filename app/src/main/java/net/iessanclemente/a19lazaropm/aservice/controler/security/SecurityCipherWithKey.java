package net.iessanclemente.a19lazaropm.aservice.controler.security;

import android.widget.Toast;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import android.util.Base64;

public class SecurityCipherWithKey{
    private SecretKey secretKey;
    private Cipher cipher;
    private final String ALGORITHM = "AES";
    private final int keySize = 16;

    public SecurityCipherWithKey() {
    }

    public void addKey(String keyValue) {
        byte[] valueBytes = keyValue.getBytes();
        secretKey = new SecretKeySpec(Arrays.copyOf(valueBytes, keySize ), ALGORITHM);
    }

    public String encrypt(String string) {
        String stringEncripted = "";
        String errorMessage;

        try {
            //Instancio Cipher con el algoritmo
            cipher = Cipher.getInstance(ALGORITHM);
            //Configuro incio de Cipher en modo encriptación
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            //genero array de bytes de la string
            byte[] stringBytes = string.getBytes();
            //obtengo el array de bytes cifrado
            byte[] cipherBytes = cipher.doFinal(stringBytes);
            //obtengo la string encriptada
            stringEncripted = Base64.encodeToString(cipherBytes, Base64.DEFAULT);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }

        return stringEncripted;
    }

    public String decrypt(String stringEncripted) {
        String stringOrigin = "";
        String errorMessage;

        try {
            //obtengo array bytes desde la string encriptada
            byte[] cipherBytes = Base64.decode(stringEncripted, Base64.DEFAULT);
            //Instancio Cipher con el algoritmo
            cipher = Cipher.getInstance(ALGORITHM);
            //Configuro incio de Cipher en modo desencriptación
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            //genero array de bytes de la string desencriptada
            byte[] stringBytes = cipher.doFinal(cipherBytes);
            //obtengo la string
            stringOrigin = new String(stringBytes);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }

        return stringOrigin;
    }


}
