package com.quelonios.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

import com.quelonios.exception.AuthException;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	 
    
    @Override
    public void configure(HttpSecurity http) throws Exception {
                http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling().authenticationEntryPoint(new AuthException())
                .and()
                .requestMatchers()
//                .and()
//                .authorizeRequests()
//                .antMatchers("/v2/api-docs/**" ).authenticated()
//                .antMatchers("/consultas/**" ).authenticated()
                .and()
                .csrf()
                .disable(); 
    }    
    
    @Override
    public void configure(WebSecurity web) throws Exception {
      // Allow swagger to be accessed without authentication
      web.ignoring().antMatchers("/v2/api-docs")//
          .antMatchers("/swagger-resources/**")//
          .antMatchers("/swagger-ui.html")//
          .antMatchers("/configuration/**")//
          .antMatchers("/webjars/**")//
          .antMatchers("/public")
          .antMatchers("/employee/admin/**");
    }

}
