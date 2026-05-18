package com.splitmoney.repository

import com.splitmoney.model.GroupInviteEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface GroupInviteRepository : JpaRepository<GroupInviteEntity, UUID> {
    fun findByToken(token: String): GroupInviteEntity?
}
