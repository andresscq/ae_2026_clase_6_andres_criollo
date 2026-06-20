package com.pucetec_andrescriollo.students.repositories

import com.pucetec_andrescriollo.students.entities.Enrollment
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface EnrollmentRepository : JpaRepository<Enrollment, Long> {

    fun existsByStudent_IdAndSubject_Id(studentId: Long, subjectId: Long): Boolean
}
