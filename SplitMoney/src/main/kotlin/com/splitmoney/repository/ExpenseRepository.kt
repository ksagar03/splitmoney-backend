package com.splitmoney.repository

import com.splitmoney.model.ExpenseEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID


@Repository
interface ExpenseRepository : JpaRepository<ExpenseEntity, UUID> {
    fun findByGroupId(groupId: UUID): List<ExpenseEntity>
}