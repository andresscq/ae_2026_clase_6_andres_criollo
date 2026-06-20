package com.pucetec_andrescriollo.students.entities

import jakarta.persistence.CascadeType
import jakarta.persistence.Table
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany


@Entity
@Table(name = "subjects")
class Subject(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    var name: String = "",

    var code: String = "",

    @ManyToOne(fetch = FetchType.LAZY)
    var professor: Professor,

    @OneToMany(mappedBy = "subject", cascade = [CascadeType.ALL], orphanRemoval = true)
    val enrollments: MutableList<Enrollment> = mutableListOf(),
)
