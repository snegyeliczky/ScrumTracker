package com.codecool.scrumtracker.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtTokenServices jwtTokenServices;

    public SecurityConfig(JwtTokenServices jwtTokenServices) {
        this.jwtTokenServices = jwtTokenServices;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable()
                .cors().and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/auth/**").permitAll()
                .antMatchers(HttpMethod.GET, "/project/**").authenticated()
                .antMatchers(HttpMethod.PUT, "/project/**").authenticated() // allowed only when signed in
                .antMatchers(HttpMethod.POST, "/project/**").authenticated()
                .antMatchers(HttpMethod.DELETE, "/project/**").authenticated() // allowed only when signed in
                .antMatchers(HttpMethod.DELETE, "/task/**").authenticated() // allowed only when signed in
                .antMatchers(HttpMethod.PUT, "/task/**").authenticated() // allowed only when signed in
                .antMatchers(HttpMethod.POST, "/user/**").authenticated() // allowed only when signed in
                .antMatchers(HttpMethod.GET, "/user/**").authenticated() // allowed only when signed in
                .anyRequest().denyAll()// anything else is denied
                .and()
                .addFilterBefore(new JwtTokenFilter(jwtTokenServices), UsernamePasswordAuthenticationFilter.class);
    }
}
