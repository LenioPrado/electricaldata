package services.electricaldata.ifsuldeminas.edu.br;

import javax.ws.rs.Path;

import beans.electricaldata.ifsuldeminas.edu.br.Place;

@Path("/place")
public class PlaceService extends BaseService<Place> {

	public PlaceService() {
		super(Place.class);
	}
	
	protected String getDeleteErrorMessage(Exception exception) {
		if (exception.getMessage().toLowerCase().contains("electrical_data")) {
			return "Não é possível excluir pois existem dados para este local!";
		}
		return exception.getMessage();
	}
}