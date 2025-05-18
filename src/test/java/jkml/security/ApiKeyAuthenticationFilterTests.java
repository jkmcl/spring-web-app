package jkml.security;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;

class ApiKeyAuthenticationFilterTests {

	private static final String API_KEY = UUID.randomUUID().toString();

	private final SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder
			.getContextHolderStrategy();

	private ApiKeyAuthenticationFilter filter;

	@BeforeEach
	void beforeEach() {
		securityContextHolderStrategy.clearContext();

		Map<String, Set<GrantedAuthority>> authorities = Map.of(API_KEY,
				Set.of(new SimpleGrantedAuthority("authority")));
		filter = new ApiKeyAuthenticationFilter(new ApiKeyAuthenticationManager(authorities));
	}

	@Test
	void testDoFilterInternal_noKey() throws IOException, ServletException {
		var request = new MockHttpServletRequest();

		filter.doFilterInternal(request, mock(HttpServletResponse.class), mock(FilterChain.class));

		assertNull(securityContextHolderStrategy.getContext().getAuthentication());
	}

	@Test
	void testDoFilterInternal_invalidKey() throws IOException, ServletException {
		var request = new MockHttpServletRequest();
		request.addHeader(ApiKeyAuthenticationConverter.API_KEY_HEADER_NAME, "INVALID_KEY");

		filter.doFilterInternal(request, mock(HttpServletResponse.class), mock(FilterChain.class));

		assertNull(securityContextHolderStrategy.getContext().getAuthentication());
	}

	@Test
	void testDoFilterInternal_validKey() throws IOException, ServletException {
		var request = new MockHttpServletRequest();
		request.addHeader(ApiKeyAuthenticationConverter.API_KEY_HEADER_NAME, API_KEY);

		filter.doFilterInternal(request, mock(HttpServletResponse.class), mock(FilterChain.class));

		assertNotNull(securityContextHolderStrategy.getContext().getAuthentication());
	}

}
