package com.splitmoney.repository

import com.splitmoney.model.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


@Repository
interface UserRepository : JpaRepository<UserEntity, Long>{
    fun findByEmail(username: String): UserEntity?
}
