package com.rental.car;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.io.IOException;
import java.time.Duration;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SeleniumTest {

    private Process process;
    private WebDriver driver;
    private WebDriverWait wait;

    // 🔹 Start Spring Boot App (like setup_server)
    // @BeforeAll
    // public void startServer() throws IOException, InterruptedException {
    //     process = new ProcessBuilder("java", "-jar", "target/car-0.0.1-SNAPSHOT.jar")
    //             .start();

    //     Thread.sleep(10000); // wait for app to start
    // }

    // 🔹 Setup driver (like pytest fixture)
    @BeforeEach
    public void setupDriver() {

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--window-size=1920,1080");

        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // 🔹 VALID LOGIN TEST
    @Test
    public void testValidLogin() {

        driver.get("http://localhost:8081/login");

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

        driver.get("http://localhost:8081/login");

        wait.until(ExpectedConditions.presenceOfElementLocated(By.name("username")));

        driver.findElement(By.name("username")).sendKeys("wrong");
        driver.findElement(By.name("password")).sendKeys("wrong");

        driver.findElement(By.tagName("button")).click();

        // Wait for error message (adjust class if needed)
        String errorText = wait.until(
                ExpectedConditions.presenceOfElementLocated(By.className("alert"))).getText();

        Assertions.assertTrue(errorText.toLowerCase().contains("invalid"));
    }

    // 🔹 Cleanup driver
    @AfterEach
    public void tearDown() {
        driver.quit();
    }

    // 🔹 Stop server
    @AfterAll
    public void stopServer() {
        process.destroy();
    }
}