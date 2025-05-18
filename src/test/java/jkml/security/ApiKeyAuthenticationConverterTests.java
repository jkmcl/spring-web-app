package jkml.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

class ApiKeyAuthenticationConverterTests {

	private final ApiKeyAuthenticationConverter converter = new ApiKeyAuthenticationConverter();

	@Test
	void testConvert_success() {
		var apiKey = UUID.randomUUID().toString();
		var request = new MockHttpServletRequest();
		request.addHeader(ApiKeyAuthenticationConverter.API_KEY_HEADER_NAME, apiKey);

		var auth = converter.convert(request);

		assertEquals(apiKey, auth.getPrincipal().toString());
	}

	@Test
	void testConvert_failure() {
		var request = new MockHttpServletRequest();

		var auth = converter.convert(request);

		assertNull(auth);
	}

}
