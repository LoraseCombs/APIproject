package testCases;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;

import java.io.File;

import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import util.ConfigReader;

public class CreateAccount extends Authentication{
	
	String createAccountEndPointFromConfig;
	String createAccountBodyFilePath;
	
	public CreateAccount() {
		
		createAccountEndPointFromConfig = ConfigReader.getProperty("createAccountEndPoint");
		createAccountBodyFilePath = "src\\main\\java\\data\\createAccountBody.json";
		
	}
	
	@Test
	public void createAccount() {
		/*
		 * given: all input details-> (baseURI, Headers, Authorization, Payload/Body, QueryParameters)
		 * when: submit api requests-> HttpMethod(Endpoint/Resource)
		 * then: validate response-> (status code, Headers, responseTime, Payload/Body)
		 */
		
		Response response =
				given().baseUri(baseURI)
				.header("Content-Type",headerContentType)
				.header("Authorization", "Bearer " + generateBearerToken())
				.body(new File (createAccountBodyFilePath)).
				when()
				.post(createAccountEndPointFromConfig).
				then()
				.log().all()
				.extract().response();
		
		int statusCode = response.getStatusCode();
		System.out.println("Status Code:"+ statusCode);
		Assert.assertEquals(statusCode, 200, "Status codes are not matching!!!");
		
		String contentType = response.getContentType();
		System.out.println("Response Content type:" + contentType);
		Assert.assertEquals(contentType, headerContentType, "Content Types Do Not Match!");
		
		String responseBody = response.getBody().asString();
		System.out.println("Response Body:" + responseBody);
		
		JsonPath jp = new JsonPath(responseBody);
		
		String successMessage = jp.getString("message");
		System.out.println("Success Message:" + successMessage);
		Assert.assertEquals("Account created", "");
		
		
	}

}
