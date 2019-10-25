package services.electricaldata.ifsuldeminas.edu.br;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.json.JSONArray;
import org.json.JSONObject;

import beans.electricaldata.ifsuldeminas.edu.br.ElectricalData;
import utils.electricaldata.ifsuldeminas.edu.br.FileResource;

@Path("/electricaldata")
public class ElectricalDataService extends BaseService<ElectricalData> {

	public ElectricalDataService() {
		super(ElectricalData.class);
	}
	
	public ElectricalDataService(HttpServletRequest request) {
		super(ElectricalData.class, request);
	}
	
	@GET
    @Produces(MediaType.APPLICATION_JSON)
	@Path("/getByPlaceAndDateInterval/{placeId}/{startDate}")
	public List<ElectricalData> getByPlaceAndDateInterval(
			@PathParam("placeId") int userId, 
			@PathParam("startDate") String startDate, 
			@QueryParam("endDate") String endDate,
			@QueryParam("dataId") String dataId){
		String query = String.format("SELECT ed FROM ElectricalData ed "
				+ "WHERE ed.place.placeId = %d AND "
				+ "ed.measurementDateTime >= '%s 00:00:00'",
				userId, startDate);
		
		if (endDate != null && !endDate.isEmpty()) {
			query += String.format(" AND ed.measurementDateTime <= '%s 23:59:59'", endDate);	
		}				
		
		if (dataId != null && !dataId.isEmpty()) {
			query = String.format("%s AND ed.dataId > %s", query, dataId);
		}
		
		return listFiltering(query);		
	}
	
	@GET
    @Produces(MediaType.APPLICATION_JSON)
	@Path("/getPlacesMeasurements/")
	public Response getPlacesMeasurements() {
		String query = "SELECT DISTINCT ed.place_id, p.name, ed.measurement_date_time FROM electrical_data ed" + 
				" INNER JOIN place p ON ed.place_id = p.place_id";		

		JSONArray jsonArr = new JSONArray();		
		
		for (Object[] data : listRawData(query)) {
			JSONObject json = new JSONObject();
			json.put("place_id", data[0]);
			json.put("name", data[1]);
			json.put("measurement_date_time", data[2]);
			jsonArr.put(json);
		}
		
		return Response.ok(jsonArr.toString(), MediaType.APPLICATION_JSON).build();
	}

	@POST
    @Path("/insertMeasurement")
	public boolean insertMeasurement(
			@QueryParam("placeId") int placeId,
			String measurement) throws Exception {
		String[] stringValues = FileResource.processLine(measurement);		
		return insertRowInDatabase(placeId,stringValues);
	}
    
	public void insertRowsInDatabase(int placeId, List<String[]> lineValues)
	{
		try
		{
			List<String> queries = new ArrayList<>();
			for (int i = 0; i < lineValues.size(); i++)
			{
				String query = getPartialQuery() + getLineValues(placeId, lineValues.get(i));
				queries.add(query);				
			}
			insert(queries);
		}
		catch (Exception ex)
		{
			throw ex;
		}
	}
	
	public boolean insertRowInDatabase(int placeId, String[] lineValue) {
		try
		{
			String query = getPartialQuery() + getLineValues(placeId, lineValue);
			
			Response response = insert(query);
			
			return response.getStatus() == Status.OK.getStatusCode();
		}
		catch (Exception ex)
		{
			throw ex;
		}
	}
	
	private String getPartialQuery() {
		String tableName = "electrical_data";
		String databaseTableQuery = String.format("INSERT INTO %s ", tableName);
		String fieldsName = "(place_id, measurement_date_time," + 
				" current_phase_a, current_phase_b, current_phase_c, current_total," + 
				" factor_power, frequency, apparent_power, active_power, reactive_power," + 
				" energy_production, phase_voltage_ab, phase_voltage_bc, phase_voltage_ca)";
		
		return databaseTableQuery + fieldsName;
	}
	
	private String getLineValues(int placeId, String[] lineValue) {
		String values = rowToString(lineValue);				
		return String.format(" VALUES (%d, %s);", placeId, values);
	}

	private String rowToString(Object[] array)
    {
        String data = "";

        for (int i = 0; i < array.length; i++)
        {
            data += array[i].toString().replace(',','.');
            if(i < array.length - 1)
            {
                data += ",";
            }
        }

        return data;
    }
}