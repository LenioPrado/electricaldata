package utils.electricaldata.ifsuldeminas.edu.br;

import javax.servlet.http.HttpSession;

import beans.electricaldata.ifsuldeminas.edu.br.User;

public class UserUtils {
	
	private static final String userKey = "user";
	
	public static void saveUserInSession(HttpSession session, User user) {
		session.setAttribute(userKey, user);
	}
	
	public static void removeUserFromSession(HttpSession session) {
		session.removeAttribute(userKey);
	}
	
	public static User getUserInSession(HttpSession session) {
		User user = (User)session.getAttribute(userKey);
		return user;
	}
}
