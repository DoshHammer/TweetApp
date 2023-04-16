package com.tweet.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@ComponentScan(basePackages = { "com.tweet" })
@EnableWebSecurity
public class securityConfig extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(HttpSecurity http) throws Exception {

        http.csrf().disable()
        .authorizeRequests()
        .antMatchers("/register","/login").permitAll() 
        .antMatchers("/**")
        .permitAll().anyRequest().authenticated().and().
          exceptionHandling().and().sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }
    
    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }
    
}
