package com.splitmoney.common.model

import jakarta.persistence.Column
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.MappedSuperclass
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime
import java.util.UUID


@MappedSuperclass
abstract class  BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    var id: UUID? = null

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    var createdAtDate: LocalDateTime? = null

    @UpdateTimestamp
    @Column(name = "updated_at")
    var updatedAtDate: LocalDateTime? = null

    // GraphQL schema declares createdAt/updatedAt as String! —
    // Spring GraphQL cannot coerce LocalDateTime to String automatically,
    // so we expose computed String properties for the resolver to use.
    val createdAt: String get() = createdAtDate?.toString() ?: ""
    val updatedAt: String get() = updatedAtDate?.toString() ?: ""

}