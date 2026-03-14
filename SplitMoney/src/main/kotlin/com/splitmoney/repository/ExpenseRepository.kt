package com.splitmoney.repository

import com.splitmoney.model.GroupEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


@Repository
interface ExpenseRepository : JpaRepository<GroupEntity, Long> {
    fun findByGroupId(groupId: String): List<GroupEntity>

}