package com.pucetec_andrescriollo.students.controllers

import com.pucetec_andrescriollo.students.dto.EnrollmentRequest
import com.pucetec_andrescriollo.students.dto.EnrollmentResponse
import com.pucetec_andrescriollo.students.dto.EnrollmentUpdateRequest
import com.pucetec_andrescriollo.students.services.EnrollmentService
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
@RequestMapping("/api/enrollments")
class EnrollmentController(
    private val enrollmentService: EnrollmentService,
) {
    private val logger = LoggerFactory.getLogger(EnrollmentController::class.java)

    @PostMapping
    fun createEnrollment(
        @Valid @RequestBody request: EnrollmentRequest
    ): ResponseEntity<EnrollmentResponse> {
        logger.info("Creating enrollment student=${request.studentId} subject=${request.subjectId}")
        val created = enrollmentService.createEnrollment(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(created)
    }

    @GetMapping
    fun getAllEnrollments(): ResponseEntity<List<EnrollmentResponse>> {
        logger.info("Getting all enrollments")
        return ResponseEntity.ok(enrollmentService.getAllEnrollments())
    }

    @GetMapping("/{id}")
    fun getEnrollmentById(@PathVariable id: Long): ResponseEntity<EnrollmentResponse> {
        logger.info("Getting enrollment id=$id")
        return ResponseEntity.ok(enrollmentService.getEnrollmentById(id))
    }

    @PutMapping("/{id}")
    fun updateEnrollment(
        @PathVariable id: Long,
        @Valid @RequestBody request: EnrollmentUpdateRequest
    ): ResponseEntity<EnrollmentResponse> {
        logger.info("Updating enrollment id=$id")
        return ResponseEntity.ok(enrollmentService.updateEnrollment(id, request))
    }

    @DeleteMapping("/{id}")
    fun deleteEnrollment(@PathVariable id: Long): ResponseEntity<Void> {
        logger.info("Deleting enrollment id=$id")
        enrollmentService.deleteEnrollment(id)
        return ResponseEntity.noContent().build()
    }
}
