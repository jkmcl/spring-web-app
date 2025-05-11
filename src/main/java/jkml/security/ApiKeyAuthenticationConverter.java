package jkml.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationConverter;

import jakarta.servlet.http.HttpServletRequest;

public class ApiKeyAuthenticationConverter implements AuthenticationConverter {

	private final Logger logger = LoggerFactory.getLogger(ApiKeyAuthenticationConverter.class);

	@Override
	public Authentication convert(HttpServletRequest request) {
		var header = request.getHeader(HttpHeaders.AUTHORIZATION);
		if (header == null) {
			logger.info("API key not found in request");
			return null;
		}
		var apiKey = header.strip();
		logger.info("API key found in request: {}", apiKey);
		return new ApiKeyAuthentication(header.strip());
	}

}
