package com.splitmoney.service.auth

import com.splitmoney.model.UserEntity
import com.splitmoney.repository.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder

class AuthService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtTokenProvider: JwtTokenProvider
) {
    fun register(name:String?, email: String?, password: String?):Map<String, Any>{
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
        return mapOf("token" to token, "user" to savedUser)
    }

    fun login(email: String, password: String): Map<String, Any>{
        val user = userRepository.findByEmail(email) ?: throw RuntimeException("Email does not exist")
        if(!passwordEncoder.matches(password, user.password)){
            throw RuntimeException("Invalid email or password")
        }
        val token = jwtTokenProvider.generateToken(user.id.toString(), user.email)
        return mapOf("token" to token, "user" to user)
    }
}