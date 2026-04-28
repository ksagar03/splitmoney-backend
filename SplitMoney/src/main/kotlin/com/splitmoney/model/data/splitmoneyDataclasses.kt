package com.splitmoney.model.data

import com.splitmoney.model.UserEntity
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

data class Balance(
    val user: UserEntity,
    val amount: Double,
    val settlements: List<Settlement>
)

data class Settlement(
    val from: UserEntity,
    val to: UserEntity,
    val amount: Double
)

data class AuthPayload(val token: String, val user: UserEntity)
