package fr.eni.tp.filmotheque.configuration;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.authorizeHttpRequests((authorize) -> authorize
				.requestMatchers("/").permitAll()
				.requestMatchers("/css/*").permitAll()
				.requestMatchers("/images/*").permitAll()
				.requestMatchers("/login").permitAll()
				.requestMatchers("/films").permitAll()
				.requestMatchers("/films/detail").permitAll()
				.requestMatchers("/contexte").authenticated()
				.requestMatchers("/session").authenticated()
				.requestMatchers("/avis").hasRole("MEMBRE")
				.requestMatchers(HttpMethod.GET,"/avis/creer").hasRole("MEMBRE")
				.requestMatchers(HttpMethod.POST,"/avis/creer").hasRole("MEMBRE")
				.requestMatchers(HttpMethod.GET,"/films/creer").hasRole("ADMIN")
				.requestMatchers(HttpMethod.POST,"/films/creer").hasRole("ADMIN")
				
			)
			.httpBasic(Customizer.withDefaults())
			.formLogin((formLogin) ->
				formLogin
					.loginPage("/login")
					.defaultSuccessUrl("/session")
			)
			.logout((logout) ->
				logout
					.invalidateHttpSession(true)
					.logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))
					.logoutSuccessUrl("/")
			);	
			

		return http.build();
	}
	

	@Bean
	//Permet de chercher les utilisateurs en base de donnée
	UserDetailsManager users(DataSource dataSource) {
		
		JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);
		jdbcUserDetailsManager.setUsersByUsernameQuery("select email, password,'true' as enabled from MEMBRE where email = ?");
		jdbcUserDetailsManager.setAuthoritiesByUsernameQuery("select email, ROLE from ROLES r inner join MEMBRE m on m.admin = r.IS_ADMIN where email = ?");
		
		return jdbcUserDetailsManager;
	}	
	
}
