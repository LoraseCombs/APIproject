package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.*;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import util.ConfigReader;

public class GetAllAccounts extends Authentication{
	String getAllAccountsEndPointFromConfig;
	String firstAccountId;
	
	public GetAllAccounts() {
		getAllAccountsEndPointFromConfig = ConfigReader.getProperty("getAllAccountsEndPoint");
	}
	
	@Test
	public void getAllAccounts() {
		
		/*
		 * given: all input details-> (baseURI, Headers, Authorization, Payload/Body, QueryParameters)
		 * when: submit api requests-> HttpMethod(Endpoint/Resource)
		 * then: validate response-> (status code, Headers, responseTime, Payload/Body)
		 */
		
		Response response =
			given().baseUri(baseURI)
			.header("Content-Type",headerContentType)
			.header("Authorization", "Bearer " + generateBearerToken()).
			when()
			.get(getAllAccountsEndPointFromConfig).
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
			firstAccountId = jp.getString("records[0].account_id");
			System.out.println("First account id:" + firstAccountId);
		
			if (firstAccountId != null) {
				System.out.println("First account ID is Not null");
			}else {
				System.out.println("First account ID is null");
			}
		
	}

}
