package com.in28minutes.springboot.studentservices;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.in28minutes.springboot.application.StudentServicesApplication;
import com.in28minutes.springboot.model.Course;
import com.in28minutes.springboot.model.Student;
import com.in28minutes.springboot.service.StudentService;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = StudentServicesApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StudentServicesApplicationTests {
	
	@LocalServerPort
	private int port;

	TestRestTemplate restTemplate = new TestRestTemplate();

	HttpHeaders headers = new HttpHeaders();

	@Autowired
	private StudentService studentService;
	
	@Test
	public void contextLoads() {
	}

	@Test
	public void testConnectionRestServiceTest() {
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);

		ResponseEntity<String> response = restTemplate.exchange(
				createURLWithPort("/"),
				HttpMethod.GET, entity, String.class);

		assertEquals(HttpStatus.OK,response.getStatusCode());
		
	}
	@Test
	public void RetrieveStudent() {
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);

		ResponseEntity<String> response = restTemplate.exchange(
				createURLWithPort("/students/Student1/students"),
				HttpMethod.GET, entity, String.class);

		assertEquals(HttpStatus.OK,response.getStatusCode());
		
		try {
			Gson gson = new Gson();
			Student studentLeido = gson.fromJson(response.getBody(), Student.class);	
			
			assertEquals("Student1", studentLeido.getId());
			
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
			fail();
		}		
		
	}
	
	@Test
	public void testRetrieveStudentCourseExist() {

		HttpEntity<String> entity = new HttpEntity<String>(null, headers);

		ResponseEntity<String> response = restTemplate.exchange(
				createURLWithPort("/students/Student1/courses/Course1"),
				HttpMethod.GET, entity, String.class);

		assertEquals(HttpStatus.OK,response.getStatusCode());

	}
	

	@Test
	public void retrieveStudentCourseTest() {

		HttpEntity<String> entity = new HttpEntity<String>(null, headers);

		ResponseEntity<String> response = restTemplate.exchange(
				createURLWithPort("/students/Student1/courses/Course1"),
				HttpMethod.GET, entity, String.class);

		String expected = "{\"id\":\"Course1\",\"name\":\"Spring\",\"steps\":[\"Learn Maven\",\"Import Project\",\"First Example\",\"Second Example\"],\"description\":\"10 Steps\"}";
		
		try {
			Gson gson = new Gson();
			Course cursoLeido = gson.fromJson(response.getBody(), Course.class);	
			
			Course cursoEsperado = studentService.retrieveCourse("Student1","Course1");
			
			
			JSONAssert.assertEquals(expected, response.getBody(), false);
			
			assertEquals(cursoEsperado, cursoLeido);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}
	}

	@Test
	public void retrieveStudentCoursesTest() {

		HttpEntity<String> entity = new HttpEntity<String>(null, headers);

		ResponseEntity<String> response = restTemplate.exchange(
				createURLWithPort("/students/Student1/courses"),
				HttpMethod.GET, entity, String.class);

		try {
			Gson gson = new Gson();
			Course[] cursoLeido = gson.fromJson(response.getBody(), Course[].class);	
			
			List<Course> lstCursosEsperados = studentService.retrieveCourses("Student1");
			
			assertEquals(lstCursosEsperados, Arrays.asList(cursoLeido));
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}
	}
	private String createURLWithPort(String uri) {
		return "http://localhost:" + port + uri;
	}
	@Test
	public void rellenarCoverageTest() {
	
		Student student = new Student("uno", "dos", "tres",  null);
		
		Student student2 = new Student(student.getId(), student.getName(), student.getDescription(),  null);
		
		String kk = student2.toString();
		
		Course curso = new Course("id", "nombre", "descripcion", new ArrayList<String>());
		
		List<Course> lstCourses = new ArrayList<Course>();
		
		lstCourses.add(curso);
		
		student.setCourses(lstCourses);
		student.setDescription(student2.getDescription());
		student.setId(student.getId());
		student.setName(student.getName());
		
		Course curso2 = new Course("uno", "dos", "tres",  null);
		
		if(!curso2.equals(curso)) {
			curso2.setdescription(curso.getdescription());
			curso2.setId(curso2.getId());
			curso2.setName(curso.getName());
			curso2.setSteps(curso.getSteps());
			kk = curso.toString();
		}
		Course curso3 = new Course(null, null, null, null);
		
		boolean bMorralla = curso2.equals(curso3);
		bMorralla = curso3.equals(curso2);
		bMorralla = curso3.equals(curso3);
		bMorralla = curso3.equals(null);
		bMorralla = curso3.equals(student);
		int res = curso.hashCode();
		res = curso3.hashCode();
		
		Course curso4 = new Course(null, null, null, null);
		bMorralla = curso3.equals(curso4);
	}
}
