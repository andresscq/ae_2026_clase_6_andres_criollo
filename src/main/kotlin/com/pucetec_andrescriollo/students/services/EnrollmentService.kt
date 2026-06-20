package com.pucetec_andrescriollo.students.services

import com.pucetec_andrescriollo.students.dto.EnrollmentRequest
import com.pucetec_andrescriollo.students.dto.EnrollmentResponse
import com.pucetec_andrescriollo.students.dto.EnrollmentUpdateRequest
import com.pucetec_andrescriollo.students.entities.Enrollment
import com.pucetec_andrescriollo.students.entities.Student
import com.pucetec_andrescriollo.students.entities.Subject
import com.pucetec_andrescriollo.students.exceptions.DuplicateEnrollmentException
import com.pucetec_andrescriollo.students.exceptions.EnrollmentNotFoundException
import com.pucetec_andrescriollo.students.exceptions.StudentNotFoundException
import com.pucetec_andrescriollo.students.exceptions.SubjectNotFoundException
import com.pucetec_andrescriollo.students.mappers.toEntity
import com.pucetec_andrescriollo.students.mappers.toResponse
import com.pucetec_andrescriollo.students.repositories.EnrollmentRepository
import com.pucetec_andrescriollo.students.repositories.StudentRepository
import com.pucetec_andrescriollo.students.repositories.SubjectRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class EnrollmentService(
    private val studentRepository: StudentRepository,
    private val subjectRepository: SubjectRepository,
    private val enrollmentRepository: EnrollmentRepository,
) {
    private val logger = LoggerFactory.getLogger(EnrollmentService::class.java)

    fun createEnrollment(request: EnrollmentRequest): EnrollmentResponse {
        logger.info("Creating enrollment student=${request.studentId} subject=${request.subjectId}")

        val student = findStudentOrThrow(request.studentId)
        val subject = findSubjectOrThrow(request.subjectId)

        if (enrollmentRepository.existsByStudent_IdAndSubject_Id(request.studentId, request.subjectId)) {
            logger.warn("Duplicate enrollment student=${request.studentId} subject=${request.subjectId}")
            throw DuplicateEnrollmentException(
                "El estudiante ${request.studentId} ya está matriculado en la materia ${request.subjectId}."
            )
        }

        val saved = enrollmentRepository.save(request.toEntity(student, subject))
        logger.info("Saved enrollment with id ${saved.id}")
        return saved.toResponse()
    }

    fun getAllEnrollments(): List<EnrollmentResponse> {
        logger.info("Get all Enrollments")
        return enrollmentRepository.findAll().map { it.toResponse() }
    }

    fun getEnrollmentById(id: Long): EnrollmentResponse {
        logger.info("Get enrollment id=$id")
        return findEnrollmentOrThrow(id).toResponse()
    }

    fun updateEnrollment(id: Long, request: EnrollmentUpdateRequest): EnrollmentResponse {
        logger.info("Updating enrollment id=$id")
        val enrollment = findEnrollmentOrThrow(id)
        enrollment.status = request.status
        return enrollmentRepository.save(enrollment).toResponse()
    }

    fun deleteEnrollment(id: Long) {
        logger.info("Deleting enrollment id=$id")
        val enrollment = findEnrollmentOrThrow(id)
        enrollmentRepository.delete(enrollment)
    }

    private fun findEnrollmentOrThrow(id: Long): Enrollment =
        enrollmentRepository.findById(id).orElseThrow {
            logger.warn("Enrollment not found id=$id")
            EnrollmentNotFoundException("Matrícula con id $id no encontrada.")
        }

    private fun findStudentOrThrow(id: Long): Student =
        studentRepository.findById(id).orElseThrow {
            logger.warn("Student not found id=$id")
            StudentNotFoundException("Estudiante con id $id no encontrado.")
        }

    private fun findSubjectOrThrow(id: Long): Subject =
        subjectRepository.findById(id).orElseThrow {
            logger.warn("Subject not found id=$id")
            SubjectNotFoundException("Materia con id $id no encontrada.")
        }
}
