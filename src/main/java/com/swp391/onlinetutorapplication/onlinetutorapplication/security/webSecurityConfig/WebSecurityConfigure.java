package com.swp391.onlinetutorapplication.onlinetutorapplication.security.webSecurityConfig;

import com.swp391.onlinetutorapplication.onlinetutorapplication.payload.response.authResponse.AuthEntryJwtResponse;
import com.swp391.onlinetutorapplication.onlinetutorapplication.filter.tokenFilter.TokenAuthFilter;
import com.swp391.onlinetutorapplication.onlinetutorapplication.security.passwordEncoder.Encoder;
import com.swp391.onlinetutorapplication.onlinetutorapplication.service.userService.userServiceImplement.UserServiceDetailsImplement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        prePostEnabled = true)
public class WebSecurityConfigure extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserServiceDetailsImplement userServiceDetails;

    @Autowired
    private AuthEntryJwtResponse unAuthEntryJwtResponse;

    @Bean
    public TokenAuthFilter tokenAuthFilter() {
        return new TokenAuthFilter();
    }

    @Autowired
    private Encoder encoder;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userServiceDetails).passwordEncoder(encoder.passwordEncoder());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .exceptionHandling().authenticationEntryPoint(unAuthEntryJwtResponse).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests().antMatchers("/api/auth/**").permitAll()
                .antMatchers("/api/test/**").permitAll()
                .anyRequest().authenticated();

        http.addFilterBefore(tokenAuthFilter(), UsernamePasswordAuthenticationFilter.class);
    }


}



