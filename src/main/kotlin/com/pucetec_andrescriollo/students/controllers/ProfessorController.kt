package com.pucetec_andrescriollo.students.controllers

import com.pucetec_andrescriollo.students.dto.ProfessorRequest
import com.pucetec_andrescriollo.students.dto.ProfessorResponse
import com.pucetec_andrescriollo.students.services.ProfessorService
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/professors")
class ProfessorController(
    private val professorService: ProfessorService,
) {
    private val logger = LoggerFactory.getLogger(ProfessorController::class.java)

    @PostMapping
    fun createProfessor(
        @Valid @RequestBody request: ProfessorRequest
    ): ResponseEntity<ProfessorResponse> {
        logger.info("Creating Professor ${request.name}")
        val created = professorService.createProfessor(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(created)
    }

    @GetMapping
    fun getAllProfessors(): ResponseEntity<List<ProfessorResponse>> {
        logger.info("Getting all professors")
        return ResponseEntity.ok(professorService.getAllProfessors())
    }

    @GetMapping("/{id}")
    fun getProfessorById(@PathVariable id: Long): ResponseEntity<ProfessorResponse> {
        logger.info("Getting professor id=$id")
        return ResponseEntity.ok(professorService.getProfessorById(id))
    }

    @PutMapping("/{id}")
    fun updateProfessor(
        @PathVariable id: Long,
        @Valid @RequestBody request: ProfessorRequest
    ): ResponseEntity<ProfessorResponse> {
        logger.info("Updating professor id=$id")
        return ResponseEntity.ok(professorService.updateProfessor(id, request))
    }

    @DeleteMapping("/{id}")
    fun deleteProfessor(@PathVariable id: Long): ResponseEntity<Void> {
        logger.info("Deleting professor id=$id")
        professorService.deleteProfessor(id)
        return ResponseEntity.noContent().build()
    }
}
