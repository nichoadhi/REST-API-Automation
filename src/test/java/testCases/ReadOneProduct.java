package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import static io.restassured.RestAssured.*;

import java.util.concurrent.TimeUnit;

public class ReadOneProduct {
	
	@Test
	public void readOneProduct() {
	/*
		given: all input details (base URI, Headers, Payload/Body, QueryParameters, Authorization)
		when: submit api requests (HTTP method, Endpoint/Resource)
		then: validate response (status code, responseHeaders, responseTime, Payload/Body)

		http Method: GET
		EndpointURL:  https://techfios.com/api-prod/api/product/read_one.php
		Authorization: basic auth/Bearer Token
		Header:
		"Content-Type" : "application/json"
		Query Params:
		"id": "4732"
		status code: 200
		*/
	
		Response response = given()
								.baseUri("https://techfios.com/api-prod/api/product")
								.header("Content-Type","application/json; charset=UTF-8")
								.header("Authorization","Bearer 0987654321") //using bearer token as authorization
								.queryParam("id", "4732")
								.relaxedHTTPSValidation().
							when()
								.get("/read_one.php").
							then()
								.extract().response();
		
		//to validate response status
		int actualResponseStatus = response.getStatusCode();
		System.out.println("Actual Response Status : " + actualResponseStatus);
		Assert.assertEquals(actualResponseStatus, 200);
		
		//to validate response header content type
		String actualResponseContentType = response.getHeader("Content-Type");
		System.out.println("Actual Response Content Type : " + actualResponseContentType);
		Assert.assertEquals(actualResponseContentType, "application/json");
		
		//to validate the response time
		long actualResponseTime= response.getTimeIn(TimeUnit.MILLISECONDS);
		System.out.println("Actual Response Time : " + actualResponseTime);
		
		if(actualResponseTime <=2000) {
			System.out.println("Response time is within range");
		}
		else {
			System.out.println("Response time is out of range !!!");
		}
		
		//to validate the response body (validating productName, ID, Description and Price)
		String actualResponseBody = response.getBody().asString();
		System.out.println("Actual Response Body: " + actualResponseBody);
		
		JsonPath jp = new JsonPath(actualResponseBody);
		
		String ProductID = jp.get("id");
		System.out.println("Product ID : " + ProductID);
		Assert.assertEquals(ProductID, "4732");
		
		String ProductName = jp.get("name");
		System.out.println("Product Name : " + ProductName);
		Assert.assertEquals(ProductName, "Amazing Pillow 2.0 By Aref");
		
		String productDescription = jp.get("description");
		System.out.println("Product Description : " + productDescription);
		Assert.assertEquals(productDescription, "The best pillow for amazing programmers.");
		
		String productPrice = jp.get("price");
		System.out.println("Product Price : " + productPrice);
		Assert.assertEquals(productPrice, "199");
	}
}
