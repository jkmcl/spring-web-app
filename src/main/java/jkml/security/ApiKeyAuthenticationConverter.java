package jkml.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.authentication.www.BasicAuthenticationConverter;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Based on {@link BasicAuthenticationConverter}.
 */
public class ApiKeyAuthenticationConverter implements AuthenticationConverter {

	public static final String API_KEY_HEADER_NAME = "X-API-Key";

	@Override
	public Authentication convert(HttpServletRequest request) {
		var header = request.getHeader(API_KEY_HEADER_NAME);
		return (header == null) ? null : new ApiKeyAuthentication(header.strip());
	}

}
