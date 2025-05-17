package jkml.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

class ApiKeyAuthenticationTests {

	@Test
	void test() {
		var apiKey = "hello";
		var auth = new ApiKeyAuthentication(apiKey);
		assertFalse(auth.isAuthenticated());
		assertEquals(apiKey, auth.getPrincipal());
		assertNull(auth.getCredentials());
	}

	@Test
	void test_authenticated() {
		var apiKey = "hello";
		var auth = new ApiKeyAuthentication(apiKey, List.of());
		assertTrue(auth.isAuthenticated());
		assertEquals(apiKey, auth.getPrincipal());
		assertNull(auth.getCredentials());
	}

}
