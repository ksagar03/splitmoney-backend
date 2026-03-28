package com.splitmoney.model

import com.splitmoney.common.model.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table


@Entity
@Table(name = "users")
class UserEntity(
    @Column(name = "name", nullable = false)
    var name: String = "",

    @Column(name = "email", nullable = false, unique = true)
    var email: String = ""
): BaseEntity(){
     constructor() : this("", "")
}