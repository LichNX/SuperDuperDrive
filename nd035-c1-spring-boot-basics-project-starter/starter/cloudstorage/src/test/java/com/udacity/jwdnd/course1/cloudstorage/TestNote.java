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
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestNote {

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

    public void AddNote() {
        //signs up a new user
        doMockSignUp("URL","Test","UT","123");

        // logs in an existing user
        doLogIn("UT", "123");

        WebDriverWait webDriverWait = new WebDriverWait(driver, 10);

        //Open note tab
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));
        WebElement noteTab = driver.findElement(By.id("nav-notes-tab"));
        noteTab.click();

        //Open note modal
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-button")));
        WebElement noteButton = driver.findElement(By.id("note-button"));
        noteButton.click();

        //add new note
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title")));
        WebElement noteTitle = driver.findElement(By.id("note-title"));
        noteTitle.sendKeys("note-title");

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-description")));
        WebElement noteDescription = driver.findElement(By.id("note-description"));
        noteDescription.sendKeys("note-description");

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("save-changes-button")));
        WebElement savechanges = driver.findElement(By.id("save-changes-button"));
        savechanges.click();

        //verifies that the home page is no longer accessible.
        Assertions.assertNotEquals("http://localhost:" + this.port + "/add-update-note", driver.getCurrentUrl());

        //verifies that the home page is no longer accessible.back-home
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("back-home")));
        WebElement backHome = driver.findElement(By.id("back-home"));
        backHome.click();

        //Open note tab
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));
        WebElement noteTabAgain = driver.findElement(By.id("nav-notes-tab"));
        noteTabAgain.click();

    }

    @Test
    @Order(1)
    public void testAddNote() {
        AddNote();

        WebDriverWait webDriverWait = new WebDriverWait(driver, 10);

        //select record add new note
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("userTable")));
        WebElement table = driver.findElement(By.id("userTable"));
        List<WebElement> rows = table.findElements(By.tagName("tr"));
        WebElement firstRow = rows.get(1);
        List<WebElement> cells = firstRow.findElements(By.tagName("td"));

        //verifies that the note details are visible in the note list.
        Assertions.assertEquals("note-description", cells.get(1).getText());
    }

    @Test
    @Order(2)
    public void testUpdateNote() {
        //login
        doLogIn("UT", "123");

        WebDriverWait webDriverWait = new WebDriverWait(driver, 10);

        //Open note tab
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));
        WebElement noteTab = driver.findElement(By.id("nav-notes-tab"));
        noteTab.click();

        //clicks the edit note button on an existing note
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("userTable")));
        WebElement table = driver.findElement(By.id("userTable"));
        WebElement cell = table.findElement(By.xpath("//tr[1]/td[1]"));
        System.out.println(cell.getText());
        WebElement editButton = cell.findElement(By.className("editNoteButton"));
        editButton.click();

        //changes the note data
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title")));
        WebElement noteTitle = driver.findElement(By.id("note-title"));
        noteTitle.sendKeys("-update");

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-description")));
        WebElement noteDescription = driver.findElement(By.id("note-description"));
        noteDescription.sendKeys("-update");

        //saves the changes
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("save-changes-button")));
        WebElement savechanges = driver.findElement(By.id("save-changes-button"));
        savechanges.click();

        //verifies that the home page is no longer accessible.
        Assertions.assertNotEquals("http://localhost:" + this.port + "/add-update-note", driver.getCurrentUrl());

        //verifies that the home page is no longer accessible.back-home
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("back-home")));
        WebElement backHome = driver.findElement(By.id("back-home"));
        backHome.click();

        //Open note tab
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));
        WebElement noteTabAgain = driver.findElement(By.id("nav-notes-tab"));
        noteTabAgain.click();

        //select record update note
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("userTable")));
        WebElement tableUpdate = driver.findElement(By.id("userTable"));
        List<WebElement> rows = tableUpdate.findElements(By.tagName("tr"));
        WebElement firstRow = rows.get(1);
        List<WebElement> cells = firstRow.findElements(By.tagName("td"));

        //verifies that the changes appear in the note list.
        Assertions.assertEquals("note-description-update", cells.get(1).getText());
    }

    @Test
    @Order(3)
    public void testDeleteNote() {
        //login
        doLogIn("UT", "123");

        WebDriverWait webDriverWait = new WebDriverWait(driver, 10);

        //Open note tab
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));
        WebElement noteTab = driver.findElement(By.id("nav-notes-tab"));
        noteTab.click();

        // clicks the delete note button on an existing note
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("userTable")));
        WebElement table = driver.findElement(By.id("userTable"));
        WebElement cell = table.findElement(By.xpath("//tr[1]/td[1]"));
        System.out.println(cell.getText());
        WebElement deleteButton = cell.findElement(By.className("deleteNoteButton"));
        deleteButton.click();

        //verifies that the home page is no longer accessible.
        Assertions.assertNotEquals("http://localhost:" + this.port + "/add-update-note", driver.getCurrentUrl());

        //verifies that the home page is no longer accessible.
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("back-home")));
        WebElement backHome = driver.findElement(By.id("back-home"));
        backHome.click();

        //Open note tab
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));
        WebElement noteTabAgain = driver.findElement(By.id("nav-notes-tab"));
        noteTabAgain.click();

        //select record update note
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("userTable")));
        WebElement tableUpdate = driver.findElement(By.id("userTable"));
        List<WebElement> rows = tableUpdate.findElements(By.tagName("tr"));

        //verifies that the note no longer appears in the note list.
        Assertions.assertEquals(1, rows.size());
    }
}
