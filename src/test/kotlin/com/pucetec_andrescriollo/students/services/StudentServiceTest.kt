package com.pucetec_andrescriollo.students.services

import com.pucetec_andrescriollo.students.dto.StudentRequest
import com.pucetec_andrescriollo.students.dto.StudentResponse
import com.pucetec_andrescriollo.students.entities.Student
import com.pucetec_andrescriollo.students.exceptions.EmailAlreadyExistsException
import com.pucetec_andrescriollo.students.exceptions.StudentNotFoundException
import com.pucetec_andrescriollo.students.repositories.StudentRepository
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
class StudentServiceTest {

    @Mock
    private lateinit var studentRepository: StudentRepository

    @InjectMocks
    private lateinit var studentService: StudentService

    @Test
    fun `createStudent returns response when email is not registered`() {
        val request = StudentRequest(name = "Ana Lopez", email = "ana@puce.edu")
        val savedStudent = Student(id = 1L, name = "Ana Lopez", email = "ana@puce.edu")
        whenever(studentRepository.existsByEmail(request.email)).thenReturn(false)
        whenever(studentRepository.save(any())).thenReturn(savedStudent)

        val response = studentService.createStudent(request)

        assertEquals(1L, response.id)
        assertEquals("Ana Lopez", response.name)
        assertEquals("ana@puce.edu", response.email)
    }

    @Test
    fun `createStudent throws EmailAlreadyExistsException when email is already registered`() {
        val request = StudentRequest(name = "Ana Lopez", email = "ana@puce.edu")
        whenever(studentRepository.existsByEmail(request.email)).thenReturn(true)

        assertFailsWith<EmailAlreadyExistsException> {
            studentService.createStudent(request)
        }
    }

    @Test
    fun `getAllStudents returns list of students`() {
        val student1 = Student(id = 1L, name = "Ana Lopez", email = "ana@puce.edu")
        val student2 = Student(id = 2L, name = "Carlos Ruiz", email = "carlos@puce.edu")
        whenever(studentRepository.findAll()).thenReturn(listOf(student1, student2))

        val responses = studentService.getAllStudents()

        assertEquals(2, responses.size)
        assertEquals(1L, responses[0].id)
        assertEquals("Ana Lopez", responses[0].name)
        assertEquals(2L, responses[1].id)
        assertEquals("Carlos Ruiz", responses[1].name)
    }

    @Test
    fun `getStudentById returns student when exists`() {
        val student = Student(id = 1L, name = "Ana Lopez", email = "ana@puce.edu")
        whenever(studentRepository.findById(1L)).thenReturn(Optional.of(student))

        val response = studentService.getStudentById(1L)

        assertEquals(1L, response.id)
        assertEquals("Ana Lopez", response.name)
    }

    @Test
    fun `getStudentById throws StudentNotFoundException when not exists`() {
        whenever(studentRepository.findById(99L)).thenReturn(Optional.empty())

        assertFailsWith<StudentNotFoundException> {
            studentService.getStudentById(99L)
        }
    }

    @Test
    fun `updateStudent returns response when email is not changed`() {
        val request = StudentRequest(name = "Ana Updated", email = "ana@puce.edu")
        val existingStudent = Student(id = 1L, name = "Ana Lopez", email = "ana@puce.edu")
        val updatedStudent = Student(id = 1L, name = "Ana Updated", email = "ana@puce.edu")
        whenever(studentRepository.findById(1L)).thenReturn(Optional.of(existingStudent))
        whenever(studentRepository.save(any())).thenReturn(updatedStudent)

        val response = studentService.updateStudent(1L, request)

        assertEquals(1L, response.id)
        assertEquals("Ana Updated", response.name)
        assertEquals("ana@puce.edu", response.email)
    }

    @Test
    fun `updateStudent returns response when email is changed but new email is not registered`() {
        val request = StudentRequest(name = "Ana Updated", email = "ana.new@puce.edu")
        val existingStudent = Student(id = 1L, name = "Ana Lopez", email = "ana@puce.edu")
        val updatedStudent = Student(id = 1L, name = "Ana Updated", email = "ana.new@puce.edu")
        whenever(studentRepository.findById(1L)).thenReturn(Optional.of(existingStudent))
        whenever(studentRepository.existsByEmail("ana.new@puce.edu")).thenReturn(false)
        whenever(studentRepository.save(any())).thenReturn(updatedStudent)

        val response = studentService.updateStudent(1L, request)

        assertEquals(1L, response.id)
        assertEquals("Ana Updated", response.name)
        assertEquals("ana.new@puce.edu", response.email)
    }

    @Test
    fun `updateStudent throws EmailAlreadyExistsException when email is changed and new email is registered`() {
        val request = StudentRequest(name = "Ana Updated", email = "ana.new@puce.edu")
        val existingStudent = Student(id = 1L, name = "Ana Lopez", email = "ana@puce.edu")
        whenever(studentRepository.findById(1L)).thenReturn(Optional.of(existingStudent))
        whenever(studentRepository.existsByEmail("ana.new@puce.edu")).thenReturn(true)

        assertFailsWith<EmailAlreadyExistsException> {
            studentService.updateStudent(1L, request)
        }
    }

    @Test
    fun `updateStudent throws StudentNotFoundException when student not exists`() {
        val request = StudentRequest(name = "Ana Updated", email = "ana@puce.edu")
        whenever(studentRepository.findById(99L)).thenReturn(Optional.empty())

        assertFailsWith<StudentNotFoundException> {
            studentService.updateStudent(99L, request)
        }
    }

    @Test
    fun `deleteStudent deletes student when exists`() {
        val student = Student(id = 1L, name = "Ana Lopez", email = "ana@puce.edu")
        whenever(studentRepository.findById(1L)).thenReturn(Optional.of(student))

        studentService.deleteStudent(1L)
    }

    @Test
    fun `deleteStudent throws StudentNotFoundException when not exists`() {
        whenever(studentRepository.findById(99L)).thenReturn(Optional.empty())

        assertFailsWith<StudentNotFoundException> {
            studentService.deleteStudent(99L)
        }
    }
}
