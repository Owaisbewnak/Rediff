package testcasesrediffPortfolio;


import org.json.simple.JSONObject;
import org.testng.ITestContext;
import org.testng.annotations.Test;

import testbase.BaseTest;

public class ManageportfolioTest extends BaseTest{
	
	@Test
	public void createPortfolio(ITestContext context) {
		JSONObject data = (JSONObject) context.getAttribute("testData");
		String portfolioName = (String) data.get("portfolioname");
		
		app.logInfo("Creating Portfolio :: " + portfolioName );
		app.click("createPortfolio_id");
		app.clear("portfolioname_id");
		app.type("portfolioname_id", portfolioName);
		app.click("createPortfolioButton_id");
		app.waitforWebPageToLoad();
		app.validateSelectedValueInDropDown("portfolio_dropdown_id", portfolioName);
		
	}
	
	@Test
	public void deleteportfolio(ITestContext context) {
		JSONObject data = (JSONObject) context.getAttribute("testData");
		String portfolioName = (String) data.get("portfolioname");
		
		app.logInfo("Deleting Portfolio :: " + portfolioName);
		app.selectByVisibleText("portfolio_dropdown_id", portfolioName);
		app.waitforWebPageToLoad();
		app.click("deletePortfolio_id");
		app.acceptAlert();
		app.waitforWebPageToLoad();
		app.validateSelectedValueNotInDropDown("portfolio_dropdown_id", portfolioName);
	}
	
	@Test
	public void selectPortfolio(ITestContext context) {
		
		String portfolioName = "Portfolio50";
		
		app.logInfo("Selecting Portfolio :: " + portfolioName);
		app.selectByVisibleText("portfolio_dropdown_id", portfolioName);
		app.waitforWebPageToLoad();
	}

}
