package smarthome.fuberlin.de.repositories;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class AlcSensorEvent {
	
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;

    private String value;

    @ManyToOne
    private AlcSensor alcSensor;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public AlcSensor getAlcSensor() {
		return alcSensor;
	}

	public void setAlcSensor(AlcSensor alcSensor) {
		this.alcSensor = alcSensor;
	}
}
