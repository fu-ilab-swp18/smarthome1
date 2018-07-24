package smarthome.fuberlin.de.repositories;

import org.springframework.data.repository.CrudRepository;

import smarthome.fuberlin.de.repositories.Sensor;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface SensorRepository extends CrudRepository<Sensor, Integer> {

}
