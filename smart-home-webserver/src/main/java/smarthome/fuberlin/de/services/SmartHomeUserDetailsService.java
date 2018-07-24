package smarthome.fuberlin.de.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import smarthome.fuberlin.de.repositories.SmartHomeUser;
import smarthome.fuberlin.de.repositories.UserRepository;

@Service
public class SmartHomeUserDetailsService implements UserDetailsService{
	
    @Autowired
    private UserRepository userRepository;
 
    @Override
    public UserDetails loadUserByUsername(String username) {
        SmartHomeUser user = userRepository.findByUsername(username);

        return user;  // also possible to take your own User Class ->  see Principal class
    }
}
