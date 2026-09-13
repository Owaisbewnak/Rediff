package testcasesrediffPortfolio;

import org.testng.annotations.Test;


import testbase.BaseTest;

public class ManageSessionTest extends BaseTest{

	@Test
	public void doLogin() {
		app.logInfo("Login Application");
		
		app.openBrowser("browser_name");
		app.openURL("URL");
		app.click("signIn_id");
		app.type("userName_id", "owaisbewnak@rediffmail.com");
		app.type("password_id", "Owais@1416");
		//app.enterCaptcha("captcha_id");
		app.wait(20);
		app.clickButton("submitBtn_id");
		app.reportAll();
	}
	
	@Test
	public void doLogOut() {
		System.out.println("****** LogOut Application");
	}
}
