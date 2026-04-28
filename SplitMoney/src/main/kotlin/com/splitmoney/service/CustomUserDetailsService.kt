package com.splitmoney.service

import com.splitmoney.repository.UserRepository
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import java.util.UUID

class CustomUserDetailsService(private val userRepository: UserRepository): UserDetailsService {

    override fun loadUserByUsername(userId: String): UserDetails {
        val user = userRepository.findById(UUID.fromString(userId))
            .orElseThrow{ UsernameNotFoundException("User not found with id: $userId") }

        return User.builder().username(user.id.toString()).password(user.password).authorities(emptyList()).build()

    }

}