/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.pyka.lavage.config;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 *
 * @author Publicab
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
    
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private DataSource dataSource;
    
    @Value("${spring.queries.login-query}")
    private String loginQuery;
    @Value("${spring.queries.roles-query}")
    private String rolesQuery;
    
    @Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.jdbcAuthentication().dataSource(dataSource).usersByUsernameQuery(loginQuery)
                        .authoritiesByUsernameQuery(rolesQuery)
                        .passwordEncoder(bCryptPasswordEncoder);
	}
        
        @Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http.
			authorizeRequests()
				.antMatchers("/").permitAll()
				.antMatchers("/login").permitAll()
                                .antMatchers("/inscription").permitAll()
                                .antMatchers("/inscriptionEntreprise").permitAll()
                                .antMatchers("/inscriptionLaveur").permitAll()
                                .antMatchers("/ConfirmerInscription*").permitAll()
                                .antMatchers("/NousContacter*").permitAll()
                                .antMatchers("/InscriptionSuccess*").permitAll()
                                .antMatchers("/ValideCode*").permitAll()
                                .antMatchers("/SirenExistante*").permitAll()
                                .antMatchers("/TelephoneExistant*").permitAll()
                                .antMatchers("/EmailExistant*").permitAll()
                                .antMatchers("/InvalideCode*").permitAll()
                                .antMatchers("/RenvoyerCode").permitAll()
                                .antMatchers("/Reinitialiser").permitAll()
                                .antMatchers("/Admin/**").access("hasAuthority('SUPER_ADMIN') or hasAuthority('ADMIN')")
                                .antMatchers("/Laveur/**").hasAuthority("LAVEUR")
                                .antMatchers("/Client/**").hasAuthority("CLIENT")
                                .anyRequest().authenticated()
                                .and()
                        .csrf().disable().formLogin()
				.loginPage("/login").failureUrl("/login?error=true")
				.defaultSuccessUrl("/home")
				.usernameParameter("email")
				.passwordParameter("password")
				.and()
                        .logout()
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
				.logoutSuccessUrl("/").and().exceptionHandling()
				.accessDeniedPage("/access-denied");
	}
	
    @Override
	public void configure(WebSecurity web) throws Exception {
	    web.ignoring().antMatchers("/resources/**", "/static/**", "/static/assets/**", "/css/**", "/js/**", "/fonts/**", "/css2/**", "/js2/**", "/images/**","/fonts/**","/font-awesome/**","/img/**","/assets/**");
	}
        
        
}
