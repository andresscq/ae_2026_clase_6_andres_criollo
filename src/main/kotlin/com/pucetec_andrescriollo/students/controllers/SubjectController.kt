package com.pucetec_andrescriollo.students.controllers

import com.pucetec_andrescriollo.students.dto.SubjectRequest
import com.pucetec_andrescriollo.students.dto.SubjectResponse
import com.pucetec_andrescriollo.students.services.SubjectService
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
@RequestMapping("/api/subjects")
class SubjectController(
    private val subjectService: SubjectService,
) {
    private val logger = LoggerFactory.getLogger(SubjectController::class.java)

    @PostMapping
    fun createSubject(
        @Valid @RequestBody request: SubjectRequest
    ): ResponseEntity<SubjectResponse> {
        logger.info("Creating Subject ${request.name}")
        val created = subjectService.createSubject(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(created)
    }

    @GetMapping
    fun getAllSubjects(): ResponseEntity<List<SubjectResponse>> {
        logger.info("Getting all subjects")
        return ResponseEntity.ok(subjectService.getAllSubjects())
    }

    @GetMapping("/{id}")
    fun getSubjectById(@PathVariable id: Long): ResponseEntity<SubjectResponse> {
        logger.info("Getting subject id=$id")
        return ResponseEntity.ok(subjectService.getSubjectById(id))
    }

    @PutMapping("/{id}")
    fun updateSubject(
        @PathVariable id: Long,
        @Valid @RequestBody request: SubjectRequest
    ): ResponseEntity<SubjectResponse> {
        logger.info("Updating subject id=$id")
        return ResponseEntity.ok(subjectService.updateSubject(id, request))
    }

    @DeleteMapping("/{id}")
    fun deleteSubject(@PathVariable id: Long): ResponseEntity<Void> {
        logger.info("Deleting subject id=$id")
        subjectService.deleteSubject(id)
        return ResponseEntity.noContent().build()
    }
}
