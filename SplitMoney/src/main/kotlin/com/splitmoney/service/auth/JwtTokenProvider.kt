package com.splitmoney.service.auth

import io.jsonwebtoken.Jwts
import org.hibernate.validator.internal.util.logging.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import java.security.Key
import java.util.Date

class JwtTokenProvider(
    @Value("\${app.jwt.secret}") private val secret: Key,
    @Value("\${app.jwt.expiration}") private val expiration: Long
) {
    fun generateToken(userId: String, email: String): String{
        val now = Date()
        val expirationDate = Date(now.time + expiration)
        return Jwts.builder()
            .setSubject(userId)
            .claim("email", email)
            .setIssuedAt(now)
            .setExpiration(expirationDate)
            .compact()
    }
    fun getUserIdFromToken(token: String): String {
        return Jwts.parserBuilder()
            .setSigningKey(secret)
            .build()
            .parseClaimsJws(token)
            .body
            .subject
    }
    fun validateToken(token: String): Boolean{
        try{
            Jwts.parserBuilder().setSigningKey(secret).build().parseClaimsJws(token)
            return true
        }catch (ex: Exception){
            return false
        }

    }
}