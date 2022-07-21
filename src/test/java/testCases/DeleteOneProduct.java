package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import static io.restassured.RestAssured.*;
import java.util.HashMap;

public class DeleteOneProduct {
	
	//Creating a method that returns a HashMap. Inside this, we create a Hashmap variable to store the payload key and value pairs
	public HashMap<String,String> deletePayLoadMap() {
		
		HashMap<String,String> payloadObj = new HashMap<String,String>();
		payloadObj.put("id", "4778");
		return payloadObj;
	}
	
	@Test(priority=1)
	public void deleteOneProduct() {
		
		/*
		given: all input details (base URI, Headers, Payload/Body, QueryParameters, Authorization)
		when: submit api requests (HTTP method, Endpoint/Resource)
		then: validate response (status code, responseHeaders, responseTime, Payload/Body)
		*/
		
		Response response = given()
								.baseUri("https://techfios.com/api-prod/api/product")
								.header("Content-Type","application/json; charset=UTF-8")
								.header("Authorization","Bearer 0987654321") //using bearer token as authorization
								.body(deletePayLoadMap()) //taking the method that holds our stored hashmap
								.relaxedHTTPSValidation().
							when()
								.delete("/delete.php"). //HTTP method: DELETE and the end point is: /delete.php
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
		Assert.assertEquals(productMessage, "Product was deleted.");
		
	}
	
	@Test(priority=2)
	public void validateProductDeleteDetails() {
	
		Response response = given()
								.baseUri("https://techfios.com/api-prod/api/product")
								.header("Content-Type","application/json; charset=UTF-8")
								.header("Authorization","Bearer 0987654321") //using bearer token as authorization
								.queryParam("id", deletePayLoadMap().get("id"))
								.relaxedHTTPSValidation().
							when()
								.get("/read_one.php").
							then()
								.extract().response();
		
		//to validate response status code
		int actualResponseStatus = response.getStatusCode();
		System.out.println("Response Status : " + actualResponseStatus);
		Assert.assertEquals(actualResponseStatus, 404);
		
		//to validate the response body
		String actualResponseBody = response.getBody().asString();
		System.out.println("Actual Response Body: " + actualResponseBody);
		
		//Created a JsonPath object: jp so that we can use this object later on to plug our key and return the corresponding value of the key
		JsonPath jp = new JsonPath(actualResponseBody);
		
		//validating product message on the console
		String productMessage = jp.get("message");
		System.out.println("Product Message : " + productMessage);
		Assert.assertEquals(productMessage, "Product does not exist.");
	
	}
	
}
