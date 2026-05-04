package com.splitmoney.service.auth

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.Date
import javax.crypto.SecretKey

@Component
class JwtTokenProvider(
    @Value("\${app.jwt.secret}") private val secret: String,
    @Value("\${app.jwt.expiration}") private val expiration: Long
) {
    private val secretKey: SecretKey by lazy { Keys.hmacShaKeyFor(secret.toByteArray()) }
    fun generateToken(userId: String, email: String): String{
        val now = Date()
        val expirationDate = Date(now.time + expiration)
        return Jwts.builder()
            .setSubject(userId)
            .claim("email", email)
            .setIssuedAt(now)
            .setExpiration(expirationDate)
            .signWith(secretKey)
            .compact()
    }
    fun getUserIdFromToken(token: String): String {
        return Jwts.parserBuilder()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(token)
            .body
            .subject
    }
    fun validateToken(token: String): Boolean{
        return try{
            Jwts.parserBuilder().setSigningKey(secret).build().parseClaimsJws(token)
             true
        }catch (ex: Exception){
             false
        }

    }
}