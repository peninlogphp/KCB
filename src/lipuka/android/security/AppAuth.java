package lipuka.android.security;



import javax.crypto.Cipher;

import kcb.android.LipukaApplication;









public class AppAuth {

	LipukaApplication app;
	
	public AppAuth(LipukaApplication app){
		this.app = app;
	}
	

    public byte[] encrypt(byte[] plainText) throws Exception
    {
        Cipher           cipher = Cipher.getInstance("RSA/None/PKCS1Padding", "BC");

        // encryption step

        cipher.init(Cipher.ENCRYPT_MODE, app.loadRawBytes());

        byte[] cipherText = cipher.doFinal(plainText);


        return cipherText;
        // decryption step

       // cipher.init(Cipher.DECRYPT_MODE, privKey);

       // byte[] plainText = cipher.doFinal(cipherText);

        //System.out.println("plain : " + Utils.toHex(plainText));
    }
}
