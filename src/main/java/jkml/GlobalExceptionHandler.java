package jkml;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(AuthenticationException.class)
	public ResponseEntity<Object> handleAuthenticationException(Exception ex, WebRequest request) {
		logger.error("{}: {}", ex.getClass().getName(), ex.getMessage());
		return createResponse(ex, HttpStatus.UNAUTHORIZED, request);
	}

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<Object> handleAccessDeniedException(Exception ex, WebRequest request) {
		logger.error("{}: {}", ex.getClass().getName(), ex.getMessage());
		return createResponse(ex, HttpStatus.FORBIDDEN, request);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> handleNonMvcException(Exception ex, WebRequest request) {
		logger.error("Exception thrown", ex);
		return createResponse(ex, HttpStatus.INTERNAL_SERVER_ERROR, request);
	}

	private ResponseEntity<Object> createResponse(Exception ex, HttpStatus status, WebRequest request) {
		var body = createProblemDetail(ex, status, ex.getMessage(), null, null, request);
		return handleExceptionInternal(ex, body, new HttpHeaders(), status, request);
	}

}
