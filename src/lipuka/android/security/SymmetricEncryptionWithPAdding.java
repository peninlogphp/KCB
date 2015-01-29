package lipuka.android.security;


import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import kcb.android.Main;







import lipuka.android.util.Utils;
import android.util.Log;


public class SymmetricEncryptionWithPAdding {

	  static public byte[] aesKey, aesInitV;
	   static Cipher cipher;
	   static IvParameterSpec ivSpec;

	   static SecretKeySpec   key;
   public static void execute() throws Exception
   {
       byte[]          input = new byte[] {
                               0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07,
                               0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f,
                               0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07 };

       init();
       byte[] cipherText = aesEncrypt(input);

       byte[] plainText = aesDecrypt(cipherText);


   }
   
   public static void init () throws Exception {

   	  SecureRandom sr = new SecureRandom();

   	  aesKey = new byte [16];

   	  sr.nextBytes(aesKey);

   	  aesInitV = new byte [16];

   	  sr.nextBytes(aesInitV);

           key = new SecretKeySpec(aesKey, "AES");
          ivSpec = new IvParameterSpec(aesInitV);
         cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");


         cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
//symmetricBlockSize = cipher.getBlockSize();

   	}
   	    public static byte [] aesEncrypt (byte [] toEncrypt) throws Exception {
    cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
    byte[] cipherText = new byte[cipher.getOutputSize(toEncrypt.length)];

    int ctLength = cipher.update(toEncrypt, 0, toEncrypt.length, cipherText, 0);

	Log.d(Main.TAG, "input length: "  + toEncrypt.length);
	Log.d(Main.TAG, "initial ctLength: " + ctLength);

    ctLength += cipher.doFinal(cipherText, ctLength);

	Log.d(Main.TAG, "cipher: " + Utils.toHex(cipherText)+ " bytes: " + ctLength);
	Log.d(Main.TAG, "cipherText length: " + cipherText.length);
	
	byte[] correctedLengthCipherText = new byte[ctLength];
System.arraycopy(cipherText, 0, correctedLengthCipherText, 0, ctLength);
   	  return correctedLengthCipherText;

   	}

   	    public static byte [] aesDecrypt (byte [] toDecrypt) throws Exception {


   	 cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);

   	 byte[] plainText = new byte[cipher.getOutputSize(toDecrypt.length)];

        int ptLength = cipher.update(toDecrypt, 0, toDecrypt.length, plainText, 0);

        ptLength += cipher.doFinal(plainText, ptLength);

    	Log.d(Main.TAG, "plain: " + Utils.toHex(plainText)+ " bytes: " + ptLength);
   


   	  return plainText;

   	}
}
