package com.udacity.jwdnd.course1.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.junit.jupiter.api.Test;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestCredential {

    @LocalServerPort
    private int port;

    private WebDriver driver;

    @BeforeAll
    static void beforeAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void beforeEach() {
        this.driver = new ChromeDriver();
    }

    @AfterEach
    public void afterEach() {
        if (this.driver != null) {
            driver.quit();
        }
    }

    private void doMockSignUp(String firstName, String lastName, String userName, String password){
        // Create a dummy account for logging in later.

        // Visit the sign-up page.
        WebDriverWait webDriverWait = new WebDriverWait(driver, 10);
        driver.get("http://localhost:" + this.port + "/signup");

        // Fill out credentials
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputFirstName")));
        WebElement inputFirstName = driver.findElement(By.id("inputFirstName"));
        inputFirstName.click();
        inputFirstName.sendKeys(firstName);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputLastName")));
        WebElement inputLastName = driver.findElement(By.id("inputLastName"));
        inputLastName.click();
        inputLastName.sendKeys(lastName);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
        WebElement inputUsername = driver.findElement(By.id("inputUsername"));
        inputUsername.click();
        inputUsername.sendKeys(userName);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
        WebElement inputPassword = driver.findElement(By.id("inputPassword"));
        inputPassword.click();
        inputPassword.sendKeys(password);

        // Attempt to sign up.
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("buttonSignUp")));
        WebElement buttonSignUp = driver.findElement(By.id("buttonSignUp"));
        buttonSignUp.click();
    }

    private void doLogIn(String userName, String password)
    {
        // Log in to our dummy account.
        driver.get("http://localhost:" + this.port + "/login");
        WebDriverWait webDriverWait = new WebDriverWait(driver, 10);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
        WebElement loginUserName = driver.findElement(By.id("inputUsername"));
        loginUserName.click();
        loginUserName.sendKeys(userName);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
        WebElement loginPassword = driver.findElement(By.id("inputPassword"));
        loginPassword.click();
        loginPassword.sendKeys(password);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login-button")));
        WebElement loginButton = driver.findElement(By.id("login-button"));
        loginButton.click();

        webDriverWait.until(ExpectedConditions.titleContains("Home"));
    }

    public void addCredential() {
        //signs up a new user
        doMockSignUp("URL","Test","UT","123");

        // logs in an existing user
        doLogIn("UT", "123");

        WebDriverWait webDriverWait = new WebDriverWait(driver, 5);

        //Open credential tab
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab")));
        WebElement credentialTab = driver.findElement(By.id("nav-credentials-tab"));
        credentialTab.click();

        //Open credential modal
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-button")));
        WebElement noteButton = driver.findElement(By.id("credential-button"));
        noteButton.click();

        //creates a credential
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-url")));
        WebElement credentialUrl = driver.findElement(By.id("credential-url"));
        credentialUrl.sendKeys("credential-url");

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-username")));
        WebElement credentialUsername = driver.findElement(By.id("credential-username"));
        credentialUsername.sendKeys("credential-username");

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-password")));
        WebElement credentialPassword = driver.findElement(By.id("credential-password"));
        credentialPassword.sendKeys("credential-password");

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialSaveChanges")));
        WebElement credentialSaveChanges = driver.findElement(By.id("credentialSaveChanges"));
        credentialSaveChanges.click();

        //verifies that the home page is no longer accessible.
        Assertions.assertNotEquals("http://localhost:" + this.port + "/add-update-credential", driver.getCurrentUrl());

        //verifies that the home page is no longer accessible.back-home
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("back-home")));
        WebElement backHome = driver.findElement(By.id("back-home"));
        backHome.click();

        //Open credential tab
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab")));
        WebElement credentialsTabAgain = driver.findElement(By.id("nav-credentials-tab"));
        credentialsTabAgain.click();
    }

    @Test
    @Order(1)
    public void testAddCredential() {

        //add Credential
        addCredential();

        WebDriverWait webDriverWait = new WebDriverWait(driver, 5);

        //select record add new note
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialTable")));
        WebElement table = driver.findElement(By.id("credentialTable"));
        List<WebElement> rows = table.findElements(By.tagName("tr"));
        WebElement firstRow = rows.get(1);
        List<WebElement> cells = firstRow.findElements(By.tagName("td"));

        //verifies that the credential details are visible in the credential list.
        Assertions.assertEquals("credential-username", cells.get(1).getText());
    }

    @Test
    @Order(2)
    public void testUpdateCredential() {
        //login
        doLogIn("UT", "123");

        WebDriverWait webDriverWait = new WebDriverWait(driver, 5);

        //Open credential tab
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab")));
        WebElement credentialTab = driver.findElement(By.id("nav-credentials-tab"));
        credentialTab.click();

        //select record add new note and open modal update
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialTable")));
        WebElement table = driver.findElement(By.id("credentialTable"));
        WebElement cell = table.findElement(By.xpath("//tr[1]/td[1]"));
        System.out.println(cell.getText());
        WebElement editButton = cell.findElement(By.className("editCredentialButton"));
        editButton.click();

        //changes the credential data
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-url")));
        WebElement credentialUrl = driver.findElement(By.id("credential-url"));
        credentialUrl.sendKeys("-update");

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-username")));
        WebElement credentialUsername = driver.findElement(By.id("credential-username"));
        credentialUsername.sendKeys("-update");

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-password")));
        WebElement credentialPassword = driver.findElement(By.id("credential-password"));
        credentialPassword.sendKeys("-update");

        //saves the changes
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialSaveChanges")));
        WebElement credentialSaveChanges = driver.findElement(By.id("credentialSaveChanges"));
        credentialSaveChanges.click();

        //verifies that the home page is no longer accessible.
        Assertions.assertNotEquals("http://localhost:" + this.port + "/add-update-credential", driver.getCurrentUrl());

        //verifies that the home page is no longer accessible.back-home
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("back-home")));
        WebElement backHome = driver.findElement(By.id("back-home"));
        backHome.click();

        //Open note tab
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab")));
        WebElement credentialTabAgain = driver.findElement(By.id("nav-credentials-tab"));
        credentialTabAgain.click();

        //select record update note
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialTable")));
        WebElement tableAgain = driver.findElement(By.id("credentialTable"));
        List<WebElement> rows = tableAgain.findElements(By.tagName("tr"));
        WebElement firstRow = rows.get(1);
        List<WebElement> cells = firstRow.findElements(By.tagName("td"));

        // verifies that the changes appear in the credential list.
        Assertions.assertEquals("credential-username-update", cells.get(1).getText());

    }

    @Test
    @Order(3)
    public void testDeleteCredential() {
        //login
        doLogIn("UT", "123");

        WebDriverWait webDriverWait = new WebDriverWait(driver, 5);

        //Open credential tab
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab")));
        WebElement credentialTab = driver.findElement(By.id("nav-credentials-tab"));
        credentialTab.click();

        //select record Credential
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialTable")));
        WebElement table = driver.findElement(By.id("credentialTable"));
        WebElement cell = table.findElement(By.xpath("//tr[1]/td[1]"));
        System.out.println(cell.getText());

        //clicks the delete credential button on an existing credential
        WebElement deleteButton = cell.findElement(By.className("deleteCredentialButton"));
        deleteButton.click();

        //verifies that the home page is no longer accessible.
        Assertions.assertNotEquals("http://localhost:" + this.port + "/add-update-note", driver.getCurrentUrl());

        //verifies that the home page is no longer accessible.
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("back-home")));
        WebElement backHome = driver.findElement(By.id("back-home"));
        backHome.click();

        //Open note tab
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab")));
        WebElement credentialTabAgain = driver.findElement(By.id("nav-credentials-tab"));
        credentialTabAgain.click();

        //select record update note
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialTable")));
        WebElement tableUpdate = driver.findElement(By.id("credentialTable"));
        List<WebElement> rows = tableUpdate.findElements(By.tagName("tr"));

        //verifies that the credential no longer appears in the credential list.
        Assertions.assertEquals(1, rows.size());
    }

}
