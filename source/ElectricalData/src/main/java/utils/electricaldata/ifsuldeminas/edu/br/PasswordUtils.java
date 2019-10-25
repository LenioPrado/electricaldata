package utils.electricaldata.ifsuldeminas.edu.br;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.xml.bind.DatatypeConverter;

import org.apache.commons.codec.binary.Base64;

public class PasswordUtils {
		
	private PasswordUtils() {}

	public static String encriptyPassword(String password) throws NoSuchAlgorithmException{
		password+=password;
        MessageDigest digest = MessageDigest.getInstance("sha");
        byte[] hash = null;
        String newPassword = "";
        
		try {
			hash = digest.digest(password.getBytes("UTF-8"));
			newPassword = new String (Base64.encodeBase64(hash));
		} catch (UnsupportedEncodingException e) {
			ProjectLogger.log.error(e.getMessage());
			e.printStackTrace();
		}

        return newPassword;
	}

	public static boolean passwordMatchTest(String providedValue, String correctValue) throws NoSuchAlgorithmException{
		providedValue = encriptyPassword(providedValue);
		return providedValue.equalsIgnoreCase(correctValue);
	}

	public static String[] decode(String auth) {
        //Replacing "Basic THE_BASE_64" to "THE_BASE_64" directly
        auth = auth.replaceFirst("[B|b]asic ", "");
 
        //Decode the Base64 into byte[]
        byte[] decodedBytes = DatatypeConverter.parseBase64Binary(auth);
 
        //If the decode fails in any case
        if(decodedBytes == null || decodedBytes.length == 0){
            return null;
        }
 
        //Now we can convert the byte[] into a splitted array :
        //  - the first one is login,
        //  - the second one password
        return new String(decodedBytes).split(":", 2);
    }
}