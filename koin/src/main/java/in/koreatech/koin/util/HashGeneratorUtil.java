package in.koreatech.koin.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashGeneratorUtil {
    public static String generateMD5(String message) {
        return hashString(message, "MD5");
    }

    public static String generateSHA256(String message) {
        return hashString(message, "SHA-256");
    }

    private static String hashString(String message, String algorithm){

        try {
            MessageDigest digest = MessageDigest.getInstance(algorithm);
            digest.update(message.getBytes());
            byte[] hashedBytes = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte hashedByte : hashedBytes) {
                StringBuilder h = new StringBuilder(Integer.toHexString(0xFF & hashedByte));
                while (h.length() < 2)
                    h.insert(0, "0");
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }

        return "";
    }
}

