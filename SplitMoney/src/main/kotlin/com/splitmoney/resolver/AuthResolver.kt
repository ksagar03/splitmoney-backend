package com.splitmoney.resolver

import com.splitmoney.model.data.AuthPayload
import com.splitmoney.service.auth.AuthService
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.stereotype.Controller

@Controller
class AuthResolver(private val authService: AuthService) {
    @MutationMapping
    fun login(@Argument email: String, @Argument password: String): AuthPayload{
       return authService.login(email, password)
    }
    @MutationMapping
    fun register(@Argument name: String, @Argument email: String, @Argument password: String): AuthPayload{
         return authService.register(name, email, password)
    }
}