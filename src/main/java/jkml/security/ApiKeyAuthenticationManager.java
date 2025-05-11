package jkml.security;

import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.fasterxml.uuid.Generators;

public class ApiKeyAuthenticationManager implements AuthenticationManager {

	private final Logger logger = LoggerFactory.getLogger(ApiKeyAuthenticationManager.class);

	private final Map<String, Set<GrantedAuthority>> authorities;

	public ApiKeyAuthenticationManager() {
		var generator = Generators.randomBasedGenerator();
		var key1 = generator.generate().toString();
		var key2 = generator.generate().toString();
		authorities = Map.of(key1, Set.of(), key2, Set.of(new SimpleGrantedAuthority("authority1")));
		logger.info("Valid API keys: {}", authorities);
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		var apiKey = authentication.getPrincipal().toString();
		var auths = authorities.get(apiKey);
		if (auths != null) {
			return new ApiKeyAuthentication(apiKey, auths);
		}
		throw new BadCredentialsException("Invalid API key");
	}

}
