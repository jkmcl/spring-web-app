package jkml.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

class ApiKeyAuthenticationManagerTests {

	private static final String API_KEY = UUID.randomUUID().toString();

	private static final String AUTHORITY = "authority";

	private ApiKeyAuthenticationManager manager;

	@BeforeEach
	void beforeEach() {
		Map<String, Set<GrantedAuthority>> authorities = Map.of(API_KEY, Set.of(new SimpleGrantedAuthority(AUTHORITY)));
		manager = new ApiKeyAuthenticationManager(authorities);
	}

	@Test
	void testAuthenticate_success() {
		var auth = new ApiKeyAuthentication(API_KEY);

		var authResult = manager.authenticate(auth);

		assertEquals(API_KEY, authResult.getPrincipal().toString());
		assertEquals(1, authResult.getAuthorities().size());
		assertEquals(AUTHORITY, authResult.getAuthorities().iterator().next().getAuthority());
	}

	@Test
	void testAuthenticate_failure() {
		var auth = new ApiKeyAuthentication("INVALID_KEY");

		assertThrows(BadCredentialsException.class, () -> manager.authenticate(auth));
	}

}
