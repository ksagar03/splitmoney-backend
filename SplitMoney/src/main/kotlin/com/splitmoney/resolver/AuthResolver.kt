package com.splitmoney.resolver

import com.splitmoney.model.UserEntity
import com.splitmoney.model.data.AuthPayload
import com.splitmoney.service.auth.AuthService
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping

class AuthResolver(private val authService: AuthService) {
    @MutationMapping
    fun login(@Argument email: String, @Argument password: String): AuthPayload{
        val result = authService.login(email, password)
        return AuthPayload(result["token"] as String, result["user"] as UserEntity)
    }
    @MutationMapping
    fun register(@Argument name: String, @Argument email: String, @Argument password: String): AuthPayload{
        val result= authService.register(name, email, password)
        return AuthPayload(result["token"] as String, result["user"] as UserEntity)
    }
}