package com.codecool.zsuzsi.puzzlesbackend.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenServices jwtTokenServices;

    @Value("${allowed.origin}")
    private String allowedOrigin;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200", allowedOrigin));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors().and()
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/registration", "/login").permitAll()
                .antMatchers(HttpMethod.GET, "/puzzles/member/logged-in").authenticated()
                .antMatchers(HttpMethod.GET, "/puzzles/member/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/puzzles/**/admin").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/puzzles/**").authenticated()
                .antMatchers(HttpMethod.POST, "/puzzles/**").authenticated()
                .antMatchers(HttpMethod.PUT, "/puzzles/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/puzzles/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/comments/member/logged-in").authenticated()
                .antMatchers(HttpMethod.GET, "/comments/member/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/comments/**").authenticated()
                .antMatchers(HttpMethod.POST, "/comments/**").authenticated()
                .antMatchers(HttpMethod.PUT, "/comments/**").authenticated()
                .antMatchers(HttpMethod.DELETE, "/comments/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/solutions/member/logged-in").authenticated()
                .antMatchers(HttpMethod.GET, "/solutions/member/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/solutions/**").authenticated()
                .antMatchers(HttpMethod.POST, "/solutions/**").authenticated()
                .antMatchers(HttpMethod.DELETE, "/solutions/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/members/admin").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/members/{member-id}").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/members/**").authenticated()
                .antMatchers(HttpMethod.PUT, "/members/logged-in").authenticated()
                .antMatchers(HttpMethod.PUT, "/members/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/members/**").hasRole("ADMIN")
                .anyRequest().denyAll()
                .and()
                .addFilterBefore(new JwtTokenFilter(jwtTokenServices), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling()
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));

        return http.build();
    }
}
