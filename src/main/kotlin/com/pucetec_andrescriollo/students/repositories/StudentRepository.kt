package com.pucetec_andrescriollo.students.repositories

import com.pucetec_andrescriollo.students.entities.Student
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


@Repository
interface StudentRepository: JpaRepository<Student, Long>{

    fun existsByEmail(email: String): Boolean
}