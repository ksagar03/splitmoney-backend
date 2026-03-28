package com.splitmoney.service

import com.splitmoney.model.ExpenseEntity
import com.splitmoney.model.data.CreateExpenseInput
import com.splitmoney.repository.ExpenseRepository
import com.splitmoney.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ExpenseService(
    private val expenseRepository: ExpenseRepository,
    private val groupService: GroupService,
    private val userRepository: UserRepository
) {

    @Transactional
    fun createExpense(input: CreateExpenseInput): ExpenseEntity {
        val group = groupService.getGroupById(input.groupId)
        val payer = userRepository.findById(input.payerId)
            .orElseThrow {  IllegalArgumentException("Payer not found with id ${input.payerId}") }
        val expense = ExpenseEntity(
            description = input.description,
            amount = input.amount.toBigDecimal(),
            payer = payer ,
            group = group
        )
        return expenseRepository.save(expense)
    }
}