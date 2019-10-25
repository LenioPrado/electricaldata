package services.electricaldata.ifsuldeminas.edu.br;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.hibernate.exception.ConstraintViolationException;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import utils.electricaldata.ifsuldeminas.edu.br.EMFactory;

public class BaseService<T> {

	@Context
    private HttpServletRequest request;	
	
	private final Class<T> classType;
	
	public BaseService(Class<T> classType){
		this.classType = classType;
	}
	
	public BaseService(Class<T> classType, HttpServletRequest request){
		this.classType = classType;
		this.request = request;
	}
	
	protected HttpSession getSession() {
		return request.getSession();
	}
	
	protected EntityManager getEM() {
		return EMFactory.getInstance().getEntityManager();
	}
	
	protected TypedQuery<T> createQuery(String query){
		return getEM().createQuery(query, classType);
	}

    protected T get(String query) {
		T entity = null;
		try {
			TypedQuery<T> q = createQuery(query);
			entity = q.getSingleResult();
		} catch (NoResultException e) {
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return entity;
    }
    
    protected Response insert(String query) {
    	EntityManager em = getEM(); 
		try {
			em.getTransaction().begin();
			em.createNativeQuery(query).executeUpdate();
			em.getTransaction().commit();
			return Response.ok(getJsonFormattedMessage("Registro Inserido com Sucesso!"), MediaType.APPLICATION_JSON).build();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
    }  
    
    protected Response insert(List<String> queries) {
    	EntityManager em = getEM(); 
    	int count = 0;   	
		try {
			em.getTransaction().begin();
			for (String query : queries) {
				em.createNativeQuery(query).executeUpdate();
				count++;
			}			
			em.getTransaction().commit();
			System.out.println(String.format("Registros inseridos: %d", count));
			return Response.ok(getJsonFormattedMessage("Registros Inserido com Sucesso!"), MediaType.APPLICATION_JSON).build();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
    }    

    protected Response delete(String query) {
    	EntityManager em = getEM(); 
    	int count;   	
		try {
			em.getTransaction().begin();
			count = em.createQuery(query).executeUpdate();
			em.getTransaction().commit();
			System.out.println(String.format("Registros apagados: %d", count));
			return Response.ok(getJsonFormattedMessage("Registro Excluído com Sucesso!"), MediaType.APPLICATION_JSON).build();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
    }
    
	@SuppressWarnings("unchecked")
	protected List<Object[]> listRawData(String query) {
		Query q = getEM().createNativeQuery(query);
		return q.getResultList();
	}
	
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<T> listAll() {
		String query = "select t from "+ this.classType.getSimpleName() +" t";
		return listFiltering(query);
    }

    protected List<T> listFiltering(String query) {
		List<T> entityList = null;
		try {
			TypedQuery<T> q = getEM().createQuery(query, classType);
			entityList = q.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return entityList;
    }
    
	@POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/create")
    public Response create(T entity) {
		EntityManager em = getEM(); 
		
		beforeCreate(entity);
		
    	try {
    		em.getTransaction().begin();
			em.persist(entity);
			em.getTransaction().commit();
			
			afterCreate(entity);
			
			return Response.ok(getJsonFormattedObject(entity), MediaType.APPLICATION_JSON).build();
		} catch (PersistenceException e) {
			String message = getCreateErrorMessage(getCauseError(e));
			return Response.serverError().entity(message).build();
		} catch (Exception e) {
			String message = getCreateErrorMessage(getCauseError(e));
			return Response.serverError().entity(message).build();
		}		
    }	
	
	@POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/delete")
    public Response delete(T entity) {
		EntityManager em = getEM(); 

		beforeDelete(entity);
		
    	try {
    		em.getTransaction().begin();
			em.remove(em.contains(entity) ? entity : em.merge(entity));
			em.getTransaction().commit();
			return Response.ok(getJsonFormattedMessage(getDeleteSuccessMessage()), MediaType.APPLICATION_JSON).build();
		} catch (PersistenceException e) {
			String message = getDeleteErrorMessage(getCauseError(e));
			return Response.serverError().entity(message).build();
		} catch (Exception e) {
			String message = getDeleteErrorMessage(getCauseError(e));
			return Response.serverError().entity(message).build();
		}
    }

	@POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/edit")
    public Response edit(T entity) {
		EntityManager em = getEM(); 
		
		beforeEdit(entity);
		
    	try {
    		em.getTransaction().begin();
    		em.merge(entity);
			em.getTransaction().commit();
			return Response.ok(getJsonFormattedMessage(getEditSuccessMessage()), MediaType.APPLICATION_JSON).build();
		} catch (PersistenceException e) {
			String message = getEditErrorMessage(getCauseError(e));
			return Response.serverError().entity(message).build();
		} catch (Exception e) {
			String message = getEditErrorMessage(getCauseError(e));
			return Response.serverError().entity(message).build();
		} 
    }

	private Exception getCauseError(Exception e) {
		e.printStackTrace();
		if(e.getCause() instanceof ConstraintViolationException) {
			ConstraintViolationException cve = (ConstraintViolationException)e.getCause();
			return cve.getSQLException();
		} else if(e.getCause() instanceof PersistenceException) {
			PersistenceException pe = (PersistenceException)e.getCause();
			return getCauseError(pe);
		}
		return e;
	}
	
	protected String getJsonFormattedMessage(String message) {
		return "{\"message\": \""+message+"\"}";
	}
	
	protected String getJsonFormattedObject(Object object) throws IOException {
		try {
			return new ObjectMapper().writer().withDefaultPrettyPrinter().writeValueAsString(object);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
			throw e;
		} catch (JsonMappingException e) {
			e.printStackTrace();
			throw e;
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	protected T beforeCreate(T entity) {
		return entity;
	}
	
	protected T afterCreate(T entity) {
		return entity;
	}
	
	protected T beforeEdit(T entity) {
		return entity;
	}
	
	protected T beforeDelete(T entity) {
		return entity;
	}

	protected String getEditSuccessMessage() {
		return "Registro Editado com Sucesso!";
	}
	
	protected String getDeleteSuccessMessage() {
		return "Registro Excluído com Sucesso!";
	}	
	
	protected String getCreateErrorMessage(Exception e) {
		return e.getMessage();
	}
	
	protected String getDeleteErrorMessage(Exception e) {
		return e.getMessage();
	}
	
	protected String getEditErrorMessage(Exception e) {
		return e.getMessage();
	}
}
