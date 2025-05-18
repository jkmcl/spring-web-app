package jkml.security;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

class ApiKeyAuthenticationFilterTests {

	private static final String API_KEY = UUID.randomUUID().toString();

	private final ApiKeyAuthenticationFilter filter = new ApiKeyAuthenticationFilter(
			new ApiKeyAuthenticationManager(Map.of(API_KEY, Set.of(new SimpleGrantedAuthority("authority")))));

	private void testDoFilterInternal(HttpServletRequest request, boolean authenticated)
			throws IOException, ServletException {
		var mockResponse = mock(HttpServletResponse.class);
		var mockFilterChain = mock(FilterChain.class);
		var securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();
		securityContextHolderStrategy.clearContext();

		filter.doFilterInternal(request, mockResponse, mockFilterChain);

		verify(mockFilterChain).doFilter(request, mockResponse);
		if (authenticated) {
			assertNotNull(securityContextHolderStrategy.getContext().getAuthentication());
		} else {
			assertNull(securityContextHolderStrategy.getContext().getAuthentication());
		}
	}

	@Test
	void testDoFilterInternal_noKey() throws IOException, ServletException {
		var request = new MockHttpServletRequest();

		testDoFilterInternal(request, false);
	}

	@Test
	void testDoFilterInternal_invalidKey() throws IOException, ServletException {
		var request = new MockHttpServletRequest();
		request.addHeader(ApiKeyAuthenticationConverter.API_KEY_HEADER_NAME, "INVALID_KEY");

		testDoFilterInternal(request, false);
	}

	@Test
	void testDoFilterInternal_validKey() throws IOException, ServletException {
		var request = new MockHttpServletRequest();
		request.addHeader(ApiKeyAuthenticationConverter.API_KEY_HEADER_NAME, API_KEY);

		testDoFilterInternal(request, true);
	}

}
