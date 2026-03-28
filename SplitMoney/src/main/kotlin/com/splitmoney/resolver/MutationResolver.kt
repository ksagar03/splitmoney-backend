package com.splitmoney.resolver

import com.splitmoney.model.ExpenseEntity
import com.splitmoney.model.GroupEntity
import com.splitmoney.model.data.CreateExpenseInput
import com.splitmoney.model.data.CreateGroupInput
import com.splitmoney.service.ExpenseService
import com.splitmoney.service.GroupService
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.stereotype.Controller

@Controller
class MutationResolver(
    private val groupService: GroupService,
    private val expenseService: ExpenseService
){
    @MutationMapping
    fun createGroup(@Argument input: CreateGroupInput): GroupEntity {
        return groupService.createGroup(input)
    }
    @MutationMapping
    fun createExpense(@Argument input: CreateExpenseInput): ExpenseEntity {
        return expenseService.createExpense(input)
    }
}