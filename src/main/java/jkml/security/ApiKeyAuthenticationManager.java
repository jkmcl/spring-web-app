package jkml.security;

import java.util.Map;
import java.util.Set;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;

public class ApiKeyAuthenticationManager implements AuthenticationManager {

	private final Map<String, Set<GrantedAuthority>> authorities;

	public ApiKeyAuthenticationManager() {
		this(Map.of());
	}

	public ApiKeyAuthenticationManager(Map<String, Set<GrantedAuthority>> authorities) {
		this.authorities = Map.copyOf(authorities);
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
