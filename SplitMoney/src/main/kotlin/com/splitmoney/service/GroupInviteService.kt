package com.splitmoney.service

import com.splitmoney.model.GroupEntity
import com.splitmoney.model.GroupInviteEntity
import com.splitmoney.repository.GroupInviteRepository
import com.splitmoney.repository.GroupRepository
import com.splitmoney.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.util.UUID

@Service
class GroupInviteService(
    private val groupRepository: GroupRepository,
    private val userRepository: UserRepository,
    private val inviteRepository: GroupInviteRepository
) {
    @Transactional
    fun generateInvite(groupId: UUID, userId: UUID): String {
        val group = groupRepository.findById(groupId)
            .orElseThrow { IllegalArgumentException("Group not found.") }
        val user = userRepository.findById(userId)
            .orElseThrow { IllegalArgumentException("User not found.") }
        // Reuse an existing non-expired token for this group if one exists
        val existing = inviteRepository.findAll()
            .firstOrNull { it.group.id == groupId && it.expiresAt.isAfter(Instant.now()) }
        if (existing != null) return existing.token

        val token = UUID.randomUUID().toString().replace("-", "").take(12)
        inviteRepository.save(GroupInviteEntity(token = token, group = group, createdBy = user))
        return token
    }

    @Transactional
    fun joinViaToken(token: String, userId: UUID): GroupEntity {
        val invite = inviteRepository.findByToken(token)
            ?: throw IllegalArgumentException("Invalid invite link.")
        if (invite.expiresAt.isBefore(Instant.now()))
            throw IllegalArgumentException("This invite link has expired.")
        val group = invite.group
        group.memberIds.add(userId)
        return groupRepository.save(group)
    }
}
