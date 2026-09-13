package testCases;

import org.testng.annotations.Test;

import keywords.ApplicationKeywords;

public class CreatePortfolioTest extends ApplicationKeywords {

	@Test
	public void createPortfolioTest() {

		/*
		 * 1. Open Targeted WebPage 
		 * 2. Click on Sign in Button 
		 * 3. Enter Login Details 
		 * 4. Click on submit button 
		 * 5. Verify you are on Portfolio Page After login 
		 * 6. click create portfolio link 
		 * 7. Enter the portfolio name 
		 * 8. click on createportfolio link
		 */

		ApplicationKeywords app = new ApplicationKeywords();
		
		app.openBrowser("browser_name");
		app.openURL("URL");
		app.click("signIn_id");
		app.type("userName_id", "owaisbewnak@rediffmail.com");
		app.type("password_id", "Owais@1416");
		app.enterCaptcha("captcha_id");
		app.clickButton("submitBtn_id");
		app.quitDriver();
		

	}
}
