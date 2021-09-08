package com.shoppinger.security;

import com.shoppinger.security.oauth.CustomerOAuth2UserService;
import com.shoppinger.security.oauth.OAuth2LoginSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private CustomerOAuth2UserService auth2UserService;
    @Autowired
    private OAuth2LoginSuccessHandler successHandler;
    @Autowired
    private DatabaseLoginSuccessHandler dbLoginSuccessHandler;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/customers").authenticated()
                .anyRequest().permitAll()
                .and()
                .formLogin()
                    .loginPage("/login")
                    .usernameParameter("email")
                    .successHandler(dbLoginSuccessHandler)
                    .permitAll()
                .and()
                .oauth2Login()
                    .loginPage("/login")
                    .userInfoEndpoint()
                    .userService(auth2UserService)
                    .and()
                    .successHandler(successHandler)
                .and()
                .logout().permitAll()
                .and()
                .rememberMe()
                    .key("1234567890_aBcDeFgHiJkLmNoPqRsTuVwXyZ")
                    .tokenValiditySeconds(14 * 24 * 60 * 60)
                    ;

    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/images/**", "/js/**", "/webjars/**");
    }

    @Bean
    public UserDetailsService userDetailsService(){
        return new CustomerUserDetailsService();
    }
    @Bean
    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();

        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());

        return authenticationProvider;
    }
}
