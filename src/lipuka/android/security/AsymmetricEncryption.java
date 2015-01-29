package lipuka.android.security;



import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPublicKeySpec;

import javax.crypto.Cipher;

import kcb.android.Main;







import android.util.Log;


public class AsymmetricEncryption {

	String publicKey;
	
	public AsymmetricEncryption(String publicKey){
		this.publicKey = publicKey;
	}
	

    public byte[] encrypt(byte[] plainText)
    {
    	byte[] cipherText = null;
     try{
    	 Cipher cipher = Cipher.getInstance("RSA/None/PKCS1Padding", "BC");
        LipukaRSAPublicKey rsaKey = new LipukaRSAPublicKey(publicKey);
        // create the keys
        KeyFactory keyFactory = KeyFactory.getInstance("RSA", "BC");

        RSAPublicKeySpec pubKeySpec = new RSAPublicKeySpec(rsaKey.getModulus(),
        		rsaKey.getExponent());
      /**  RSAPrivateKeySpec privKeySpec = new RSAPrivateKeySpec(
                new BigInteger("d46f473a2d746537de2056ae3092c451", 16),
                new BigInteger("57791d5430d593164082036ad8b29fb1", 16));
*/
        
        RSAPublicKey pubKey = (RSAPublicKey)keyFactory.generatePublic(pubKeySpec);
       // RSAPrivateKey privKey = (RSAPrivateKey)keyFactory.generatePrivate(
         //                                                             privKeySpec);


        // encryption step

        cipher.init(Cipher.ENCRYPT_MODE, pubKey);

         cipherText = cipher.doFinal(plainText);

    }catch(Exception e){
  	  Log.d(Main.TAG, "Symmetric Init Exception", e);
    }
        return cipherText;
        // decryption step

       // cipher.init(Cipher.DECRYPT_MODE, privKey);

       // byte[] plainText = cipher.doFinal(cipherText);

        //System.out.println("plain : " + Utils.toHex(plainText));
    }
}
