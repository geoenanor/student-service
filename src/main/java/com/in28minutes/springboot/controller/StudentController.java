package com.in28minutes.springboot.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.in28minutes.springboot.model.Course;
import com.in28minutes.springboot.model.Student;
import com.in28minutes.springboot.service.StudentService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

//@RequestMapping("/api/v1")
@RestController
@Api(value="Sistema Gestión Estudiantes", description="Operaciones sobre estudiantes y cursos")
public class StudentController {

	@Autowired
	private StudentService studentService;
	
	@ApiOperation(value="Obtener detalles de un curso en el que esta inscrito el estudiante", response = Course.class)
	@GetMapping(value="/students/{studentId}/courses/{courseId}")
	public Course retrieveDetailsForCourse(
			 @PathVariable String studentId ,
			@PathVariable String courseId) {
		
		return studentService.retrieveCourse(studentId, courseId);
		
	}

	@ApiOperation(value="Punto entrada a la app", response = String.class)
	@GetMapping("/")
	public String srvRestStudent() {
		return "Servicio Levantado";
	}
	
	@ApiOperation(value="Obtener listado de cursos en los que esta inscrito el estudiante", response = List.class)
	@GetMapping("/students/{studentId}/courses")
	public List<Course> retrieveCoursesForStudent(@PathVariable String studentId) {
		return studentService.retrieveCourses(studentId);
	}	
	
	@ApiOperation(value="Obtener detalles de un estudiante", response = Student.class)
	@GetMapping("/students/{studentId}/students")
	public Student retrieveStudent(@PathVariable String studentId) {
		return studentService.retrieveStudent(studentId);
	}
}
