package lipuka.android.security;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import lipuka.android.util.Base64;

/**
 * Example of using PBE without using a PBEParameterSpec
 */
public class PBEEncryption
{
	public static String encrypt(char[] password, String plaintext) throws Exception {

	    byte[] salt = new byte[8];
	   	  SecureRandom sr = new SecureRandom();
	    sr.nextBytes(salt);

	    PBEKeySpec keySpec = new PBEKeySpec(password);

	    SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithSHAAnd3KeyTripleDES", "BC");

	    SecretKey key = keyFactory.generateSecret(keySpec);

	    PBEParameterSpec paramSpec = new PBEParameterSpec(salt, 1000);

	    Cipher cipher = Cipher.getInstance("PBEWithSHAAnd3KeyTripleDES", "BC");
	    cipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);

	    byte[] ciphertext = cipher.doFinal(plaintext.getBytes());

	    String saltString = Base64.encodeBytes(salt);
	    String ciphertextString = Base64.encodeBytes(ciphertext);

	    return saltString + ciphertextString;
	  }

	public static String decrypt(char[] password, String text) throws Exception {
	    String salt = text.substring(0, 12);
	    String ciphertext = text.substring(12, text.length());
	    byte[] saltArray = Base64.decode(salt);
	    byte[] ciphertextArray = Base64.decode(ciphertext);

	    PBEKeySpec keySpec = new PBEKeySpec(password);

	    SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithSHAAnd3KeyTripleDES", "BC");

	    SecretKey key = keyFactory.generateSecret(keySpec);

	    PBEParameterSpec paramSpec = new PBEParameterSpec(saltArray, 1000);

	    Cipher cipher = Cipher.getInstance("PBEWithSHAAnd3KeyTripleDES", "BC");
	    cipher.init(Cipher.DECRYPT_MODE, key, paramSpec);

	    return new String(cipher.doFinal(ciphertextArray));
	  }
}
