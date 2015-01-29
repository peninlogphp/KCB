package lipuka.android.security;


import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import kcb.android.Main;







import lipuka.android.util.Utils;
import android.util.Log;


public class SymmetricEncryption {

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
   
   public static void init () {

   	  SecureRandom sr = new SecureRandom();

   	  aesKey = new byte [16];

   	  sr.nextBytes(aesKey);

   	  aesInitV = new byte [16];

   	  sr.nextBytes(aesInitV);

           key = new SecretKeySpec(aesKey, "AES");
          ivSpec = new IvParameterSpec(aesInitV);
      try{   cipher = Cipher.getInstance("AES/CBC/NoPadding", "BC");


         cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
      }catch(Exception e){
    	  Log.d(Main.TAG, "Symmetric Init Exception", e);
      }
//symmetricBlockSize = cipher.getBlockSize();

   	}
   	    public static byte [] aesEncrypt (byte [] toEncrypt) {
   	    	byte[] cipherText = null;
    try {
    	cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);

    int symmetricBlockSize = cipher.getBlockSize();


    int remainder = (toEncrypt.length % symmetricBlockSize);
    int numBlocks = 0;
    if (remainder != 0){
          numBlocks = (toEncrypt.length / symmetricBlockSize) + 1;
    }
    else {
  numBlocks = (toEncrypt.length / symmetricBlockSize);
    }
          byte[] plaintext = new byte[numBlocks * symmetricBlockSize];
          System.arraycopy(toEncrypt, 0, plaintext, 0, toEncrypt.length);

          // encrypt!
           cipherText = new byte[numBlocks * symmetricBlockSize];
          for (int i = 0; i < cipherText.length; i += symmetricBlockSize) {
              cipher.update(plaintext, i, symmetricBlockSize, cipherText, i);
          }
   	    }catch(Exception e){
          	  Log.d(Main.TAG, "Symmetric Init Exception", e);
            }
   	  return cipherText;
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
