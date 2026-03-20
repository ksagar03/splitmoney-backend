package com.splitmoney.resolver

import com.splitmoney.model.ExpenseEntity
import com.splitmoney.model.GroupEntity
import com.splitmoney.model.data.CreateExpenseInput
import com.splitmoney.model.data.CreateGroupInput
import com.splitmoney.repository.ExpenseRepository
import com.splitmoney.repository.GroupRepository
import com.splitmoney.repository.UserRepository
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.stereotype.Controller

@Controller
class MutationResolver(
    private val userRepository: UserRepository,
    private val groupRepository: GroupRepository,
    private val expenseRepository: ExpenseRepository
){
    @MutationMapping
    fun createGroup(@Argument input: CreateGroupInput): GroupEntity {
        val creator = userRepository.findById(input.createdBy).orElseThrow { RuntimeException("User not found") }

        val group = GroupEntity(
            name = input.name,
            createdBy = creator,
            memberIds = input.membersId.toMutableSet()
        )
        return groupRepository.save(group)
    }
    @MutationMapping
    fun createExpense(@Argument input: CreateExpenseInput): ExpenseEntity {
        val payer = userRepository.findById(input.payerId).orElseThrow { RuntimeException("Payer not found") }

        val group = groupRepository.findById(input.groupId).orElseThrow { RuntimeException("Group not found") }

        val expense = ExpenseEntity(
            description = input.description,
            amount = input.amount.toBigDecimal(),
            payer = payer,
            group = group
        )

        return expenseRepository.save(expense)
    }
}