package beans.electricaldata.ifsuldeminas.edu.br;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;


/**
 * The persistent class for the place database table.
 * 
 */
@Entity
@NamedQuery(name="Place.findAll", query="SELECT p FROM Place p")
public class Place implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="place_id")
	private int placeId;

	private String meter;

	private String name;

	//bi-directional many-to-one association to ElectricalData
	@OneToMany(mappedBy="place")
	@JsonIgnore
	private List<ElectricalData> electricalData;

	public Place() {
	}

	public int getPlaceId() {
		return this.placeId;
	}

	public void setPlaceId(int placeId) {
		this.placeId = placeId;
	}

	public String getMeter() {
		return this.meter;
	}

	public void setMeter(String meter) {
		this.meter = meter;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<ElectricalData> getElectricalData() {
		return this.electricalData;
	}

	public void setElectricalData(List<ElectricalData> electricalData) {
		this.electricalData = electricalData;
	}

	public ElectricalData addElectricalData(ElectricalData electricalData) {
		getElectricalData().add(electricalData);
		electricalData.setPlace(this);

		return electricalData;
	}

	public ElectricalData removeElectricalData(ElectricalData electricalData) {
		getElectricalData().remove(electricalData);
		electricalData.setPlace(null);

		return electricalData;
	}
}