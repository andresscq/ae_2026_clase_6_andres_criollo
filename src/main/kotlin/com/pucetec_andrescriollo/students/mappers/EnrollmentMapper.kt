package com.pucetec_andrescriollo.students.mappers

import com.pucetec_andrescriollo.students.dto.EnrollmentRequest
import com.pucetec_andrescriollo.students.dto.EnrollmentResponse
import com.pucetec_andrescriollo.students.entities.Enrollment
import com.pucetec_andrescriollo.students.entities.Student
import com.pucetec_andrescriollo.students.entities.Subject

fun EnrollmentRequest.toEntity(student: Student, subject: Subject) = Enrollment(
    status = this.status,
    student = student,
    subject = subject,
)

fun Enrollment.toResponse() = EnrollmentResponse(
    id = this.id,
    createdAt = this.createdAt.toString(),
    status = this.status,
    subject = this.subject.toResponse(),
    student = this.student.toResponse()
)
