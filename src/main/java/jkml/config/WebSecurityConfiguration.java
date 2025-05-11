package jkml.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;

import jkml.security.ApiKeyAuthenticationFilter;
import jkml.security.ApiKeyAuthenticationManager;
import jkml.security.SecurityExceptionHandler;

@Configuration
class WebSecurityConfiguration {

	private final SecurityExceptionHandler securityExceptionHandler;

	WebSecurityConfiguration(SecurityExceptionHandler entryPoint) {
		this.securityExceptionHandler = entryPoint;
	}

	// This bean also disables the auto-configured UserDetailsService
	@Bean
	ApiKeyAuthenticationManager apiKeyAuthenticationManager() {
		return new ApiKeyAuthenticationManager();
	}

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http, ApiKeyAuthenticationManager apiKeyAuthenticationManager) throws Exception {
		http.addFilterAfter(new ApiKeyAuthenticationFilter(apiKeyAuthenticationManager), LogoutFilter.class);
		http.httpBasic(basic -> basic.disable());
		http.csrf(csrf -> csrf.disable());
		http.sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		http.authorizeHttpRequests(authorize -> authorize
				.requestMatchers("/authentication").authenticated()
				.requestMatchers("/authority1").hasAuthority("authority1")
				.anyRequest().permitAll()
		);
		// https://www.baeldung.com/spring-security-exceptionhandler
		http.exceptionHandling(eh -> eh.authenticationEntryPoint(securityExceptionHandler).accessDeniedHandler(securityExceptionHandler));
		return http.build();
	}

}
