package listner;

import org.testng.ITestListener;
import org.testng.ITestResult;
import com.aventstack.extentreports.ExtentTest;

public class MyTestNGListener implements ITestListener {

    @Override
    public void onTestFailure(ITestResult result) {
        ExtentTest test = (ExtentTest) result.getTestContext().getAttribute("extentTest");
        if (test != null) {
            test.fail(result.getThrowable().getMessage());
        } else {
            System.out.println("Test Failed (ExtentTest was null): " + result.getName());
        }
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        ExtentTest test = (ExtentTest) result.getTestContext().getAttribute("extentTest");
        if (test != null) {
            test.pass("Test Success : " + result.getName());
        } else {
            System.out.println("Test Passed (ExtentTest was null): " + result.getName());
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        ExtentTest test = (ExtentTest) result.getTestContext().getAttribute("extentTest");
        if (test != null) {
            test.skip(result.getName() + " : Test Skipped Due to Critical Error in Previous Test");
        } else {
            System.out.println("Test Skipped (ExtentTest was null): " + result.getName());
        }
    }
}