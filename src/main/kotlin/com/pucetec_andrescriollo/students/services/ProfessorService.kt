package com.pucetec_andrescriollo.students.services

import com.pucetec_andrescriollo.students.dto.ProfessorRequest
import com.pucetec_andrescriollo.students.dto.ProfessorResponse
import com.pucetec_andrescriollo.students.exceptions.EmailAlreadyExistsException
import com.pucetec_andrescriollo.students.exceptions.ProfessorNotFound
import com.pucetec_andrescriollo.students.mappers.toEntity
import com.pucetec_andrescriollo.students.mappers.toResponse
import com.pucetec_andrescriollo.students.repositories.ProfessorRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class ProfessorService(
    private val professorRepository: ProfessorRepository,
) {
    private val logger = LoggerFactory.getLogger(ProfessorService::class.java)

    fun createProfessor(request: ProfessorRequest): ProfessorResponse {
        logger.info("Creating Professor ${request.name}")

        if (professorRepository.existsByEmail(request.email)) {
            logger.warn("Attempted to register duplicate professor email: ${request.email}")
            throw EmailAlreadyExistsException("El correo ${request.email} ya está registrado.")
        }

        val saved = professorRepository.save(request.toEntity())
        logger.info("Saved professor with id ${saved.id}")
        return saved.toResponse()
    }

    fun getAllProfessors(): List<ProfessorResponse> {
        logger.info("Get all Professors")
        return professorRepository.findAll().map { it.toResponse() }
    }

    fun getProfessorById(id: Long): ProfessorResponse {
        logger.info("Get professor id=$id")
        return findProfessorOrThrow(id).toResponse()
    }

    fun updateProfessor(id: Long, request: ProfessorRequest): ProfessorResponse {
        logger.info("Updating professor id=$id")
        val professor = findProfessorOrThrow(id)

        if (professor.email != request.email && professorRepository.existsByEmail(request.email)) {
            logger.warn("Email conflict on professor update: ${request.email}")
            throw EmailAlreadyExistsException("El correo ${request.email} ya está registrado.")
        }

        professor.name = request.name
        professor.email = request.email
        return professorRepository.save(professor).toResponse()
    }

    fun deleteProfessor(id: Long) {
        logger.info("Deleting professor id=$id")
        val professor = findProfessorOrThrow(id)
        professorRepository.delete(professor)
    }

    private fun findProfessorOrThrow(id: Long) =
        professorRepository.findById(id).orElseThrow {
            logger.warn("Professor not found id=$id")
            ProfessorNotFound("Profesor con id $id no encontrado.")
        }
}
