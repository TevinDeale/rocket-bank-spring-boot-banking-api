package com.tevind.rocketbank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class WebSecurity {

    @Autowired
    private Environment environment;


    @Bean
    public SecurityFilterChain apiSecurityChain(HttpSecurity http) throws Exception {
        http.cors(cors -> cors.configurationSource(corsConfigurationSource())).authorizeHttpRequests(requests -> requests.requestMatchers("/api/**", "/hello").authenticated()
                .requestMatchers("/home", "/home/").permitAll()).oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.decoder(jwtDecoder())));
        return http.build();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withJwkSetUri(environment.getProperty("env.jwks.data")).build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        String proxy = environment.getProperty("env.proxy.address");
        String front = environment.getProperty("env.frontend.address");
        String prod_front = environment.getProperty("env.prod.frontend.address");
        CorsConfiguration cors = new CorsConfiguration();
        cors.setAllowedOrigins(Arrays.asList(proxy, front, prod_front));
        cors.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
        cors.setAllowedHeaders(Arrays.asList("Content-Type", "Authorization"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cors);
        return source;
    }

}
