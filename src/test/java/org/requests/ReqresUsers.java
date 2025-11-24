package org.requests;
import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;
import org.json.JSONTokener;
import org.testng.annotations.Test;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

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
	public void updateUser() {
		Map<String, String> data = new HashMap<String, String>();
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
	
	@Test
	public void getUsersUsingPathAndQueryParameters() {
		
		given()
			.header("x-api-key", "reqres-free-v1")
			.pathParam("customPath1", "users")
			.queryParam("page", 2)
			.queryParam("id", 6)
			
		.when()
			//This will pass the url "https://reqres.in/api/users?page=2&id=5"
			//Query parameters need not be mentioned in curly braces, since it will be automatically be passed into the request from the queryParam method
			.get(baseURL+"api/{customPath1}") 
			
		.then()
			.statusCode(200)
			.log().all()
			.body("data.id", equalTo(6));
	}
	
	@Test
	public void getCookiesInfo() {
		
		Response response = given()
		
		.when()
			.get("https://www.google.com/");
		
		System.out.println(response.getCookie("AEC"));
		
		Map<String, String> cookies = response.getCookies();
		Set<String> keySet = cookies.keySet();
		
		for (String key : keySet) {
			String value = response.getCookie(key);
			System.out.println(key+": "+value);
		}
			

	}
}
