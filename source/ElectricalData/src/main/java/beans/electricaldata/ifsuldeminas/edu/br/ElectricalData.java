package beans.electricaldata.ifsuldeminas.edu.br;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * The persistent class for the electrical_data database table.
 * 
 */
@Entity
@Table(name="electrical_data")
@NamedQuery(name="ElectricalData.findAll", query="SELECT e FROM ElectricalData e")
public class ElectricalData implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="data_id")
	private int dataId;

	@Column(name="current_total")
	private float currentTotal;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="measurement_date_time")
	private Date measurementDateTime;

	@Column(name="phase_voltage_ab")
	private float phaseVoltageAb;

	@Column(name="phase_voltage_bc")
	private float phaseVoltageBc;

	@Column(name="phase_voltage_ca")
	private float phaseVoltageCa;

	//bi-directional many-to-one association to Place
	@ManyToOne
	@JoinColumn(name="place_id")
	private Place place;

	public ElectricalData() {
	}

	public int getDataId() {
		return this.dataId;
	}

	public void setDataId(int dataId) {
		this.dataId = dataId;
	}

	public float getCurrentTotal() {
		return this.currentTotal;
	}

	public void setCurrentTotal(float currentTotal) {
		this.currentTotal = currentTotal;
	}

	public Date getMeasurementDateTime() {
		return this.measurementDateTime;
	}

	public void setMeasurementDateTime(Date measurementDateTime) {
		this.measurementDateTime = measurementDateTime;
	}

	public float getPhaseVoltageAb() {
		return this.phaseVoltageAb;
	}

	public void setPhaseVoltageAb(float phaseVoltageAb) {
		this.phaseVoltageAb = phaseVoltageAb;
	}

	public float getPhaseVoltageBc() {
		return this.phaseVoltageBc;
	}

	public void setPhaseVoltageBc(float phaseVoltageBc) {
		this.phaseVoltageBc = phaseVoltageBc;
	}

	public float getPhaseVoltageCa() {
		return this.phaseVoltageCa;
	}

	public void setPhaseVoltageCa(float phaseVoltageCa) {
		this.phaseVoltageCa = phaseVoltageCa;
	}

	public Place getPlace() {
		return this.place;
	}

	public void setPlace(Place place) {
		this.place = place;
	}
}