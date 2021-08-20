import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.testng.Assert;

import files.ReusableMethods;
import files.payload;

public class Basics {

	public static void main(String[] args) throws IOException {

		// validate if Add Place API is working as expected

		// given - all input details
		// when - submit the API
		// Then - validate the response 

		// Add place -> update place with new address -> Get place to validate if new
		// address is present in response

		RestAssured.baseURI = "https://rahulshettyacademy.com/";
		String response = given().log().all().queryParam("key", "qaclick123").header("Content-Type", "application/json")
				.body(new String (Files.readAllBytes(Paths.get("D:\\Projects\\Rest-Assured\\AddPlace.json")))).when().post("maps/api/place/add/json").then().assertThat().statusCode(200)
				.body("scope", equalTo("APP")).header("Server", "Apache/2.4.18 (Ubuntu)").extract().response()
				.asString();

		/*
		 * RestAssured.baseURI = "https://rahulshettyacademy.com/"; String response =
		 * given().log().all().queryParam("key", "qaclick123").header("Content-Type",
		 * "application/json")
		 * .body(payload.AddPlace()).when().post("maps/api/place/add/json").then().
		 * assertThat().statusCode(200) .body("scope", equalTo("APP")).header("Server",
		 * "Apache/2.4.18 (Ubuntu)").extract().response() .asString();
		 */

		System.out.println(response);

		JsonPath js = new JsonPath(response);// for parsing json
		String placeId = js.getString("place_id");
		System.out.println(placeId);

		// Update place
		String newAddress = "70 Summer walk, USA";
		given().log().all().queryParam("key", "qaclick123").header("Content-Type", "application/json")
				.body("{\r\n" + "\"place_id\":\"" + placeId + "\",\r\n" + "\"address\":\"" + newAddress + "\",\r\n"
						+ "\"key\":\"qaclick123\"\r\n" + "}\r\n" + "")
				.when().put("maps/api/place/update/json").then().assertThat().log().all().statusCode(200)
				.body("msg", equalTo("Address successfully updated"));

		// Get place
		String getPlaceResponse = given().log().all().queryParam("key", "qaclick123").queryParam("place_id", placeId)
				.when().get("maps/api/place/get/json").then().assertThat().log().all().statusCode(200).extract()
				.response().asString();

		// JsonPath jsl = new JsonPath(getPlaceResponse);
		JsonPath jsl = ReusableMethods.rawToJson(getPlaceResponse);
		String actualAddress = jsl.getString("address");
		System.out.println(actualAddress);

		Assert.assertEquals(newAddress, actualAddress);

	}

}
