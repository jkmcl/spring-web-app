package jkml.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;

import jkml.security.ApiKeyAuthenticationFilter;
import jkml.security.ApiKeyAuthenticationManager;
import jkml.security.SecurityExceptionResolver;

@Configuration
class WebSecurityConfiguration {

	private final SecurityExceptionResolver exceptionResolver;

	WebSecurityConfiguration(SecurityExceptionResolver exceptionResolver) {
		this.exceptionResolver = exceptionResolver;
	}

	// This bean also disables the auto-configured UserDetailsService
	@Bean
	AuthenticationManager authenticationManager() {
		return new ApiKeyAuthenticationManager();
	}

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
		// See https://docs.spring.io/spring-security/reference/servlet/architecture.html#adding-custom-filter
		http.addFilterAfter(new ApiKeyAuthenticationFilter(authenticationManager), LogoutFilter.class);
		// See https://docs.spring.io/spring-security/reference/features/exploits/csrf.html#csrf-when
		http.csrf(csrf -> csrf.disable());
		http.sessionManagement(configure -> configure
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		http.authorizeHttpRequests(authorize -> authorize
				.requestMatchers("/authentication").authenticated()
				.requestMatchers("/authority1").hasAuthority("authority1")
				.anyRequest().permitAll());
		// See https://www.baeldung.com/spring-security-exceptionhandler
		http.exceptionHandling(configure -> configure
				.authenticationEntryPoint(exceptionResolver)
				.accessDeniedHandler(exceptionResolver));
		return http.build();
	}

}
