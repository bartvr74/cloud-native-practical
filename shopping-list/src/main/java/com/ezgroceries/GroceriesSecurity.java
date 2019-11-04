package com.ezgroceries;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.firewall.StrictHttpFirewall;

@Configuration
@EnableWebSecurity
public class GroceriesSecurity extends WebSecurityConfigurerAdapter {

    private DataSource dataSource;

    @Autowired
    public GroceriesSecurity(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder authnManagerBuilder) throws Exception {
        PasswordEncoder passwordEncoder = passwordEncoder();
        UserBuilder users = User.builder().passwordEncoder((password) -> passwordEncoder.encode(password));
        authnManagerBuilder
                .jdbcAuthentication()
                .dataSource(dataSource);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity web) {
        web.httpFirewall(new StrictHttpFirewall());
        web.ignoring().mvcMatchers("/actuator/**");
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf().disable();
        httpSecurity.headers().cacheControl();
        httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        httpSecurity.servletApi();
        httpSecurity.cors().disable();
        httpSecurity.authorizeRequests().mvcMatchers("/cocktails/**").permitAll();
        httpSecurity.authorizeRequests().mvcMatchers("//shopping-lists/**").permitAll(); // todo change authn
        httpSecurity.authorizeRequests().anyRequest().denyAll();
    }

}
