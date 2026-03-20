package com.splitmoney.resolver

import com.splitmoney.model.GroupEntity
import com.splitmoney.model.UserEntity
import com.splitmoney.repository.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.stereotype.Controller

@Controller
class GroupFieldResolver(
    private val userRepository: UserRepository
){
    @SchemaMapping(typeName = "Group", field = "members")
    fun members(group: GroupEntity): List<UserEntity> {
        return group.memberIds.mapNotNull { userId -> userRepository.findByIdOrNull(userId) }
    }




}