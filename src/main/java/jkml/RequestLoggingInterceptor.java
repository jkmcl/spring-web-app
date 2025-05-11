package jkml;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;

import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.NoArgGenerator;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class RequestLoggingInterceptor implements HandlerInterceptor {

	private static final String ATTR_NAME = RequestLoggingInterceptor.class.getSimpleName() + ".REQ_ID";

	private final Logger logger = LoggerFactory.getLogger(RequestLoggingInterceptor.class);

	private final NoArgGenerator idGenerator = Generators.defaultTimeBasedGenerator();

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		var reqId = idGenerator.generate();
		request.setAttribute(ATTR_NAME, reqId);
		logger.info("Received request ({}). Path: {}", reqId, request.getRequestURI());
		return true;
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
		var reqId = request.getAttribute(ATTR_NAME);
		logger.info("Sent response ({}). Status: {}", reqId, response.getStatus());
	}

}
