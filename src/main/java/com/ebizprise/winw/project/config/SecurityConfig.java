package com.ebizprise.winw.project.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.session.HttpSessionEventPublisher;

import com.ebizprise.winw.project.security.service.CustomAuthenticationProvider;
import com.ebizprise.winw.project.security.service.LdapUserDetailsService;
import com.ebizprise.winw.project.security.service.LoginFailureHandler;
import com.ebizprise.winw.project.security.service.LoginSuccessHandler;
import com.ebizprise.winw.project.security.service.LogoutSuccessHandler;
import com.ebizprise.winw.project.security.service.SessionListener;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private Environment env;

	@Autowired
	private CustomAuthenticationProvider customAuthenticationProvider;

	@Autowired
	private LdapUserDetailsService ldapUserDetailsService;

	@Autowired
	private LoginSuccessHandler loginSuccessHandler;

    @Autowired
    private LoginFailureHandler loginFailureHandler;

	@Autowired
	private LogoutSuccessHandler logoutSuccessHandler;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.authorizeRequests()
	        .antMatchers("/resources/**", "/static/**", "/ldap/**", "/ws/**").permitAll()
			.anyRequest().authenticated()
			.and()
			.formLogin()
			.loginPage("/login")
			.usernameParameter("username")
			.passwordParameter("password")
			.permitAll()
			.successHandler(loginSuccessHandler)
			.failureHandler(loginFailureHandler)
			.and()
			.logout()
			.deleteCookies("JSESSIONID")
            .clearAuthentication(true)
            .invalidateHttpSession(true)
            .permitAll()
			.logoutSuccessHandler(logoutSuccessHandler)
			.and()
			.sessionManagement().invalidSessionUrl("/login")
			.maximumSessions(2).expiredUrl("/login").sessionRegistry(sessionRegistry());
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) {
		if (env.getProperty("ldap.enabled").equals("true")) {
			auth.authenticationProvider(ldapUserDetailsService);
		} else {
			auth.authenticationProvider(customAuthenticationProvider);
		}
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationTrustResolver getAuthenticationTrustResolver() {
		return new AuthenticationTrustResolverImpl();
	}

	@Bean
	public SessionRegistry sessionRegistry() {
		return new SessionRegistryImpl();
	}

	@Bean
	public HttpSessionEventPublisher httpSessionEventPublisher() {
		return new SessionListener();
	}

}