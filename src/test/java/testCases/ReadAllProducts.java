package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import static io.restassured.RestAssured.*;

import java.util.concurrent.TimeUnit;

public class ReadAllProducts {
	
	@Test
	public void readAllProducts() {
	/*
		given: all input details (base URI, Headers, Payload/Body, QueryParameters, Authorization)
		when: submit api requests (HTTP method, Endpoint/Resource)
		then: validate response (status code, responseHeaders, responseTime, Payload/Body)

		HTTP Method: GET
		EndpointURL:  https://techfios.com/api-prod/api/product/read.php
		Header:
		"Content-Type" : "application/json; charset=UTF-8"
		status code: 200
		*/
	
		Response response = given()
								.baseUri("https://techfios.com/api-prod/api/product")
								.header("Content-Type","application/json; charset=UTF-8")
								.auth().basic("demo@techfios.com","abc123") //using basic authorization as username & password
								.relaxedHTTPSValidation().
							when()
								.get("/read.php").
							then()
								.extract().response();
		
		//to validate response status
		int actualResponseStatus = response.getStatusCode();
		System.out.println("Actual Response Status : " + actualResponseStatus);
		Assert.assertEquals(actualResponseStatus, 200);
		
		//to validate response header content type
		String actualResponseContentType = response.getHeader("Content-Type");
		System.out.println("Actual Response Content Type : " + actualResponseContentType);
		Assert.assertEquals(actualResponseContentType, "application/json; charset=UTF-8");
		
		//to validate the response time
		long actualResponseTime= response.getTimeIn(TimeUnit.MILLISECONDS);
		System.out.println("Actual Response Time : " + actualResponseTime);
		
		if(actualResponseTime <=2000) {
			System.out.println("Response time is within range");
		}
		else {
			System.out.println("Response time is out of range !!!");
		}
		
		//to validate the response body and show the first product ID is not null
		String actualResponseBody = response.getBody().asString();
		System.out.println("Actual Response Body: " + actualResponseBody);
		
		JsonPath jp = new JsonPath(actualResponseBody);
		String firstProductID = jp.get("records[0].id");
		System.out.println("First Product ID : " + firstProductID);
		
		if(firstProductID != null) {
			System.out.println("First Product ID is not null");
		}
		else {
			System.out.println("First Product ID is null !!!");
		}
		
	}
}
