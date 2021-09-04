import static io.restassured.RestAssured.given;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;

import io.restassured.parsing.Parser;
import io.restassured.path.json.JsonPath;
import pojo.Api;
import pojo.GetCourse;
import pojo.WebAutomation;


public class OAuthTest {

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		
		String[] courseTitles={"Selenium Webdriver Java","Cypress","Protractor"};
	//	System.setProperty("webdriver.chrome.driver", "C:\\Users\\HP\\Downloads\\chromedriver.exe");
	//	WebDriver driver = new ChromeDriver();
	//	driver.get("https://accounts.google.com/o/oauth2/v2/auth?scope=https://www.googleapis.com/auth/userinfo.email&auth_url=https://accounts.google.com/o/oauth2/v2/auth&client_id=692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com&response_type=code&redirect_uri=https://rahulshettyacademy.com/getCourse.php");
	//	driver.findElement(By.cssSelector("//input[type='email']")).sendKeys("natekar.sneha18@gmail.com");
	//	driver.findElement(By.cssSelector("//input[type='email']")).sendKeys(Keys.ENTER);
	//	Thread.sleep(3000);
	//	driver.findElement(By.cssSelector("//input[type='password']")).sendKeys("Pramod@18");
	//	driver.findElement(By.cssSelector("//input[type='password']")).sendKeys(Keys.ENTER);
	//	Thread.sleep(4000);
		
	//	String url = driver.getCurrentUrl();
		String url ="https://rahulshettyacademy.com/getCourse.php?code=4%2F0AX4XfWgsbKn6azLDDwXFiC6Igfa2iDd5JHTB2erZMRt53o4DlFeBwlVORb2L7p5Q9QRy2Q&scope=email+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email+openid&authuser=0&prompt=none#";
		String partialCode = url.split("code=")[1];
		String code = partialCode.split("&scope")[0];
		System.out.println(code);
		
		String accessTokenResponse = 
		 given().urlEncodingEnabled(false)
		.queryParam("code", code)
		.queryParam("client_id", "692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com")
		.queryParam("client_secret", "erZOWM9g3UtwNRj340YYaK_W")
		.queryParam("redirect_uri", "https://rahulshettyacademy.com/getCourse.php")
		.queryParam("grant_type", "authorization_code")
		.when().log().all()
		.post("https://www.googleapis.com/oauth2/v4/token").asString();
		
		JsonPath js = new JsonPath(accessTokenResponse);
		String accessToken = js.getString("access_token");
		
		
		
		GetCourse gc = given().queryParam("access_token", accessToken).expect().defaultParser(Parser.JSON)
		.when()
	//	.get("https://rahulshettyacademy.com/getCourse.php").asString();
		.get("https://rahulshettyacademy.com/getCourse.php").as(GetCourse.class);
		
	//	System.out.println(response);
		System.out.println(gc.getLinkedIn());
		System.out.println(gc.getInstructor());
	
	//	System.out.println(gc.getCourses().getApi().get(1).getCourseTitle());
		
	// get course proce where course title is soapUI
		List<Api> apiCourses = gc.getCourses().getApi();
		for(int i=0; i<apiCourses.size(); i++)
		{
			if(apiCourses.get(i).getCourseTitle().equalsIgnoreCase("SoapUI Webservices testing"))
			{
				System.out.println(apiCourses.get(i).getPrice());
			}
				
		}
		
		//get all course title of web automation
		ArrayList<String> a = new ArrayList<String>();
		List<WebAutomation> webAutomationCourses = gc.getCourses().getWebAutomation();
		System.out.println(webAutomationCourses.size());
		for (int j=0; j<webAutomationCourses.size(); j++)
		{
		//	System.out.println(webAutomationCourses.get(j).getCourseTitle());
			a.add(webAutomationCourses.get(j).getCourseTitle());
		}
		List<String> expectedList = Arrays.asList(courseTitles);
		System.out.println(expectedList);
		Assert.assertTrue(a.equals(expectedList));
	}
	
	

}
