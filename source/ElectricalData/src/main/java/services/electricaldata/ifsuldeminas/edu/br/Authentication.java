package services.electricaldata.ifsuldeminas.edu.br;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.Provider;

import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.apache.tomcat.util.http.fileupload.IOUtils;

import beans.electricaldata.ifsuldeminas.edu.br.User;
import utils.electricaldata.ifsuldeminas.edu.br.UserUtils;

@Provider
@PreMatching
public class Authentication implements ContainerRequestFilter {
	
	@Context
    private HttpServletRequest servletRequest;
	
	private boolean isPathOrMethodAllowed(String path, String method) {
		boolean allowed = false;
		
		if(path.equals("pages/index.html") || 
				path.contains("user/login") || 
				path.contains("user/create")) {
			allowed = true;
		}
		
		if(method.equals("GET") && (path.equals("application.wadl") || path.equals("application.wadl/xsd0.xsd"))) {
			allowed = true;
		}
		
		return allowed;
	}

	@Override
	public void filter(ContainerRequestContext containerRequest) throws WebApplicationException {

		getRequestJsonString(containerRequest);
		
        String method = containerRequest.getMethod();
        String path = containerRequest.getUriInfo().getPath(true);
        
        String userAgent = containerRequest.getHeaderString("User-Agent");
        
        System.out.println(String.format("Path: %s -- Method: %s ", path, method));
        System.out.println(String.format("User-Agent: %s", userAgent));
        
        
        if(userAgent.contains("Android")) {
        	System.out.println("Android Client!");
            return;
        }
        
        if(isPathOrMethodAllowed(path, method)){
        	System.out.println("Caminho ou método permitido");
            return;
        } 
	
		HttpSession session = servletRequest.getSession(false);
		 
        if (session == null) {
        	System.out.println("Sessão nula");
        	throw new WebApplicationException(Status.UNAUTHORIZED);
        } else {
        	User user = UserUtils.getUserInSession(session);
        	if(user == null) {
        		System.out.println("Usuário nulo");
        		throw new WebApplicationException(Status.UNAUTHORIZED);
        	}
        }
	}
	
	private String getRequestJsonString(ContainerRequestContext requestContext) {
		String json = "";
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if(requestContext.getEntityStream() != null) {
    		try {
    			IOUtils.copy(requestContext.getEntityStream(),baos);
    			byte[] jsonBytes = baos.toByteArray();
    			json = new String(jsonBytes, "UTF-8");
    			requestContext.setEntityStream( new ByteArrayInputStream(jsonBytes) );
    		} catch (UnsupportedEncodingException e) {
    			e.printStackTrace();
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
    		if(!json.toLowerCase().contains("application/pdf")) {
    			System.out.println(json);	
    		}    		
		}        
        return json;
	}
}