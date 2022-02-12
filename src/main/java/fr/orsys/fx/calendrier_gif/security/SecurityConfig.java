package fr.orsys.fx.calendrier_gif.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    public SecurityConfig(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        
        http.csrf()
        .disable()
        .formLogin()
        .loginPage("/index")
        .loginProcessingUrl("/login")
        .defaultSuccessUrl("/calendrier")
        .failureForwardUrl("/index?notification=Email%20ou%20mot%20de%20passe%20incorrect")
        .permitAll()
        .and()
        .logout()
        .logoutUrl("/deconnexion")
        .logoutSuccessUrl("/index?notification=Au%20revoir")
        .invalidateHttpSession(true)
        .and()
        .authorizeRequests()
        .antMatchers("/calendrier").authenticated()
        .antMatchers("/placerGifDistant").authenticated()
        .antMatchers("/reagir").authenticated()
        .antMatchers("/logout").permitAll()
        .anyRequest().permitAll();
       
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);         
        return authProvider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(userDetailsService);
    }
        
}