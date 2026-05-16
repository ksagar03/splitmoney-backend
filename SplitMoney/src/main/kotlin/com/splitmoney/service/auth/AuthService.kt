package com.splitmoney.service.auth

import com.splitmoney.common.exception.AuthException
import com.splitmoney.model.UserEntity
import com.splitmoney.model.data.AuthPayload
import com.splitmoney.repository.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtTokenProvider: JwtTokenProvider
) {
    fun register(name: String?, email: String?, password: String?): AuthPayload {
        if (name == null || email == null || password == null) {
            throw AuthException("Please fill in all fields.", "MISSING_FIELDS")
        }
        if (userRepository.findByEmail(email) != null) {
            throw AuthException("An account with this email already exists.", "EMAIL_ALREADY_REGISTERED")
        }
        val user = UserEntity(
            name = name,
            email = email,
            password = passwordEncoder.encode(password)
        )
        val savedUser = userRepository.save(user)
        val token = jwtTokenProvider.generateToken(savedUser.id.toString(), savedUser.email)
        return AuthPayload(token, savedUser)
    }

    fun login(email: String, password: String): AuthPayload {
        val user = userRepository.findByEmail(email)
            ?: throw AuthException("No account found with that email.", "USER_NOT_FOUND")
        if (user.password == null) {
            throw AuthException(
                "This account was created with ${user.provider}. Please use social login.",
                "SOCIAL_LOGIN_REQUIRED"
            )
        }
        if (!passwordEncoder.matches(password, user.password)) {
            throw AuthException("Incorrect email or password.", "INVALID_CREDENTIALS")
        }
        val token = jwtTokenProvider.generateToken(user.id.toString(), user.email)
        return AuthPayload(token, user)
    }
}