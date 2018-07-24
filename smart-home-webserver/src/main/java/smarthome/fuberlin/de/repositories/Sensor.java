package smarthome.fuberlin.de.repositories;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.validator.constraints.Length;

@Entity
public class Sensor {
	
	
	public Sensor() {
		
	}
	
	public Sensor(Integer id) {
		this.setId(id);
	}
	
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;

    @Column(nullable = false, unique = true)
    @Length(max = 20)
    private String name;

    private String description;
        
    @ManyToOne
    private SmartHomeUser user;

	public SmartHomeUser getUser() {
		return user;
	}

	public void setUser(SmartHomeUser user) {
		this.user = user;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	
	@Override
	public boolean equals(Object o) {
		if (this.getId() == ((Sensor)o).getId()) {
			return true;
		} else {
			return false;
		}
	}
    

    
}

