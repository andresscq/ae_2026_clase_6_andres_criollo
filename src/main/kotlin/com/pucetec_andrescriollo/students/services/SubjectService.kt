package com.pucetec_andrescriollo.students.services

import com.pucetec_andrescriollo.students.dto.SubjectRequest
import com.pucetec_andrescriollo.students.dto.SubjectResponse
import com.pucetec_andrescriollo.students.entities.Professor
import com.pucetec_andrescriollo.students.entities.Subject
import com.pucetec_andrescriollo.students.exceptions.ProfessorNotFound
import com.pucetec_andrescriollo.students.exceptions.SubjectNotFoundException
import com.pucetec_andrescriollo.students.mappers.toEntity
import com.pucetec_andrescriollo.students.mappers.toResponse
import com.pucetec_andrescriollo.students.repositories.ProfessorRepository
import com.pucetec_andrescriollo.students.repositories.SubjectRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class SubjectService(
    private val subjectRepository: SubjectRepository,
    private val professorRepository: ProfessorRepository,
) {
    private val logger = LoggerFactory.getLogger(SubjectService::class.java)

    fun createSubject(request: SubjectRequest): SubjectResponse {
        logger.info("Creating Subject ${request.name}")
        val professor = findProfessorOrThrow(request.professorId)
        val saved = subjectRepository.save(request.toEntity(professor))
        logger.info("Saved subject with id ${saved.id}")
        return saved.toResponse()
    }

    fun getAllSubjects(): List<SubjectResponse> {
        logger.info("Get all Subjects")
        return subjectRepository.findAll().map { it.toResponse() }
    }

    fun getSubjectById(id: Long): SubjectResponse {
        logger.info("Get subject id=$id")
        return findSubjectOrThrow(id).toResponse()
    }

    fun updateSubject(id: Long, request: SubjectRequest): SubjectResponse {
        logger.info("Updating subject id=$id")
        val subject = findSubjectOrThrow(id)
        val professor = findProfessorOrThrow(request.professorId)

        subject.name = request.name
        subject.code = request.code
        subject.professor = professor
        return subjectRepository.save(subject).toResponse()
    }

    fun deleteSubject(id: Long) {
        logger.info("Deleting subject id=$id")
        val subject = findSubjectOrThrow(id)
        subjectRepository.delete(subject)
    }

    private fun findSubjectOrThrow(id: Long): Subject =
        subjectRepository.findById(id).orElseThrow {
            logger.warn("Subject not found id=$id")
            SubjectNotFoundException("Materia con id $id no encontrada.")
        }

    private fun findProfessorOrThrow(id: Long): Professor =
        professorRepository.findById(id).orElseThrow {
            logger.warn("Professor not found id=$id")
            ProfessorNotFound("Profesor con id $id no encontrado.")
        }
}
