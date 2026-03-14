package com.splitmoney.repository

import com.splitmoney.model.GroupEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface GroupRepository : JpaRepository<GroupEntity, Long> {
    @Query("SELECT g FROM GroupEntity g WHERE:userId MEMBER of g.memberIds")
    fun findByGroupIdIn(@Param("userId") userid: UUID): List<GroupEntity>
}