package com.pucetec_andrescriollo.students.services

import com.pucetec_andrescriollo.students.dto.EnrollmentRequest
import com.pucetec_andrescriollo.students.dto.EnrollmentUpdateRequest
import com.pucetec_andrescriollo.students.entities.Enrollment
import com.pucetec_andrescriollo.students.entities.Professor
import com.pucetec_andrescriollo.students.entities.Student
import com.pucetec_andrescriollo.students.entities.Subject
import com.pucetec_andrescriollo.students.exceptions.DuplicateEnrollmentException
import com.pucetec_andrescriollo.students.exceptions.EnrollmentNotFoundException
import com.pucetec_andrescriollo.students.exceptions.StudentNotFoundException
import com.pucetec_andrescriollo.students.exceptions.SubjectNotFoundException
import com.pucetec_andrescriollo.students.repositories.EnrollmentRepository
import com.pucetec_andrescriollo.students.repositories.StudentRepository
import com.pucetec_andrescriollo.students.repositories.SubjectRepository
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import java.util.Optional
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith


@ExtendWith(value = [MockitoExtension::class])
class EnrollmentServiceTest {

    @Mock
    private lateinit var studentRepository: StudentRepository

    @Mock
    private lateinit var subjectRepository: SubjectRepository

    @Mock
    private lateinit var enrollmentRepository: EnrollmentRepository

    @InjectMocks
    private lateinit var enrollmentService: EnrollmentService

    @Test
    fun `createEnrollment returns response when student and subject exist and no duplicate enrollment`() {
        val request = EnrollmentRequest(studentId = 1L, subjectId = 1L, status = "INSCRITO")
        val professor = Professor(id = 1L, name = "Dr. Smith", email = "smith@puce.edu")
        val student = Student(id = 1L, name = "Ana Lopez", email = "ana@puce.edu")
        val subject = Subject(id = 1L, name = "Mathematics", code = "MAT101", professor = professor)
        val savedEnrollment = Enrollment(
            id = 1L,
            createdAt = java.time.LocalDateTime.now(),
            status = "INSCRITO",
            subject = subject,
            student = student
        )
        whenever(studentRepository.findById(1L)).thenReturn(Optional.of(student))
        whenever(subjectRepository.findById(1L)).thenReturn(Optional.of(subject))
        whenever(enrollmentRepository.existsByStudent_IdAndSubject_Id(1L, 1L)).thenReturn(false)
        whenever(enrollmentRepository.save(any())).thenReturn(savedEnrollment)

        val response = enrollmentService.createEnrollment(request)

        assertEquals(1L, response.id)
        assertEquals("INSCRITO", response.status)
        assertEquals(1L, response.student.id)
        assertEquals(1L, response.subject.id)
    }

    @Test
    fun `createEnrollment throws StudentNotFoundException when student does not exist`() {
        val request = EnrollmentRequest(studentId = 99L, subjectId = 1L, status = "INSCRITO")
        whenever(studentRepository.findById(99L)).thenReturn(Optional.empty())

        assertFailsWith<StudentNotFoundException> {
            enrollmentService.createEnrollment(request)
        }
    }

    @Test
    fun `createEnrollment throws SubjectNotFoundException when subject does not exist`() {
        val request = EnrollmentRequest(studentId = 1L, subjectId = 99L, status = "INSCRITO")
        val student = Student(id = 1L, name = "Ana Lopez", email = "ana@puce.edu")
        whenever(studentRepository.findById(1L)).thenReturn(Optional.of(student))
        whenever(subjectRepository.findById(99L)).thenReturn(Optional.empty())

        assertFailsWith<SubjectNotFoundException> {
            enrollmentService.createEnrollment(request)
        }
    }

    @Test
    fun `createEnrollment throws DuplicateEnrollmentException when enrollment already exists`() {
        val request = EnrollmentRequest(studentId = 1L, subjectId = 1L, status = "INSCRITO")
        val professor = Professor(id = 1L, name = "Dr. Smith", email = "smith@puce.edu")
        val student = Student(id = 1L, name = "Ana Lopez", email = "ana@puce.edu")
        val subject = Subject(id = 1L, name = "Mathematics", code = "MAT101", professor = professor)
        whenever(studentRepository.findById(1L)).thenReturn(Optional.of(student))
        whenever(subjectRepository.findById(1L)).thenReturn(Optional.of(subject))
        whenever(enrollmentRepository.existsByStudent_IdAndSubject_Id(1L, 1L)).thenReturn(true)

        assertFailsWith<DuplicateEnrollmentException> {
            enrollmentService.createEnrollment(request)
        }
    }

