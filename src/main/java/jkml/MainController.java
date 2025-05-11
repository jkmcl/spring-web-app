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

	@GetMapping("/now")
	public Map<String, String> now() {
		return Map.of("now", Instant.now().toString());
	}

	@GetMapping("/redirect")
	public ResponseEntity<Object> redirect() {
		var headers = new HttpHeaders();
		headers.setLocation(URI.create("https://www.google.com/"));
		return new ResponseEntity<>(headers, HttpStatus.FOUND);
	}

	@GetMapping("/env")
	public Map<String, String> env() {
		return System.getenv();
	}

	@GetMapping("/properties")
	public Map<String, String> properties() {
		var body = new LinkedHashMap<String, String>();
		System.getProperties().forEach((k, v) -> body.put(k.toString(), v.toString()));
		return body;
	}

	@GetMapping("/authentication")
	public Map<String, Object> authentication(Authentication authentication) {
		var body = new LinkedHashMap<String, Object>();
		body.put("name", authentication.getName());
		var authorities = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
		body.put("authorities", authorities);
		return body;
	}

	@GetMapping("/unsupported")
	public void unsupported() {
		throw new UnsupportedOperationException("Unsupported operation");
	}

}
