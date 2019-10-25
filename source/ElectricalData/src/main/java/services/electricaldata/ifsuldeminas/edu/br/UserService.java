package services.electricaldata.ifsuldeminas.edu.br;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import javax.jws.WebService;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import beans.electricaldata.ifsuldeminas.edu.br.User;
import utils.electricaldata.ifsuldeminas.edu.br.PasswordUtils;
import utils.electricaldata.ifsuldeminas.edu.br.ProjectLogger;
import utils.electricaldata.ifsuldeminas.edu.br.UserUtils;

@WebService
@Path("/user")
public class UserService extends BaseService<User> {
	
	public UserService() {
		super(User.class);
	}
	
	@Override
	protected User beforeCreate(User user) {
		String password = user.getPassword();
		try {
			String cryptoPassword = PasswordUtils.encriptyPassword(password);
			user.setPassword(cryptoPassword);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return user;
	}
	
	@GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/logout")
    public Response logout() {
		UserUtils.removeUserFromSession(getSession());
		return Response.ok().build();
	}
	
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/login/{userEmail}/{userPassword}")
    public Response login(@PathParam("userEmail") String userEmail, @PathParam("userPassword") String userPassword) {
		ProjectLogger.log.info("login ");
		User user = get("SELECT t FROM User t WHERE t.email = '" + userEmail + "'");
		
		if (user != null) {
			boolean passwordIsValid = false;
			
			try {
				passwordIsValid = PasswordUtils.passwordMatchTest(userPassword, user.getPassword());
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
				return Response.serverError().entity(e.getMessage()).build();
			}
			
			if(passwordIsValid) {				
				ProjectLogger.log.info("Usuário Logado!!!!!");
				UserUtils.saveUserInSession(getSession(), user);
				String json = "";
				try {
					json = getJsonFormattedObject(user);
				} catch (IOException e) {
					e.printStackTrace();
					return Response.serverError().entity(e.getMessage()).build();	
				}
				return Response.ok().entity(json).build();
			} else {
				// Senha inválida
				ProjectLogger.log.info("invalid password ");
				UserUtils.removeUserFromSession(getSession());
				return Response.serverError().entity("Os dados de acesso informados estão incorretos!").build();
			}
		} else {
			// Conta inexistente
			ProjectLogger.log.info("account not found ");
			UserUtils.removeUserFromSession(getSession());
			return Response.serverError().entity("Não há uma conta associada ao e-mail informado!").build();
		}
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/roles")
    public Response getLoggedUserRoles() {
		User user = UserUtils.getUserInSession(getSession());
		if(user!=null) {
			String json = "";
			try {
				json = getJsonFormattedObject(user);
				return Response.ok().entity(json).build();
			} catch (IOException e) {
				e.printStackTrace();
				return Response.serverError().entity(e.getMessage()).build();	
			}
		} else {
			Response.serverError().entity("Usuário não logado!").build();
		}
		return Response.ok().build();
	}
}
