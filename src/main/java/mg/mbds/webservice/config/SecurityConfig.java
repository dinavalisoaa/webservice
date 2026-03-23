package mg.mbds.webservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;@Configuration
public class SecurityConfig { 
    @Autowired 
    JwtAuthenticationFilter jwtFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean 
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception{ 
        http 
            .csrf(csrf -> csrf.disable()) 
            .sessionManagement( 
                session -> session.sessionCreationPolicy( 
                    SessionCreationPolicy.STATELESS 
                ) 
            ) 
            .authorizeHttpRequests(auth -> auth 
                .requestMatchers("/api/auth/**", "/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll() 
                .requestMatchers( 
                    HttpMethod.GET, "/api/products" 
                ).permitAll() 
                .requestMatchers(HttpMethod.DELETE, "/api/products/**").hasAuthority("ROLE_ADMIN")
                .anyRequest().authenticated() 
            ) 
            .addFilterBefore( 
                jwtFilter, 
                UsernamePasswordAuthenticationFilter.class 
            ); 
        return http.build(); 
    } 
} 
