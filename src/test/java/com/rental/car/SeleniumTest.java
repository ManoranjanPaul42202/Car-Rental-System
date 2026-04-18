package com.rental.car;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import io.github.bonigarcia.wdm.WebDriverManager;

import java.time.Duration;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SeleniumTest {

    private WebDriver driver;
    private WebDriverWait wait;

    // 🔹 Setup driver (before each test)
    @BeforeEach
    public void setupDriver() {

        // Automatically download & setup ChromeDriver
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new"); // Required for Jenkins
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--window-size=1920,1080");

        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // 🔹 VALID LOGIN TEST
    @Test
    public void testValidLogin() {

        wait.until(driver -> {
            driver.get("http://localhost:8080/login");
            return driver.getTitle() != null;
        });

        wait.until(ExpectedConditions.presenceOfElementLocated(By.name("username")));

        driver.findElement(By.name("username")).sendKeys("user");
        driver.findElement(By.name("password")).sendKeys("password");
        driver.findElement(By.tagName("button")).click();

        wait.until(ExpectedConditions.urlContains("dashboard"));

        Assertions.assertTrue(driver.getCurrentUrl().contains("dashboard"));
    }

    // 🔹 INVALID LOGIN TEST
    @Test
    public void testInvalidLogin() {

        wait.until(driver -> {
            driver.get("http://localhost:8080/login");
            return driver.getTitle() != null;
        });

        wait.until(ExpectedConditions.presenceOfElementLocated(By.name("username")));

        driver.findElement(By.name("username")).sendKeys("wrong");
        driver.findElement(By.name("password")).sendKeys("wrong");

        wait.until(ExpectedConditions.elementToBeClickable(By.tagName("button"))).click();

        // Wait for error message
        String errorText = wait.until(
                ExpectedConditions.presenceOfElementLocated(By.className("alert"))).getText();

        Assertions.assertTrue(errorText.toLowerCase().contains("invalid"));
    }

    // 🔹 Cleanup driver
    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}