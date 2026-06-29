package com.pucetec_andrescriollo.students.repositories

import com.pucetec_andrescriollo.students.entities.Professor
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProfessorRepository : JpaRepository<Professor, Long> {

    fun existsByEmail(email: String): Boolean
}
