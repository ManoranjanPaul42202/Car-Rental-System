package com.rental.car;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import io.github.bonigarcia.wdm.WebDriverManager;

import java.time.Duration;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SeleniumTest {

    private WebDriver driver;
    private WebDriverWait wait;

    // 🔹 Setup driver
    @BeforeEach
    public void setupDriver() {

        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new"); // Jenkins compatible
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--window-size=1920,1080");

        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    // 🔹 VALID LOGIN TEST
    @Test
    public void testValidLogin() {

        driver.get("http://127.0.0.1:8080/login");

        wait.until(ExpectedConditions.presenceOfElementLocated(By.name("username")));

        // 👉 Use actual credentials from DB
        driver.findElement(By.name("username")).sendKeys("Mano");
        driver.findElement(By.name("password")).sendKeys("Mano@123");

        // 🔥 Scroll to button
        ((JavascriptExecutor) driver)
                .executeScript("window.scrollTo(0, document.body.scrollHeight)");

        // 🔥 JS Click (fixes intercept issue)
        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].click();",
                        driver.findElement(By.tagName("button")));

        // Wait for dashboard redirect
        wait.until(ExpectedConditions.urlContains("dashboard"));

        Assertions.assertTrue(driver.getCurrentUrl().contains("dashboard"));
    }

    // 🔹 INVALID LOGIN TEST
    @Test
    public void testInvalidLogin() {

        driver.get("http://127.0.0.1:8080/login");

        wait.until(ExpectedConditions.presenceOfElementLocated(By.name("username")));

        driver.findElement(By.name("username")).sendKeys("wrong");
        driver.findElement(By.name("password")).sendKeys("wrong");

        // 🔥 JS Click
        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].click();",
                        driver.findElement(By.tagName("button")));

        // ✅ Check URL instead of alert
        wait.until(ExpectedConditions.urlContains("error"));

        Assertions.assertTrue(driver.getCurrentUrl().contains("error"));
    }

    // 🔹 Cleanup
    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}