package com.splitmoney.service.auth

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
    fun register(name:String?, email: String?, password: String?): AuthPayload{
        if(name == null || email == null || password == null){
            throw RuntimeException("Field is missing")
        }
        if(userRepository.findByEmail(email) != null){
            throw RuntimeException("Email already registered")
        }
        val user = UserEntity(
            name = name,
            email =email,
            password = passwordEncoder.encode(password)
        )
        val savedUser = userRepository.save(user)
        val token = jwtTokenProvider.generateToken(savedUser.id.toString(), savedUser.email)
        return AuthPayload(token, savedUser)
    }

    fun login(email: String, password: String): AuthPayload{
        val user = userRepository.findByEmail(email) ?: throw RuntimeException("Email does not exist")
        if(user.password == null){
            throw RuntimeException("This account uses ${user.provider} for login. Please use social login.")
        }
        if(!passwordEncoder.matches(password, user.password)){
            throw RuntimeException("Invalid email or password")
        }
        val token = jwtTokenProvider.generateToken(user.id.toString(), user.email)
        return AuthPayload(token, user)
    }
}