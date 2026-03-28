package com.splitmoney.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.DefaultSecurityFilterChain

@Configuration
class SecurityConfig {
    @Bean
    fun securityFilterChain(http: HttpSecurity): DefaultSecurityFilterChain? {
        http.authorizeHttpRequests {
            auth ->
            auth.requestMatchers("/graphql", "/graphiql", "/graphql/**").permitAll().anyRequest().authenticated()

        }.csrf { it.disable() }
        return http.build()
    }
}