    @Test
    fun `getAllEnrollments returns list of enrollments`() {
        val professor = Professor(id = 1L, name = "Dr. Smith", email = "smith@puce.edu")
        val student = Student(id = 1L, name = "Ana Lopez", email = "ana@puce.edu")
        val subject = Subject(id = 1L, name = "Mathematics", code = "MAT101", professor = professor)
        val enrollment1 = Enrollment(
            id = 1L,
            createdAt = java.time.LocalDateTime.now(),
            status = "INSCRITO",
            subject = subject,
            student = student
        )
        val enrollment2 = Enrollment(
            id = 2L,
            createdAt = java.time.LocalDateTime.now(),
            status = "APROBADO",
            subject = subject,
            student = student
        )
        whenever(enrollmentRepository.findAll()).thenReturn(listOf(enrollment1, enrollment2))

        val responses = enrollmentService.getAllEnrollments()

        assertEquals(2, responses.size)
        assertEquals(1L, responses[0].id)
        assertEquals("INSCRITO", responses[0].status)
        assertEquals(2L, responses[1].id)
        assertEquals("APROBADO", responses[1].status)
    }

    @Test
    fun `getEnrollmentById returns enrollment when exists`() {
        val professor = Professor(id = 1L, name = "Dr. Smith", email = "smith@puce.edu")
        val student = Student(id = 1L, name = "Ana Lopez", email = "ana@puce.edu")
        val subject = Subject(id = 1L, name = "Mathematics", code = "MAT101", professor = professor)
        val enrollment = Enrollment(
            id = 1L,
            createdAt = java.time.LocalDateTime.now(),
            status = "INSCRITO",
            subject = subject,
            student = student
        )
        whenever(enrollmentRepository.findById(1L)).thenReturn(Optional.of(enrollment))

        val response = enrollmentService.getEnrollmentById(1L)

        assertEquals(1L, response.id)
        assertEquals("INSCRITO", response.status)
    }

    @Test
    fun `getEnrollmentById throws EnrollmentNotFoundException when not exists`() {
        whenever(enrollmentRepository.findById(99L)).thenReturn(Optional.empty())

        assertFailsWith<EnrollmentNotFoundException> {
            enrollmentService.getEnrollmentById(99L)
        }
    }

    @Test
    fun `updateEnrollment returns response when enrollment exists`() {
        val request = EnrollmentUpdateRequest(status = "APROBADO")
        val professor = Professor(id = 1L, name = "Dr. Smith", email = "smith@puce.edu")
        val student = Student(id = 1L, name = "Ana Lopez", email = "ana@puce.edu")
        val subject = Subject(id = 1L, name = "Mathematics", code = "MAT101", professor = professor)
        val existingEnrollment = Enrollment(
            id = 1L,
            createdAt = java.time.LocalDateTime.now(),
            status = "INSCRITO",
            subject = subject,
            student = student
        )
        val updatedEnrollment = Enrollment(
            id = 1L,
            createdAt = java.time.LocalDateTime.now(),
            status = "APROBADO",
            subject = subject,
            student = student
        )
        whenever(enrollmentRepository.findById(1L)).thenReturn(Optional.of(existingEnrollment))
        whenever(enrollmentRepository.save(any())).thenReturn(updatedEnrollment)

        val response = enrollmentService.updateEnrollment(1L, request)

        assertEquals(1L, response.id)
        assertEquals("APROBADO", response.status)
    }

    @Test
    fun `updateEnrollment throws EnrollmentNotFoundException when enrollment does not exist`() {
        val request = EnrollmentUpdateRequest(status = "APROBADO")
        whenever(enrollmentRepository.findById(99L)).thenReturn(Optional.empty())

        assertFailsWith<EnrollmentNotFoundException> {
            enrollmentService.updateEnrollment(99L, request)
        }
    }

    @Test
    fun `deleteEnrollment deletes enrollment when exists`() {
        val professor = Professor(id = 1L, name = "Dr. Smith", email = "smith@puce.edu")
        val student = Student(id = 1L, name = "Ana Lopez", email = "ana@puce.edu")
        val subject = Subject(id = 1L, name = "Mathematics", code = "MAT101", professor = professor)
        val enrollment = Enrollment(
            id = 1L,
            createdAt = java.time.LocalDateTime.now(),
            status = "INSCRITO",
            subject = subject,
            student = student
        )
        whenever(enrollmentRepository.findById(1L)).thenReturn(Optional.of(enrollment))

        enrollmentService.deleteEnrollment(1L)
    }

    @Test
    fun `deleteEnrollment throws EnrollmentNotFoundException when not exists`() {
        whenever(enrollmentRepository.findById(99L)).thenReturn(Optional.empty())

        assertFailsWith<EnrollmentNotFoundException> {
            enrollmentService.deleteEnrollment(99L)
        }
    }
}
