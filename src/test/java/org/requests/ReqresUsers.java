package org.requests;
import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.json.JSONTokener;
import org.testng.annotations.Test;

import io.restassured.path.json.JsonPath;

public class ReqresUsers {
	
	int id;
	String name, baseURL="https://reqres.in/";
	
	@Test(priority=1)
	public void getUsers() {
		given()
			.header("x-api-key", "reqres-free-v1")
			
		.when()
			.get(baseURL+"api/users?page=2")
			
		.then()
			.statusCode(200)
			.log().all();
	}
	
	@Test(priority=2)
	public void createUser() throws IOException{
		
	/*	HashMap data = new HashMap();
		data.put("name", "Arun");
		data.put("job", "Tester");*/
		
		File f = new File(".\\src/test/resources/ReqresCreateUserBody.json");
		FileReader fr = new FileReader(f);
		JSONTokener jt = new JSONTokener(fr);
		JSONObject data = new JSONObject(jt);
		
		JsonPath jsonPath = given()
			.contentType("application/json")
			.header("x-api-key", "reqres-free-v1")
			.body(data.toString())
			
		.when()
			.post(baseURL+"api/users")
			.jsonPath();
		
		id = jsonPath.getInt("id");
		name = jsonPath.getString("name");
			
		jsonPath.prettyPrint();
		System.out.println("ID: "+id);
		System.out.println("Name: "+name);
		

	}
	
	@Test(priority=3, dependsOnMethods = {"createUser"})
	private void updateUser() {
		Map data = new HashMap();
		data.put("name", "Arun Prabhu");
		
		given()
			.contentType("application/json")
			.header("x-api-key", "reqres-free-v1")
			.body(data)
		
		.when()
			.put(baseURL+"api/users/"+id)
		
		.then()
			.statusCode(200)
			.log().all();

	}
	
	@Test(priority=4, dependsOnMethods = {"createUser"})
	public void deleteUser() {
		
		given()
			.header("x-api-key", "reqres-free-v1")
		
		.when()
			.delete(baseURL+"api/users/"+id)
		
		.then()
			.statusCode(204)
			.log().all();
	}
	
	@Test
	public void listOfUsers() {
		
		given()
			.header("x-api-key", "reqres-free-v1")
			
		.when()
			.get(baseURL+"api/unkown")
		
		.then()
			.statusCode(200)
			.body("total", equalTo(12))
			.body("data[0].name", equalTo("cerulean"))
			.body("data[1].id", equalTo(2))
			.body("data[2].color", equalTo("#BF1932"))
			.log().all();
	}
}
