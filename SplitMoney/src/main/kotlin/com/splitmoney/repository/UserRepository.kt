package com.splitmoney.repository

import com.splitmoney.model.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID


@Repository
interface UserRepository : JpaRepository<UserEntity, UUID>{
    fun findByEmail(username: String): UserEntity?
}
