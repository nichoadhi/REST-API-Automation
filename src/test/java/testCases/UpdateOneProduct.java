package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import static io.restassured.RestAssured.*;
import java.util.HashMap;

public class UpdateOneProduct {
	
	//creating a global variable 
	HashMap<String,String> payloadObj;
	
	//Creating a method that returns a HashMap. Inside this, we create a Hashmap variable to store the payload key and value pairs
	public HashMap<String,String> updatePayLoadMap() {
		
		payloadObj = new HashMap<String,String>();
		payloadObj.put("id", "4780");
		payloadObj.put("name", "Nicholas iWatch version2");
		payloadObj.put("description", "watch series 6");
		payloadObj.put("price", "999");
		payloadObj.put("category_id", "3");
		payloadObj.put("category_name", "Motors");
		
		return payloadObj;
	}
	
	@Test(priority=1)
	public void updateOneProduct() {
		
		/*
		given: all input details (base URI, Headers, Payload/Body, QueryParameters, Authorization)
		when: submit api requests (HTTP method, Endpoint/Resource)
		then: validate response (status code, responseHeaders, responseTime, Payload/Body)
		*/
		
		Response response = given()
								.baseUri("https://techfios.com/api-prod/api/product")
								.header("Content-Type","application/json; charset=UTF-8")
								.header("Authorization","Bearer 0987654321") //using bearer token as authorization
								.body(updatePayLoadMap()) //taking the method that holds our stored hashmap
								.relaxedHTTPSValidation().
							when()
								.put("/update.php"). //HTTP method: PUT and the end point is: /create.php
							then()
								.extract().response();
		
		//to validate response status
		int actualResponseStatus = response.getStatusCode();
		System.out.println("Response Status : " + actualResponseStatus);
		Assert.assertEquals(actualResponseStatus, 200);
		
		//to validate response header content type
		String actualResponseContentType = response.getHeader("Content-Type");
		System.out.println("Response Content Type : " + actualResponseContentType);
		Assert.assertEquals(actualResponseContentType, "application/json; charset=UTF-8");
		
		//to validate the response body (validating productMessage)
		String actualResponseBody = response.getBody().asString();
		System.out.println("Response Body: " + actualResponseBody);
		
		JsonPath jp = new JsonPath(actualResponseBody);
		
		String productMessage = jp.get("message");
		System.out.println("Product Message : " + productMessage);
		Assert.assertEquals(productMessage, "Product was updated.");
		
	}
	
	
	@Test(priority=2)
	public void validateProductUpdateDetails() {
	
		Response response = given()
								.baseUri("https://techfios.com/api-prod/api/product")
								.header("Content-Type","application/json; charset=UTF-8")
								.header("Authorization","Bearer 0987654321") //using bearer token as authorization
								.queryParam("id", updatePayLoadMap().get("id"))
								.relaxedHTTPSValidation().
							when()
								.get("/read_one.php").
							then()
								.extract().response();
		
		//to validate response status code
		int actualResponseStatus = response.getStatusCode();
		System.out.println("Response Status : " + actualResponseStatus);
		Assert.assertEquals(actualResponseStatus, 200);
		
		//to validate the response body
		String actualResponseBody = response.getBody().asString();
		System.out.println("Actual Response Body: " + actualResponseBody);
		
		//Created a JsonPath object: jp so that we can use this object later on to plug our key and return the corresponding value of the key
		JsonPath jp = new JsonPath(actualResponseBody);
		
		//List of variables coming from createPayLoadMap() method, that we will use later to assert (expected vs actual)
		String expectedProductName = updatePayLoadMap().get("name");
		String expectedProductDescription = updatePayLoadMap().get("description");
		String expectedProductPrice = updatePayLoadMap().get("price");
		
		//Assertion of product name (name that we are reading from the system vs. name that we have entered into the system)
		String actualProductName = jp.get("name");
		System.out.println("Product Name : " + actualProductName);
		Assert.assertEquals(actualProductName, expectedProductName);
		
		//Assertion of Product Description (Description that we are reading from the system vs. Description that we have entered into the system)
		String actualProductDescription = jp.get("description");
		System.out.println("Product Description : " + actualProductDescription);
		Assert.assertEquals(actualProductDescription, expectedProductDescription);
		
		//Assertion of Product Price (Price that we are reading from the system vs. Price that we have entered into the system)
		String actualProductPrice = jp.get("price");
		System.out.println("Product Price : " + actualProductPrice);
		Assert.assertEquals(actualProductPrice, expectedProductPrice);
	}
	
}
