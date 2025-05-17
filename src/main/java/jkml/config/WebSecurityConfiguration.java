package jkml.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jkml.security.ApiKeyAuthenticationFilter;
import jkml.security.ApiKeyAuthenticationManager;

@Configuration
class WebSecurityConfiguration {

	private final HandlerExceptionResolver resolver;

	WebSecurityConfiguration(@Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
		this.resolver = resolver;
	}

	// This bean also disables the auto-configured UserDetailsService
	@Bean
	AuthenticationManager authenticationManager() {
		return new ApiKeyAuthenticationManager();
	}

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
		http.sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		http.logout(logout -> logout.disable());

		// See https://docs.spring.io/spring-security/reference/features/exploits/csrf.html#csrf-when
		http.csrf(csrf -> csrf.disable());

		// See https://docs.spring.io/spring-security/reference/servlet/architecture.html#_adding_a_custom_filter
		http.addFilterAfter(new ApiKeyAuthenticationFilter(authenticationManager), LogoutFilter.class);

		// See https://www.baeldung.com/spring-security-exceptionhandler
		http.exceptionHandling(eh -> eh
				.authenticationEntryPoint(this::commence)
				.accessDeniedHandler(this::handle));

		http.authorizeHttpRequests(authorize -> authorize
				.requestMatchers("/authentication").authenticated()
				.requestMatchers("/authority1").hasAuthority("authority1")
				.anyRequest().permitAll());

		return http.build();
	}

	private void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) {
		resolver.resolveException(request, response, null, authException);
	}

	private void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) {
		resolver.resolveException(request, response, null, accessDeniedException);
	}

}
