package jkml.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

class ApiKeyAuthenticationManagerTests {

	@Test
	void testAuthenticate_success() {
		var apiKey = "key1";
		var authority = new SimpleGrantedAuthority("authority1");
		Map<String, Set<GrantedAuthority>> authorities = Map.of(apiKey, Set.of(authority));
		var manager = new ApiKeyAuthenticationManager(authorities);

		var auth = new ApiKeyAuthentication(apiKey);
		var authResult = manager.authenticate(auth);

		assertEquals(apiKey, authResult.getPrincipal().toString());
		assertEquals(1, authResult.getAuthorities().size());
		assertEquals(authority, authResult.getAuthorities().iterator().next());
	}

	@Test
	void testAuthenticate() {
		var manager = new ApiKeyAuthenticationManager();
		var auth = new ApiKeyAuthentication("a");
		assertThrows(BadCredentialsException.class, () -> manager.authenticate(auth));
	}

}
