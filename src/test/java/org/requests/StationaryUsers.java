package org.requests;
import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.Test;

import io.restassured.path.json.JsonPath;

public class StationaryUsers {
	
	int id;
	String name, baseURL="http://localhost:8080";
	
	@Test(priority=1)
	public void getUsers() {
		given()
			
		.when()
			.get(baseURL+"/users")
			
		.then()
			.statusCode(200)
			.log().all();
	}
	
	@Test(priority=2)
	public void createUser() {
		
		/*
		 * HashMap data = new HashMap(); data.put("name", "Aravind"); 
		 * data.put("gender", "Male"); 
		 * data.put("email", "aravind@gmail.com"); 
		 * data.put("password", "1809Aravind");
		 */
		
		Stationary_POJO data = new Stationary_POJO();
		data.setName("Aravind");
		data.setGender("Male");
		data.setEmail("aravind@gmail.com");
		data.setPassword("1809Aravind");
		
		JsonPath jsonPath = given()
			.contentType("application/json")
			.body(data)
			
		.when()
			.post(baseURL+"/users")
			.jsonPath();
		
		id = jsonPath.getInt("id");
		name = jsonPath.getString("name");
			
		jsonPath.prettyPrint();
		System.out.println("ID: "+id);
		System.out.println("Name: "+name);
		

	}
	
	@Test(priority=3, dependsOnMethods = {"createUser"})
	private void updateUser() {

	    Map<String, Object> data = new HashMap<>();
	    data.put("name", "Aravind Alan");
		data.put("gender", "Male");
		data.put("email", "aravind@gmail.com");
		data.put("password", "1809Aravind");
		
	    given()
	        .contentType("application/json")
	        .body(data)

	    .when()
	        .put(baseURL + "/users/" + id)  

	    .then()
	        .statusCode(200)
	        .log().all();
	}

	
	@Test(priority=4, dependsOnMethods = {"createUser"})
	public void deleteUser() {
		
		given()
		
		.when()
			.delete(baseURL+"/users/"+id)
		
		.then()
			.statusCode(200)
			.log().all();
	}
}
