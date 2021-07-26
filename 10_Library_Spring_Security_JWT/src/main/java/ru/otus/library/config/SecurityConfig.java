package ru.otus.library.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;
import ru.otus.library.service.UserDetailsServiceImpl;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserDetailsServiceImpl userDetailsService;
    private final JwtConfiguration configuration;

    @Bean
    JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withPublicKey(configuration.getPublicKey()).build();
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/token").permitAll()
                .antMatchers(HttpMethod.POST, "/token").permitAll()
                .antMatchers(HttpMethod.GET, "/books").hasAnyAuthority("SCOPE_ROLE_user", "SCOPE_ROLE_admin")
                .antMatchers(HttpMethod.GET, "/books/**").hasAnyAuthority("SCOPE_ROLE_user", "SCOPE_ROLE_admin")
                .antMatchers(HttpMethod.POST, "/books/**").hasAuthority("SCOPE_ROLE_admin")
                .antMatchers(HttpMethod.PUT, "/books/**").hasAuthority("SCOPE_ROLE_admin")
                .antMatchers(HttpMethod.DELETE, "/books/**").hasAuthority("SCOPE_ROLE_admin")
                .antMatchers(HttpMethod.GET, "/authors/**").hasAnyAuthority("SCOPE_ROLE_user", "SCOPE_ROLE_admin")
                .antMatchers(HttpMethod.POST, "/authors/**").hasAuthority("SCOPE_ROLE_admin")
                .antMatchers(HttpMethod.DELETE, "/authors/**").hasAuthority("SCOPE_ROLE_admin")
                .antMatchers(HttpMethod.GET, "/genres/**").hasAnyAuthority("SCOPE_ROLE_user", "SCOPE_ROLE_admin")
                .antMatchers(HttpMethod.POST, "/genres/**").hasAuthority("SCOPE_ROLE_admin")
                .antMatchers(HttpMethod.DELETE, "/genres/**").hasAuthority("SCOPE_ROLE_admin")
                .antMatchers("/**").denyAll()
                .and()
                .httpBasic(Customizer.withDefaults())
                .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        ;
    }

    @SuppressWarnings("deprecation")
    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }


    @Autowired
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

}
