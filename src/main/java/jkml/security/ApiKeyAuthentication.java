package jkml.security;

import java.util.Collection;
import java.util.Objects;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class ApiKeyAuthentication extends AbstractAuthenticationToken {

	private static final long serialVersionUID = 1L;

	private final String apiKey;

	public ApiKeyAuthentication(String apiKey) {
		super(null);
		this.apiKey = apiKey;
		setAuthenticated(false);
	}

	public ApiKeyAuthentication(String apiKey, Collection<? extends GrantedAuthority> authorities) {
		super(authorities);
		this.apiKey = apiKey;
		setAuthenticated(true);
	}

	@Override
	public Object getCredentials() {
		return null;
	}

	@Override
	public String getPrincipal() {
		return apiKey;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(apiKey);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (!(obj instanceof ApiKeyAuthentication)) {
			return false;
		}
		ApiKeyAuthentication other = (ApiKeyAuthentication) obj;
		return Objects.equals(apiKey, other.apiKey);
	}

}
