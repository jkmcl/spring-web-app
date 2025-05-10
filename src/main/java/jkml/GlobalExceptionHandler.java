package jkml;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	private final Logger myLogger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> handleNonMvcException(Exception ex, WebRequest request) {
		myLogger.error("Exception thrown", ex);

		var status = HttpStatus.INTERNAL_SERVER_ERROR;
		var headers = new HttpHeaders();
		var detail = ex.getClass().getName() + ": " + ex.getMessage();
		var body = createProblemDetail(ex, status, detail, null, null, request);
		return handleExceptionInternal(ex, body, headers, status, request);
	}

}
