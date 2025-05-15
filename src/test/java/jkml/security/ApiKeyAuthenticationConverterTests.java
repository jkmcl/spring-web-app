package jkml.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import jakarta.servlet.http.HttpServletRequest;

class ApiKeyAuthenticationConverterTests {

	private final ApiKeyAuthenticationConverter converter = new ApiKeyAuthenticationConverter();

	private final HttpServletRequest mockRequest = mock(HttpServletRequest.class);

	@Test
	void testConvert_success() {
		var apiKey = UUID.randomUUID().toString();
		when(mockRequest.getHeader(ApiKeyAuthenticationConverter.API_KEY_HEADER_NAME)).thenReturn(apiKey);

		var authentication = converter.convert(mockRequest);

		assertEquals(apiKey, authentication.getPrincipal().toString());
	}

	@Test
	void testConvert_failure() {
		var authentication = converter.convert(mockRequest);

		assertNull(authentication);
	}

}
