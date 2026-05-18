package com.splitmoney.model

import com.splitmoney.common.model.BaseEntity
import jakarta.persistence.*
import java.time.Instant
import java.time.temporal.ChronoUnit

@Entity
@Table(name = "group_invites")
class GroupInviteEntity(

    @Column(name = "token", unique = true, nullable = false)
    var token: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    var group: GroupEntity,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    var createdBy: UserEntity,

    @Column(name = "expires_at", nullable = false)
    var expiresAt: Instant = Instant.now().plus(7, ChronoUnit.DAYS)

) : BaseEntity() {
    constructor() : this("", GroupEntity(), UserEntity())
}
