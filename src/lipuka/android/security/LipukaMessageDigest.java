package lipuka.android.security;

import java.security.MessageDigest;

public class LipukaMessageDigest {
  public static byte[] calculateDigest(byte[] content) throws Exception {

    MessageDigest messageDigest = MessageDigest.getInstance("MD5");
    messageDigest.update(content);
    return messageDigest.digest();

  }
}