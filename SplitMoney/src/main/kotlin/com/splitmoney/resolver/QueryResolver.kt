package com.splitmoney.resolver

import com.splitmoney.model.ExpenseEntity
import com.splitmoney.model.GroupEntity
import com.splitmoney.model.UserEntity
import com.splitmoney.repository.ExpenseRepository
import com.splitmoney.repository.GroupRepository
import com.splitmoney.repository.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller
import java.util.UUID

@Controller
class QueryResolver(
    private val userRepository: UserRepository,
    private val groupRepository: GroupRepository,
    private val expenseRepository: ExpenseRepository
) {

    @QueryMapping
    fun hello(): String = "Hello SplitMoney!"

    @QueryMapping
    fun users(): List<UserEntity> = userRepository.findAll()

    @QueryMapping
    fun user(@Argument id: UUID): UserEntity? = userRepository.findByIdOrNull(id)

    @QueryMapping
    fun groups(): List<GroupEntity> = groupRepository.findAll()

    @QueryMapping
    fun group(@Argument id: UUID): GroupEntity? = groupRepository.findByIdOrNull(id)

    @QueryMapping
    fun expenses(@Argument groupId: UUID): List<ExpenseEntity> = expenseRepository.findByGroupId(groupId)
}
