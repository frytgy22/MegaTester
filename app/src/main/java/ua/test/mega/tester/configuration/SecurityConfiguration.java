package ua.test.mega.tester.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.InMemoryUserDetailsManagerConfigurer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;

import ua.test.mega.tester.core.api.UserAdapter;
import ua.test.mega.tester.core.api.model.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
				.csrf().disable()
				.authorizeRequests().anyRequest().authenticated()
				.and()
				.httpBasic();

	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth, UserAdapter userAdapter) throws Exception {
		InMemoryUserDetailsManagerConfigurer<AuthenticationManagerBuilder> authenticationManagerBuilder =
				auth.inMemoryAuthentication()
						.passwordEncoder(NoOpPasswordEncoder.getInstance());

		for (User user : userAdapter.getAllUsers()) {
			authenticationManagerBuilder = authenticationManagerBuilder
					.withUser(user.getUsername())
					.password(user.getPassword())
					.roles(user.getRoles().toArray(new String[0]))//TODO
                                         //.roles((String[]) user.getRoles().toArray())
					.and();
		}
	}
}
