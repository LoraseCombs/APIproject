package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import static io.restassured.RestAssured.*;

import java.io.File;
import java.util.concurrent.TimeUnit;

import util.ConfigReader;

public class Authentication {
	String baseURI;
	String authEndPoint;
	String authBodyFilePath;
	String headerContentType;
	static long responseTime;
	public static String bearerToken;
	
	public Authentication() {
		baseURI=ConfigReader.getProperty("baseURI");
		authEndPoint=ConfigReader.getProperty("authEndPoint");
		authBodyFilePath="src\\main\\java\\data\\authBody.json";
		headerContentType=ConfigReader.getProperty("contentType");
		
	}
	
	public boolean compareResponseTime() {
		boolean withinRange = false;
		if(responseTime <= 3000) {
			System.out.println("Response Time is within range");
			withinRange =true;
		}else {
			System.out.println("Response Time is NOT within range");
			withinRange =false;
		}
		
		return withinRange;
		
	}
	
	
	public String generateBearerToken() {
		/*
		 * given: all input details-> (baseURI, Headers, Authorization, Payload/Body, QueryParameters)
		 * when: submit api requests-> HttpMethod(Endpoint/Resource)
		 * then: validate response-> (status code, Headers, responseTime, Payload/Body)
		 */
		
		Response response =
		
		given()
		.baseUri(baseURI)
		.header("Content-Type",headerContentType)
		.body(new File(authBodyFilePath)).
		when()
		.post(authEndPoint).
		then()
//		.log().all()
		.extract().response();
		
		int statusCode = response.getStatusCode();
		System.out.println("Status Code:"+ statusCode);
		Assert.assertEquals(statusCode, 201, "Status codes are not matching!!!");
		
		
		long responseTime = response.getTimeIn(TimeUnit.MILLISECONDS);
		System.out.println("Response Time:"+ responseTime);
		Assert.assertEquals(compareResponseTime(), true);
		
//		String contentType = response.getHeader("Content-Type");
		String contentType = response.getContentType();
		System.out.println("Response Content type:" + contentType);
		Assert.assertEquals(contentType, headerContentType, "Content Types Do Not Match!");
		
		String responseBody = response.getBody().asString();
		System.out.println("Response Body:" + responseBody);
		
		JsonPath jp = new JsonPath(responseBody);
		bearerToken = jp.get("access_token");
		
		return bearerToken;
		
	}

}
