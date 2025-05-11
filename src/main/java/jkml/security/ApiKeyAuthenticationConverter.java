package jkml.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.authentication.www.BasicAuthenticationConverter;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Based on {@link BasicAuthenticationConverter}.
 */
public class ApiKeyAuthenticationConverter implements AuthenticationConverter {

	private static final String API_KEY_HEADER_NAME = "X-API-Key";

	private final Logger logger = LoggerFactory.getLogger(ApiKeyAuthenticationConverter.class);

	@Override
	public Authentication convert(HttpServletRequest request) {
		var header = request.getHeader(API_KEY_HEADER_NAME);
		if (header == null) {
			logger.info("API key not found in request");
			return null;
		}

		var apiKey = header.strip();
		logger.info("API key found in request: {}", apiKey);
		return new ApiKeyAuthentication(apiKey);
	}

}
