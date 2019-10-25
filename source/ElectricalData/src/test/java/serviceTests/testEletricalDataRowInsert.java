package serviceTests;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import beans.electricaldata.ifsuldeminas.edu.br.Place;
import services.electricaldata.ifsuldeminas.edu.br.ElectricalDataService;
import services.electricaldata.ifsuldeminas.edu.br.PlaceService;
import utils.electricaldata.ifsuldeminas.edu.br.FileResource;
import utils.electricaldata.ifsuldeminas.edu.br.HttpClientHelper;

public class testEletricalDataRowInsert {
	
	static HttpClientHelper clientHelper;
	static ElectricalDataService electricalDataService;
	static PlaceService placeService;
	
	@Before
	public void before() throws Exception {

	}
	
	@After
	public void closeEntityManager() throws Exception {

	}
	
	@BeforeClass
	public static void beforeClass() {
		clientHelper = new HttpClientHelper();
		electricalDataService = new ElectricalDataService();
		placeService = new PlaceService();
	}
	
	@Test
	public void insertMeasurement() {
		
		try {
			String fullPath = "E:\\Develop\\electricaldata\\ImportedData\\1\\CERIn-Data-Values.csv";
			List<Place> places = placeService.listAll();
			
			List<String> fileLines = FileResource.getFileLines(fullPath);
			
			if(places.size() == 0) {
				System.out.println("Não existem 'places' cadastrados! Saindo...");
				return;
			}
			
			if(fileLines.size() <= 1) {
				System.out.println(String.format("Não foi possível ler linhas do arquivo '%s'! Saindo...", fullPath));
				return;
			}		
	
			String measurement = fileLines.get(1);
			boolean wasInserted = electricalDataService.insertMeasurement(places.get(0).getPlaceId(), measurement);
			if(wasInserted) {
				System.out.println("Registro inserido com sucesso!");
			} else {
				System.out.println("Falha ao inserir registro!");
			}			
		} 
		catch (Exception e) {			
			e.printStackTrace();
		}
	}
}