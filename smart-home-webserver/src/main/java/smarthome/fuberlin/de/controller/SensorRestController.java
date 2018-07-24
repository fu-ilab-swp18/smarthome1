package smarthome.fuberlin.de.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import smarthome.fuberlin.de.repositories.AlcSensor;
import smarthome.fuberlin.de.repositories.AlcSensorEvent;
import smarthome.fuberlin.de.repositories.AlcSensorEventRepository;
import smarthome.fuberlin.de.repositories.AlcSensorRepository;
import smarthome.fuberlin.de.repositories.Sensor;
import smarthome.fuberlin.de.repositories.SensorEvent;
import smarthome.fuberlin.de.repositories.SensorEventRepository;
import smarthome.fuberlin.de.repositories.SensorRepository;
import smarthome.fuberlin.de.repositories.SmartHomeUser;
import smarthome.fuberlin.de.repositories.UserRepository;

@RestController
@RequestMapping("/api")
public class SensorRestController {

	@Autowired
	SensorRepository sensorRepository;

	@Autowired
	SensorEventRepository sensorEventRepository;
	
	@Autowired
	AlcSensorRepository alcSensorRepository;
	
	@Autowired
	AlcSensorEventRepository alcSensorEventRepository;
	
	@Autowired
	UserRepository userRepository;

	@PostConstruct
	public void init() {

	}

	// AUTHENTICATION METHODS

	@PostMapping("/register")
	public RestWrapperDTO register(@RequestBody SmartHomeUser user) {
		System.out.println("Wir registrieren einen Neuen USER !!!!!!!");

		user.setRole("ROLE_USER");
		userRepository.save(user);

		// Authentication auth = new UsernamePasswordAuthenticationToken(user,
		// user.getPassword(), user.getAuthorities());
		// SecurityContextHolder.getContext().setAuthentication(auth);

		RestWrapperDTO wrapperDTO = new RestWrapperDTO();
		wrapperDTO.setSuccess(true);
		return wrapperDTO;
	}

	@RequestMapping(value = "/logout")
	public RestWrapperDTO logout(HttpSession session) {
		System.out.println("Log out the user");

		session.invalidate();

		RestWrapperDTO wrapperDTO = new RestWrapperDTO();
		wrapperDTO.setSuccess(true);
		return wrapperDTO;
	}

	@RequestMapping("/user")
	public Principal user(Principal user) {
		return user;
	}

