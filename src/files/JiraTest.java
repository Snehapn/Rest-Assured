package files;

import static io.restassured.RestAssured.*;

import java.io.File;

import org.testng.Assert;

import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
import io.restassured.path.json.JsonPath;

public class JiraTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		RestAssured.baseURI = "http://localhost:8080";

		SessionFilter session = new SessionFilter();
		// Login
		String response = given().relaxedHTTPSValidation().log().all().header("Content-Type", "application/json")
				.body("{\r\n" + "    \"username\": \"Snehapn\",\r\n" + "    \"password\": \"Sneha@18\"\r\n" + "}").log()
				.all().filter(session).when().post("/rest/auth/1/session").then().log().all().extract().response().asString();

		String expectedMessage = "Hi, How are you?";
		// Add comment
		String addCommentResponse = given().pathParam("issueIdOrKey", 10100).log().all().header("Content-Type", "application/json")
				.body("{\r\n" + "    \"body\": \""+expectedMessage+"\",\r\n" + "    \"visibility\": {\r\n"
						+ "        \"type\": \"role\",\r\n" + "        \"value\": \"Administrators\"\r\n" + "    }\r\n"
						+ "}")
				.filter(session).when().post("/rest/api/2/issue/{issueIdOrKey}/comment")
				.then().log().all().assertThat().statusCode(201).extract().response().asString();
		
		JsonPath js = new JsonPath(addCommentResponse);
		String commentID = js.get("id");

		// Add attachment
		given().header("X-Atlassian-Token","no-check").filter(session).pathParam("issueIdOrKey", 10100)
		.header("Content-Type", "multipart/form-data")
		.multiPart("file", new File("jira.txt"))
		.when().post("/rest/api/2/issue/{issueIdOrKey}/attachment")
		.then().log().all().assertThat().statusCode(200);
		
		
		// Get issue
		String issueDeatils = given().filter(session).pathParam("issueIdOrKey", 10100)
		.queryParam("fields", "comment").log().all()
		.when().get("/rest/api/2/issue/{issueIdOrKey}")
		.then().log().all().extract().response().asString();	
		System.out.println(issueDeatils);
	
		JsonPath js1 = new JsonPath(issueDeatils);
		int commentsCount = js1.getInt("fields.comment.comments.size()");
	
		for(int i=0;i<commentsCount;i++) 
		{
			String commentIdIssue = js1.get("fields.comment.comments.size("+i+").id").toString();
			if (commentIdIssue.equalsIgnoreCase(commentID)) 
			{
				String message = js1.get("fields.comment.comments.size("+i+").body").toString();
				System.out.println(message);
				Assert.assertEquals(message, expectedMessage);
			}
		}
	
	
	}

}
