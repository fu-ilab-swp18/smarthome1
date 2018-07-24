package smarthome.fuberlin.de.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import smarthome.fuberlin.de.repositories.SmartHomeUser;
import smarthome.fuberlin.de.repositories.UserRepository;

@Component
public class SmartHomeAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	UserRepository userRepository;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;
		SmartHomeUser user = userRepository.findByUsername(token.getName());

		if (!user.getPassword().equalsIgnoreCase(token.getCredentials().toString())) {
			throw new BadCredentialsException("The Credentials are invalid");
		}

		return new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return UsernamePasswordAuthenticationToken.class.equals(authentication);
	}

}