	@RequestMapping("/resource")
	public Map<String, Object> home() {

		SmartHomeUser user = (SmartHomeUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		System.out.println("Username : " + user.getUsername());

		Map<String, Object> model = new HashMap<String, Object>();
		model.put("id", UUID.randomUUID().toString());
		model.put("content", "Hello World");
		return model;
	}

	@PostMapping("/sensorevent")
	public RestWrapperDTO handleSensorEvent(@RequestBody SensorEvent sensorEvent) {

		System.out.println("Hello. I am the Sensor Event Rest Controller");
		sensorEvent.setTime(LocalDateTime.now());

		System.out.println(sensorEvent);
		sensorEventRepository.save(sensorEvent);
		
		Sensor sensor = sensorRepository.findById(sensorEvent.getSensor().getId()).get();
		
		System.out.println(sensor);
		
		SmartHomeUser user = sensor.getUser();
		
		System.out.println(user);
		
		String deviceToken = user.getDeviceToken();
		
		System.out.println(deviceToken);
		
		
		String push_cmd =
		         "curl -i -H 'Content-type: application/json' -H 'Authorization: key=AAAAHAEvsFU:APA91bFvAEgtDNrKPlCvPvUyCXCYx6Mn1oMb0WP7E869zOppnJHRfpkVLj7u0W_7VnguQsH_PYYdo5iQ8euqGt1BT4mQlcl4Y0fBJixHgcawqkwFqBsSmXwxhH-227OzAkToVmhg0o8WC45IHzHmKZqmdwY5MSt-DA' -XPOST https://fcm.googleapis.com/fcm/send -d '{"
		         + " \"registration_ids\":[\"" + deviceToken + "\"], "
		         + " \"notification\": { "
		         + " \"title\":\"SmartHome - Post\", "
		         + " \"body\":\"Du hast einen Brief bekommen!\" "
		         + " } "
		         + "}' "
		;
		
		System.out.println(push_cmd);
		
		PrintWriter writer;
		try {
			writer = new PrintWriter("/tmp/push.sh", "UTF-8");
			writer.println(push_cmd);
			writer.close();
            Runtime rt = Runtime.getRuntime();
			rt.exec("bash /tmp/push.sh");
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	

		RestWrapperDTO wrapperDTO = new RestWrapperDTO();
		wrapperDTO.setSuccess(true);
		return wrapperDTO;
	}

	@PostMapping("/emptymailbox")
	public RestWrapperDTO handleMailboxEmptying(@RequestBody Sensor sensorDummy) {
		System.out.println("You're mailbox is empty...");

		System.out.println("Sensordummy ID : " + sensorDummy.getId());

		Sensor sensor = sensorRepository.findById(sensorDummy.getId()).get();

		for (SensorEvent currentSensorEvent : sensorEventRepository.findAll()) {

			if (currentSensorEvent.getSensor().equals(sensor)) {
				currentSensorEvent.setOld(true);
				sensorEventRepository.save(currentSensorEvent);
			}

		}

		RestWrapperDTO wrapperDTO = new RestWrapperDTO();
		wrapperDTO.setSuccess(true);
		return wrapperDTO;
	}
	
	@PostMapping("/alcsensorevent")
	public RestWrapperDTO handleAlcSensorEvent(@RequestBody AlcSensorEvent alcSensorEvent) {


		System.out.println("Hello. I am the Sensor Event Rest Controller");

		System.out.println(alcSensorEvent);
		alcSensorEventRepository.save(alcSensorEvent);
		
		// ToDo : Different sensor Types
		Sensor sensor = sensorRepository.findById(alcSensorEvent.getAlcSensor().getId()).get();
		
		System.out.println(sensor);
		
		SmartHomeUser user = sensor.getUser();
		
		System.out.println(user);
		
		String deviceToken = user.getDeviceToken();
		
		System.out.println(deviceToken);
		
		
		String push_cmd =
		         "curl -i -H 'Content-type: application/json' -H 'Authorization: key=AAAAHAEvsFU:APA91bFvAEgtDNrKPlCvPvUyCXCYx6Mn1oMb0WP7E869zOppnJHRfpkVLj7u0W_7VnguQsH_PYYdo5iQ8euqGt1BT4mQlcl4Y0fBJixHgcawqkwFqBsSmXwxhH-227OzAkToVmhg0o8WC45IHzHmKZqmdwY5MSt-DA' -XPOST https://fcm.googleapis.com/fcm/send -d '{"
		         + " \"registration_ids\":[\"" + deviceToken + "\"], "
		         + " \"notification\": { "
		         + " \"title\":\"SmartHome - Alc\", "
		         + " \"body\":\"Du bist betrunken!!!\" "
		         + " } "
		         + "}' "
		;
		
		System.out.println(push_cmd);
		
		PrintWriter writer;
		try {
			writer = new PrintWriter("/tmp/push.sh", "UTF-8");
			writer.println(push_cmd);
			writer.close();
            Runtime rt = Runtime.getRuntime();
			rt.exec("bash /tmp/push.sh");
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	

		RestWrapperDTO wrapperDTO = new RestWrapperDTO();
		wrapperDTO.setSuccess(true);
		return wrapperDTO;
	}

	/**
	 * Returns a Sensor[] array containing all the Sensors belonging to the
	 * currently logged in principal user.
	 * 
	 * @return returns an array of Sensors
	 */
	@RequestMapping("/getsensorevents")
	public List<SensorEvent> userSensors(@RequestBody Sensor sensorDummy) {

		System.out.println("Gathering Sensors...");
		SmartHomeUser user = (SmartHomeUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		System.out.println("User  : " + user.getUsername());
		List<SensorEvent> sr = new ArrayList<>();

		System.out.println("User ID :" + user.getId());


		for (Sensor sensor : sensorRepository.findAll()) {
			if (sensor.getUser().equals(user)) {
				
				System.out.println("Found a fitting sensor!!");

				for (SensorEvent currentSensorEvent : sensorEventRepository.findAll()) {
					if (currentSensorEvent.getSensor().equals(sensor)) {
						sr.add(currentSensorEvent);
					}
				}
			}		
		}


		System.out.println("Gathered all Sensors!");

		return sr;
	}
	
	
	@RequestMapping("/getalcsensorevents")
	public List<AlcSensorEvent> getAlcSensorEvents(@RequestBody AlcSensor sensorDummy) {

		System.out.println("Gathering Sensors...");
		SmartHomeUser user = (SmartHomeUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		System.out.println("User  : " + user.getUsername());
		List<AlcSensorEvent> sr = new ArrayList<>();

		System.out.println("User ID :" + user.getId());

		System.out.println("Sensordummy ID : " + sensorDummy.getId());

		AlcSensor sensor = alcSensorRepository.findById(sensorDummy.getId()).get(); 
		
		if (sensor.getUser().equals(user)) {
			for (AlcSensorEvent currentSensorEvent : alcSensorEventRepository.findAll()) {

				if (currentSensorEvent.getAlcSensor().equals(sensor)) {
					sr.add(currentSensorEvent);
				}
			}
		}

		System.out.println("Gathered all Sensors!");

		return sr;
	}
	
	
	

	public class RestWrapperDTO {
		protected boolean success;

		public boolean isSuccess() {
			return success;
		}

		public void setSuccess(boolean value) {
			success = value;
		}
	}

	public class RestErrorDTO extends RestWrapperDTO {
		private Map<String, String> errors;

		public Map<String, String> getErrors() {
			return errors;
		}

		public void setErrors(Map<String, String> value) {
			errors = value;
		}
	}

}