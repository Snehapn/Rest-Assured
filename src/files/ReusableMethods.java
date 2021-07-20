package files;

import io.restassured.path.json.JsonPath;

public class ReusableMethods {

	public static JsonPath rawToJson(String response)
	{
	
		JsonPath jsl = new JsonPath(response);
		return jsl;
	}

}
