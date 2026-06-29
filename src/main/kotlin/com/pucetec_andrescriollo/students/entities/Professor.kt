package com.pucetec_andrescriollo.students.entities

import jakarta.persistence.CascadeType
import jakarta.persistence.Table
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany

@Entity
@Table(name = "professors")
class Professor(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    var name: String = "",

    var email: String? = null,

    @OneToMany(mappedBy = "professor", cascade = [CascadeType.ALL], orphanRemoval = true)
    val subjects: MutableList<Subject> = mutableListOf(),
)
