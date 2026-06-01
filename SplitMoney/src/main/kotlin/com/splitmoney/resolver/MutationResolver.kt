package com.splitmoney.resolver

import com.splitmoney.model.ExpenseEntity
import com.splitmoney.model.GroupEntity
import com.splitmoney.model.data.CreateExpenseInput
import com.splitmoney.model.data.CreateGroupInput
import com.splitmoney.model.data.UpdateExpenseInput
import com.splitmoney.model.data.UpdateGroupInput
import com.splitmoney.service.ExpenseService
import com.splitmoney.service.GroupService
import com.splitmoney.service.GroupInviteService
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Controller
import java.util.UUID

@Controller
class MutationResolver(
    private val groupService: GroupService,
    private val expenseService: ExpenseService,
    private val groupInviteService: GroupInviteService
) {
    @MutationMapping
    fun createGroup(@Argument input: CreateGroupInput): GroupEntity {
        return groupService.createGroup(input)
    }

    @MutationMapping
    fun createExpense(@Argument input: CreateExpenseInput): ExpenseEntity {
        return expenseService.createExpense(input)
    }

    @MutationMapping
    fun generateGroupInvite(@Argument groupId: String): String {
        return groupInviteService.generateInvite(UUID.fromString(groupId), currentUserId())
    }

    @MutationMapping
    fun joinGroup(@Argument token: String): GroupEntity {
        return groupInviteService.joinViaToken(token, currentUserId())
    }

    private fun currentUserId(): UUID =
        UUID.fromString(SecurityContextHolder.getContext().authentication?.name)

    @MutationMapping
    fun updateExpense(@Argument id: String, @Argument input: UpdateExpenseInput): ExpenseEntity {
        return expenseService.updateExpense(UUID.fromString(id), input)
    }
    @MutationMapping
    fun deleteExpense(@Argument id:String): Boolean{
        return expenseService.deleteExpense(UUID.fromString(id))
    }
    @MutationMapping
    fun updateGroup(@Argument groupId: UUID, @Argument input: UpdateGroupInput): GroupEntity {
        return groupService.updateGroup(groupId, input)
    }
    @MutationMapping
    fun deleteGroup(@Argument groupId: UUID): Boolean {
        return groupService.deleteGroup(groupId)
    }
}