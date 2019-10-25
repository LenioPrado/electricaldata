package utils.electricaldata.ifsuldeminas.edu.br;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class EMFactory {

    private static final String PERSISTENCE_UNIT_NAME = "electricalData";
    private static EntityManagerFactory factory;
    
    private static EMFactory _instance;
    
    private EMFactory() {
    	factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
    }
    
    public static EMFactory getInstance() {
    	if (_instance == null) {
    		_instance = new EMFactory();
		}
    	return _instance;
    }
    
    public EntityManager getEntityManager() {
    	return factory.createEntityManager();
    }
}