package com.splitmoney.model

import com.splitmoney.common.model.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.math.BigDecimal

@Entity
@Table(name="expenses")
open class ExpenseEntity(

    @Column(name = "description", nullable = false)
    var description: String,

    @Column(name = "amount", nullable = false, precision = 19, scale = 2)
    var amount: BigDecimal,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payer_id", nullable = false)
    var payer: UserEntity,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    var group: GroupEntity

    ): BaseEntity(){

    protected constructor() : this("", BigDecimal.ZERO, UserEntity(), GroupEntity("", UserEntity()))

}
