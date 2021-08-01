package files;

import static io.restassured.RestAssured.*;

import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;

public class JiraTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		RestAssured.baseURI = "http://localhost:8080";

		SessionFilter session = new SessionFilter();
		// Login
		String response = given().log().all().header("Content-Type", "application/json")
				.body("{\r\n" + "    \"username\": \"Snehapn\",\r\n" + "    \"password\": \"Sneha@18\"\r\n" + "}").log()
				.all().filter(session).when().post("/rest/auth/1/session").then().log().all().extract().response().asString();

		// Add comment
		given().pathParam("issueIdOrKey", 10100).log().all().header("Content-Type", "application/json")
				.body("{\r\n" + "    \"body\": \"New comment\",\r\n" + "    \"visibility\": {\r\n"
						+ "        \"type\": \"role\",\r\n" + "        \"value\": \"Administrators\"\r\n" + "    }\r\n"
						+ "}")
				.filter(session).when().post("/rest/api/2/issue/{issueIdOrKey}/comment")
				.then().log().all().assertThat().statusCode(201);

	}

}
