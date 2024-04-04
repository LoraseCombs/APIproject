package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.*;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import util.ConfigReader;

public class GetAccountDetails extends Authentication{
	String getAccountDetailsEndPointFromConfig;
	
	public GetAccountDetails() {
		getAccountDetailsEndPointFromConfig = ConfigReader.getProperty("getAccountDetailsEndPoint");
	}
	
	@Test
	public void getAccountDetails() {
		
		/*
		 * given: all input details-> (baseURI, Headers, Authorization, Payload/Body, QueryParameters)
		 * when: submit api requests-> HttpMethod(Endpoint/Resource)
		 * then: validate response-> (status code, Headers, responseTime, Payload/Body)
		 */
		
		Response response =
			given().baseUri(baseURI)
			.header("Content-Type",headerContentType)
			.auth().preemptive().basic("demo1@codefios.com", "abc123")
			.queryParam("account_id", "691").
			when()
			.get(getAccountDetailsEndPointFromConfig).
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
			String accountName = jp.getString("account_name");
			System.out.println("Account Name:" + accountName);
			Assert.assertEquals(accountName, "MD Techfios account 99999", "Account Names are Not matching");
		
//			if (accountName != null) {
//				System.out.println("Account Name is Not null");
//			}else {
//				System.out.println("Account Name is null");
//			}
		
	}

}
