package serviceTests;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import beans.electricaldata.ifsuldeminas.edu.br.ElectricalData;
import services.electricaldata.ifsuldeminas.edu.br.ElectricalDataService;
import services.electricaldata.ifsuldeminas.edu.br.UserService;
import utils.electricaldata.ifsuldeminas.edu.br.HttpClientHelper;

public class testUserService {
	
	static HttpClientHelper clientHelper;
	static UserService userService;
	static ElectricalDataService flyDataService;
	
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
		flyDataService = new ElectricalDataService();
	}
	
	@Test
	public void sendNotification() {		

		List<ElectricalData> electricalsData = flyDataService.listAll();
		ElectricalData electricalData = electricalsData.get(0);
		
		String electricalDataInfo = String.format("###ElectricalDataId Id: %d -- Corrente Total: %f -- Tensão Fase AB: %f -- Tensão Fase BC: %f -- Tensão Fase CA: %f", 
				electricalData.getDataId(), electricalData.getCurrentTotal(), electricalData.getPhaseVoltageAb(), electricalData.getPhaseVoltageBc(), electricalData.getPhaseVoltageCa());
		System.out.println(electricalDataInfo);
	}
}
