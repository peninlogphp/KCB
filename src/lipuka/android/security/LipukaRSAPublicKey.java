package lipuka.android.security;


import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;

import kcb.android.Main;






import android.util.Log;

public class LipukaRSAPublicKey {
    private BigInteger EXPONENT;
    private BigInteger MODULUS;
    public LipukaRSAPublicKey(String contents) {
		     Log.d(Main.TAG, "Public key: "+ contents);

        int linebreak = contents.indexOf("\n");
        EXPONENT = new BigInteger(contents.substring(0, linebreak).trim());
        MODULUS = new BigInteger(contents.substring(linebreak + 1).trim());
    }
    public BigInteger getExponent(){
    	return EXPONENT;
    }
    public BigInteger getModulus(){
    	return MODULUS;
    }
    
}

