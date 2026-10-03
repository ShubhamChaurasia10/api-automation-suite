package com.reservationhub.listeners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import java.io.File;

public class ExtentReportListener implements ITestListener {

    private static ExtentReports extent;
    private static final ThreadLocal<ExtentTest> testNode = new ThreadLocal<>();

    @Override
    public void onStart(ITestContext context) {
        String reportDir = System.getProperty("user.dir") + File.separator + "reports";
        new File(reportDir).mkdirs();

        ExtentSparkReporter spark = new ExtentSparkReporter(reportDir + File.separator + "TestExecutionReport.html");
        spark.config().setReportName("Reservation Hub API Automation Summary");
        spark.config().setDocumentTitle("API Test Results");

        extent = new ExtentReports();
        extent.attachReporter(spark);
        extent.setSystemInfo("Environment", "Sandbox");
        extent.setSystemInfo("Framework", "REST Assured + TestNG");
    }

    @Override
    public void onTestStart(ITestResult result) {
        ExtentTest test = extent.createTest(result.getMethod().getMethodName(), result.getMethod().getDescription());

        // Assign TestNG groups as categories in Extent Report for easy navigation
        for (String group : result.getMethod().getGroups()) {
            test.assignCategory(group);
        }

        testNode.set(test);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        testNode.get().log(Status.PASS, "Test Executed Successfully");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        ExtentTest test = testNode.get();
        test.log(Status.FAIL, "<b>Test Status:</b> FAILED");

        if (result.getThrowable() != null) {
            test.log(Status.FAIL, "<b>Failure Cause:</b> " + result.getThrowable().getMessage());
            test.fail(result.getThrowable());
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        testNode.get().log(Status.SKIP, "Test Skipped: " + result.getThrowable().getMessage());
    }

    @Override
    public void onFinish(ITestContext context) {
        if (extent != null) {
            extent.flush();
        }
    }
}