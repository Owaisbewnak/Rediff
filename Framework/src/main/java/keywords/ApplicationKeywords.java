package keywords;

import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.asserts.SoftAssert;

public class ApplicationKeywords extends ValidationKeywords {

	public ApplicationKeywords() {
		prop = new Properties();
		try {
			FileInputStream fs = new FileInputStream(
					System.getProperty("user.dir") + "\\src\\test\\resources\\Project.properties");
			prop.load(fs);
		} catch (Exception e) {
			e.printStackTrace();
		}

		softAssert = new SoftAssert();
	}
	
	public void goToBuySell(String companyName) throws InterruptedException {
		int rowNum = getRowNumWithCellData("stockTable_id", companyName);
		
		if (rowNum == -1) {
			logError("Stock is not Present in list");
		}
		
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		//driver.findElement(By.cssSelector("table#stock > tbody > tr:nth-child(" + rowNum + ") input.radio")).click();
		WebElement radioBtn = driver.findElement(
		        By.cssSelector("table#stock > tbody > tr:nth-child(" + rowNum + ") input.radio")
		);

		// scroll to the element
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", radioBtn);

		// small wait (optional but helps in dynamic pages)
		Thread.sleep(300);

		// click the radio button
		radioBtn.click();
		
		driver.findElement(By.cssSelector("table#stock > tbody > tr:nth-child(" + rowNum + ") input.buySell")).click();
		waitforWebPageToLoad();
	}

	public void selectDateFromCalender(String date) {
		logInfo("Selecting the Date :: " + date);

		try {
			Date currentDate = new Date(); // dd-MM-yyyy
			SimpleDateFormat dFormat = new SimpleDateFormat("dd-MM-yyyy");
			Date dateToSelect = dFormat.parse(date);

			String day = new SimpleDateFormat("d").format(dateToSelect);
			String month = new SimpleDateFormat("MMMM").format(dateToSelect);
			String year = new SimpleDateFormat("yyyy").format(dateToSelect);

			String monthYearToBeSelected = month + " " + year;

			String monthYearDisplayed = getElement("monthyear_css").getText();

			while (!monthYearToBeSelected.equals(monthYearDisplayed)) {
				click("datebackButton_xpath");
				monthYearDisplayed = getElement("monthyear_css").getText();
			}

			driver.findElement(By.xpath("//td[text()='" + day + "']")).click();

		} catch (Exception e) {
			logError(e.getMessage());
			e.printStackTrace();
		}
	}

	public int  findCurrentStockQuantity(String companyName) {
		int rowNum = getRowNumWithCellData("stockTable_id", companyName);
		
		if (rowNum == -1) {
			logError("Stock Quantity is 0 as given Stock - " + companyName + "is not present in Stock List");
			return 0;
		}
		String quantity = driver.findElement(By.cssSelector("table#stock > tbody > tr:nth-child(" + rowNum + ") > td:nth-child(4)")).getText();
		logInfo(companyName + " :: Stock quantity is -- " + quantity);
		
		return Integer.parseInt(quantity);
	}
	
	public void openTransactionHistory(String companyName) {
		int rowNum = getRowNumWithCellData("stockTable_id", companyName);
		
		if (rowNum == -1) {
			logError("Stock is not Present in list");
		}
		driver.findElement(By.cssSelector("table#stock > tbody > tr:nth-child(" + rowNum + ") input.radio")).click();
		driver.findElement(By.cssSelector("table#stock > tbody > tr:nth-child(" + rowNum + ") input.equityTransaction")).click();
		waitforWebPageToLoad();
	}
	
	public int getRowNumWithCellData(String locatorKey, String data) {

		WebElement table = getElement(locatorKey);
		List<WebElement> rows = table.findElements(By.tagName("tr"));

		for (int rNum = 0; rNum < rows.size(); rNum++) {
			WebElement row = rows.get(rNum);
			List<WebElement> cells = row.findElements(By.tagName("td"));
			for (int cNum = 0; cNum < cells.size(); cNum++) {
				WebElement cell = cells.get(cNum);
				if (!cell.getText().trim().equals(""))
					System.out.println("Cell Data :: " + cell.getText());
				if (cell.getText().contains(data))
					return (rNum);
			}
		}
		return -1; // data is not found
	}

	public void login() {

	}

	public void selectDate() {

	}
}
