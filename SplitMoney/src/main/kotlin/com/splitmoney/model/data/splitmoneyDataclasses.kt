package com.splitmoney.model.data

import java.util.UUID


data class CreateGroupInput(
    val name: String,
    val membersId: List<UUID>,
    val createdBy: UUID
)

data class CreateExpenseInput(
    val description: String,
    val amount: Double,
    val payerId: UUID,
    val groupId: UUID
)

