import files.payload;
import io.restassured.path.json.JsonPath;

public class ComplexJsonParse {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		JsonPath js = new JsonPath(payload.CoursePrice());
		int count  = js.getInt("courses.size()");
		System.out.println(count);
		
		int totalAmount = js.getInt("dashboard.purchaseAmount");
		System.out.println(totalAmount);
		
		String courseTitle = js.getString("courses[0].title");
		System.out.println(courseTitle);
		

		for (int i=0; i<count; i++) {
			String TitleOfAllCourses = js.getString("courses["+i+"].title");
			System.out.println(js.getString("courses["+i+"].price").toString()); 
			System.out.println(TitleOfAllCourses);

		}
		
		for (int i=0; i<count; i++) {
			String TitleOfAllCourses = js.getString("courses["+i+"].title");
			if(TitleOfAllCourses.equalsIgnoreCase("RPA")) {
				int copies = js.get("courses["+i+"].copies");
				System.out.println(copies);
				break;
			}

		}
	}

}
