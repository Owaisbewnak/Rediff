package keywords;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.ProfilesIni;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;
import org.testng.asserts.SoftAssert;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;

import reports.ExtentManager;

public class GenericKeywords {
	public WebDriver driver = null;
	public Properties prop;
	public ExtentTest test;
	public SoftAssert softAssert;

	public void openBrowser(String browserKey) {
		String browserName = prop.getProperty(browserKey);
		logInfo("Opening Browser --" + browserName);
		if (browserName.equalsIgnoreCase("chrome")) {
			System.getProperty("webdriver.chrome.driver",
					System.getProperty("user.dir") + "\\Driver\\chromedriver.exe");
			ChromeOptions options = new ChromeOptions();
			options.addArguments("--start-maximized", "--disable-infobars");
			options.addArguments("--disable-extensions");
			options.addArguments("--incognito");
			options.addArguments("--disable-notifications");
			options.addArguments("ignore-certificate-errors");

			driver = new ChromeDriver(options);
		} else if (browserName.equalsIgnoreCase("firefox")) {
			System.getProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "\\Driver\\geckodriver.exe");
			FirefoxOptions options = new FirefoxOptions();

			ProfilesIni profiles = new ProfilesIni();

			FirefoxProfile ffprofile = profiles.getProfile("TestUser");
			ffprofile.setPreference("dom.webnotifications.enabled", false);
			ffprofile.setAcceptUntrustedCertificates(true);
			ffprofile.setAssumeUntrustedCertificateIssuer(false);

			options.setProfile(ffprofile);

			driver = new FirefoxDriver(options);
		} else if (browserName.equalsIgnoreCase("edge")) {
			System.getProperty("webdriver.chrome.driver", System.getProperty("usr.dir") + "\\Driver\\msedgedriver.exe");
			driver = new EdgeDriver();
		} else {
			System.getProperty("webdriver.chrome.driver", System.getProperty("usr.dir") + "\\Driver\\chromedriver.exe");
			driver = new ChromeDriver();
		}
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(60));
	}

	public void setReport(ExtentTest test) {
		this.test = test;
	}

	public void reportFailure(String msg, boolean isCriticalFailure) {
		logError(msg);
		takeScreenShot();
		softAssert.fail(msg);

		if (isCriticalFailure) {
			Reporter.getCurrentTestResult().getTestContext().setAttribute("isCriticalFailure", "true");
			reportAll();
		}
	}

	public void reportFailure(String msg) {
//		logError(msg);
//		softAssert.fail(msg);
		reportFailure(msg, false);
	}

	public void reportAll() {
		softAssert.assertAll();
	}

	public void takeScreenShot() {
		// File Name of ScreenShot
		Date currentDate = new Date();

		// Format Date and Time
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String formattedDate = dateFormat.format(currentDate);

		String screenshotFile = formattedDate + ".png";

		// take screenshot
		File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

		try {
			FileUtils.copyFile(srcFile, new File(ExtentManager.screenShotPath + '\\' + screenshotFile));

			// put screenshot file in extent reports
			// test.log(Status.INFO, "Screenshot --"
			// + test.addScreenCaptureFromPath(ExtentManager.screenShotPath + '\\' +
			// screenshotFile));

			test.log(Status.FAIL, MarkupHelper.createLabel("Screenshot", ExtentColor.RED));
			test.log(Status.FAIL,
					"<img src='" + ExtentManager.screenShotPath + '/' + screenshotFile + "' style = 'width: 100%;' />");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void openURL(String URLKey) {
		logInfo("Opening Web URL  : " + prop.getProperty(URLKey));
		driver.get(prop.getProperty(URLKey));
	}

	public void click(String locatorKey) {
		logInfo("Performing click on locator: " + prop.getProperty(locatorKey));
		WebElement element = getElement(locatorKey);

		try {
			// 1. Wait until clickable
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			wait.until(ExpectedConditions.elementToBeClickable(element));

			// 2. Scroll to center (prevents bottom scrolling)
			((JavascriptExecutor) driver)
					.executeScript("arguments[0].scrollIntoView({block: 'center', inline: 'nearest'});", element);
			Thread.sleep(200);

			// 3. Try normal click
			element.click();
		} catch (ElementClickInterceptedException e) {
			logError("Normal click intercepted, performing JS click: " + locatorKey);

			((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
		} catch (Exception e) {
			logError("Unable to click: " + locatorKey);
			throw new RuntimeException(e);
		}
	}

	public void clickEnterKey(String locatorKey) {
		logInfo("Pressing Enter Key");
		getElement(locatorKey).sendKeys(Keys.ENTER);
	}

	public void clickButton(String locatorKey) {
		logInfo("Perform click on locatore : " + prop.getProperty(locatorKey));
		getElement(locatorKey).click();
		// driver.findElement(By.id(locator)).click();
	}

	public void type(String locatorKey, String value) {
		logInfo("Typing Text : " + value + " -In Locator : " + prop.getProperty(locatorKey));
		getElement(locatorKey).sendKeys(value);
		// driver.findElement(By.id(locator)).sendKeys(value);
	}

	public void selectByVisibleText(String locatorKey, String value) {
		logInfo("Selecting Portfolio :: " + value);
		Select dropDown = new Select(getElement(locatorKey));
		dropDown.selectByVisibleText(value);
	}

	public void enterCaptcha(String locatorKey) {
		System.out.println("Given captcha Locator : " + prop.getProperty(locatorKey));
		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter Captcha to fill in the text box : ");
		String inputText = scanner.nextLine();

		getElement(locatorKey).sendKeys(inputText);
		// driver.findElement(By.id(locator)).sendKeys(inputText);
	}

	public WebElement getElement(String locatorKey) {

		// Element is present
		if (!isElementPresent(locatorKey)) {
			// Report Error
			System.out.println("Element is not present : " + locatorKey);
		}

		// Element is visible
		if (!isElementVisible(locatorKey)) {
			// Report Error
			System.out.println("Element is not visible : " + locatorKey);
		}

		// Create webElement and Return WebElement
		WebElement element = driver.findElement(getLocator(locatorKey));
		return element;
	}

	public List<WebElement> getElements(String locatorKey) {

		// Create webElement and Return WebElement
		List<WebElement> elements = driver.findElements(getLocator(locatorKey));
		return elements;
	}

	public boolean isElementPresent(String locatorKey) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(50));
		try {
			wait.until(ExpectedConditions.presenceOfElementLocated(getLocator(locatorKey)));
		} catch (Exception e) {
			reportFailure("Unable to locate Element with Locator : " + getLocator(locatorKey));
			reportFailure(e.getMessage());
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean isElementVisible(String locatorKey) {

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(50));
		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(getLocator(locatorKey)));
		} catch (Exception e) {
			reportFailure(e.getMessage());
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public By getLocator(String locatorKey) {
		By by = null;

		if (locatorKey.endsWith("_id")) {
			by = By.id(prop.getProperty(locatorKey));
		} else if (locatorKey.endsWith("_xpath")) {
			by = By.xpath(prop.getProperty(locatorKey));
		} else if (locatorKey.endsWith("_css")) {
			by = By.cssSelector(prop.getProperty(locatorKey));
		} else if (locatorKey.endsWith("_linkText")) {
			by = By.linkText(prop.getProperty(locatorKey));
		} else if (locatorKey.endsWith("_partialLinkText")) {
			by = By.partialLinkText(prop.getProperty(locatorKey));
		} else if (locatorKey.endsWith("_name")) {
			by = By.name(prop.getProperty(locatorKey));
		} else if (locatorKey.endsWith("_className")) {
			by = By.className(prop.getProperty(locatorKey));
		} else if (locatorKey.endsWith("_tagName")) {
			by = By.tagName(prop.getProperty(locatorKey));
		}
		return by;
	}

	public void logInfo(String msg) {
		test.log(Status.INFO, msg);
	}

	public void logError(String msg) {
		test.log(Status.FAIL, msg);
	}

	public void logWarning(String msg) {
		test.log(Status.WARNING, msg);
	}

	public void logSkip(String msg) {
		test.log(Status.SKIP, msg);
	}

	public void clear(String locatorKey) {
		logInfo("Clear the Default Text From : " + locatorKey);
		getElement(locatorKey).clear();
	}

	public void select() {

	}

	public String getText(String locatorKey) {
		logInfo("Getting the Text From : " + locatorKey);
		return getElement(locatorKey).getText();
	}

	public void navigate() {

	}

	public void acceptAlert() {
		logInfo("Accepting the Alert");

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
		wait.until(ExpectedConditions.alertIsPresent());

		driver.switchTo().alert().accept();
		logInfo("Accepted the Alert Successfully");

	}

	public void dismissAlert() {

	}

	public void quitDriver() {
		driver.quit();
	}

	public void waitforWebPageToLoad() {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		int i = 0;

		while (i != 10) {
			String state = (String) js.executeScript("return document.readyState;");
			System.out.println(state);

			if (state.equals("complete"))
				break;
			else
				wait(2);

			i++;
		}

		// check for jQuery status
		i = 0;
		while (i != 10) {
			Long d = (Long) js.executeScript("return jQuery.active;");
			System.out.println(d);
			if (d.longValue() == 0)
				break;
			else
				wait(2);
			i++;
		}
	}

	public void wait(int time) {
		try {
			Thread.sleep(time * 1000);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

}
