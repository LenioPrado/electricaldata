package serviceTests;

import javax.ws.rs.core.Response;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import services.electricaldata.ifsuldeminas.edu.br.UserService;
import utils.electricaldata.ifsuldeminas.edu.br.HttpClientHelper;

public class testUserLoginService {
	
	static HttpClientHelper clientHelper;
	static UserService userService;
	
	@Before
	public void before() throws Exception {

	}
	
	@After
	public void closeEntityManager() throws Exception {

	}
	
	@BeforeClass
	public static void beforeClass() {
		clientHelper = new HttpClientHelper();
		userService = new UserService();
	}
	
	@Test
	public void sendNotification() {
		Response response = userService.login("joao", "112233");
		System.out.println(response.getEntity());
	}
}
