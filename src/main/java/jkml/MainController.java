package jkml;

import java.net.URI;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

	@GetMapping(Paths.NOW)
	public Map<String, String> now() {
		return Map.of("now", Instant.now().toString());
	}

	@GetMapping(Paths.REDIRECT)
	public ResponseEntity<Object> redirect() {
		var headers = new HttpHeaders();
		headers.setLocation(URI.create("https://www.google.com/"));
		return new ResponseEntity<>(headers, HttpStatus.FOUND);
	}

	@GetMapping(Paths.ENV)
	public Map<String, String> env() {
		return System.getenv();
	}

	@GetMapping(Paths.PROPERTIES)
	public Map<String, String> properties() {
		var body = new LinkedHashMap<String, String>();
		System.getProperties().forEach((k, v) -> body.put(k.toString(), v.toString()));
		return body;
	}

	@GetMapping(Paths.AUTHENTICATION)
	public Map<String, Object> authentication(Authentication authentication) {
		var body = new LinkedHashMap<String, Object>();
		body.put("name", authentication.getName());
		var authorities = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
		body.put("authorities", authorities);
		return body;
	}

	@GetMapping(Paths.UNSUPPORTED)
	public void unsupported() {
		throw new UnsupportedOperationException("Unsupported operation");
	}

	@GetMapping(Paths.AUTHORITY1)
	public Map<String, String> authority1() {
		return Map.of("Message", "You have reached " + Paths.AUTHORITY1);
	}

}
