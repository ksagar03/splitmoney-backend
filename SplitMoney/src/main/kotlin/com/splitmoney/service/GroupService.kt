package com.splitmoney.service

import com.splitmoney.model.GroupEntity
import com.splitmoney.model.data.CreateGroupInput
import com.splitmoney.repository.GroupRepository
import com.splitmoney.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID


@Service
class GroupService(
    private val groupRepository: GroupRepository,
    private val userRepository: UserRepository
) {
    @Transactional
    fun createGroup(input: CreateGroupInput): GroupEntity {
        val members = userRepository.findAllById(input.membersId)
        if(members.size != input.membersId.size){
            throw IllegalArgumentException("One or more members do not match")
        }
        val adminUser = userRepository.findById(input.createdBy)
            .orElseThrow { IllegalArgumentException("User not found") }
        val newGroup = GroupEntity(
            name = input.name,
            memberIds = input.membersId.toHashSet(),
            createdBy = adminUser
        )

        return groupRepository.save(newGroup)

    }
    @Transactional(readOnly = true)
    fun getGroupById(groupId: UUID): GroupEntity {
        return groupRepository.findById(groupId)
            .orElseThrow{ NoSuchElementException("Group not found with id $groupId") }
    }

}