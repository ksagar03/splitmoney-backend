package com.splitmoney.model

import com.splitmoney.common.model.BaseEntity
import jakarta.persistence.CascadeType
import jakarta.persistence.CollectionTable
import jakarta.persistence.Column
import jakarta.persistence.ElementCollection
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import java.util.UUID
import kotlin.collections.mutableListOf


@Entity
@Table(name = "groups")
class GroupEntity : BaseEntity {

    @Column(name = "name", nullable = false)
    var name: String = ""

    @ElementCollection
    @CollectionTable(
        name = "group_members",
        joinColumns = [JoinColumn(name = "group_id")]
    )
    @Column(name = "user_id")
    var memberIds: MutableSet<UUID> = mutableSetOf()

    @OneToMany(mappedBy = "group", cascade = [CascadeType.ALL], orphanRemoval = true)
    var expenses: MutableList<ExpenseEntity> = mutableListOf()

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    var createdBy: UserEntity? = null
    
    constructor()  {
        this.name = name
        this.createdBy = createdBy
    }
}