package br.com.prova.security.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import br.com.prova.service.JwtService;
import br.com.prova.utils.Path;
import br.com.prova.utils.Role;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	

	@Autowired
	private UserDetailsService service;
	
	@Autowired
	private JwtService jwtService;
	
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(service).passwordEncoder(passwordEncoder());
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public JwtTokenFilter jwtTokenFilter() {
		return new JwtTokenFilter(jwtService, service);
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
		.csrf().disable()
		.authorizeRequests()
			.requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
					.antMatchers(HttpMethod.GET, "/**").permitAll()
					.antMatchers(HttpMethod.POST, Path.PATH_AUTENTICAR).permitAll()
			        .antMatchers(HttpMethod.POST, Path.PATH_CADASTRO).hasAuthority(Role.ROLE_ADMIN)
			        .antMatchers(HttpMethod.PUT, Path.PATH_CADASTRO).hasAuthority(Role.ROLE_ADMIN)
			        .antMatchers(HttpMethod.PATCH, Path.PATH_CADASTRO).hasAuthority(Role.ROLE_ADMIN)
			        .antMatchers(HttpMethod.DELETE, Path.PATH_CADASTRO).hasAuthority(Role.ROLE_ADMIN)
					.anyRequest().authenticated()	
		.and()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		.and()
			.addFilterBefore(jwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
	}
	
	@Bean
	public FilterRegistrationBean<CorsFilter> corsFilter() {

		List<String> all = Arrays.asList("*");

		var config = new CorsConfiguration();
		config.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
		config.setAllowedMethods(all);
		config.setAllowedHeaders(all);
		config.setAllowCredentials(true);

		var source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);

		var corFilter = new CorsFilter(source);

		FilterRegistrationBean<CorsFilter> filter = new FilterRegistrationBean<>(corFilter);
		filter.setOrder(Ordered.HIGHEST_PRECEDENCE);

		return filter;
	}
	
}
