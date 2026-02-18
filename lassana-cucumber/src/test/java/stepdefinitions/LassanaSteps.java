package stepdefinitions;

import io.cucumber.java.After;
import io.cucumber.java.en.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LassanaSteps {

    WebDriver driver;
    WebDriverWait wait;

    // ================= OPEN BROWSER =================
    @Given("user opens the browser")
    public void user_opens_the_browser() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    // ================= NAVIGATE =================
    @When("user navigates to {string}")
    public void user_navigates_to(String url) {
        driver.get(url);
        handlePopup();
    }

    // ================= POPUP HANDLING =================
    private void handlePopup() {
        try {
            WebElement acceptBtn = wait.until(
                    ExpectedConditions.elementToBeClickable(
                            By.xpath("//button[contains(text(),'Accept') or contains(text(),'OK')]")
                    )
            );
            acceptBtn.click();
        } catch (Exception e) {
            // Popup not displayed – continue execution
        }
    }

    // ================= VERIFY TITLE =================
    @Then("page title should contain {string}")
    public void page_title_should_contain(String title) {
        String actualTitle = driver.getTitle();
        if (!actualTitle.contains(title)) {
            throw new AssertionError("Expected title to contain: " + title);
        }
    }

    // ================= SEARCH PRODUCT =================
    @And("user searches for {string}")
    public void user_searches_for(String product) {
        WebElement searchBox = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.xpath("//input[contains(@placeholder,'Search')]")
                )
        );
        searchBox.clear();
        searchBox.sendKeys(product);
        searchBox.sendKeys(Keys.ENTER);
    }

    // ================= SELECT SECOND PRODUCT =================
    @And("user selects the second product")
    public void user_selects_the_second_product() {
        WebElement secondProduct = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.xpath("(//a[contains(@href,'product')])[2]")
                )
        );
        secondProduct.click();
    }

    // ================= ADD TO CART =================
    @And("user adds product to cart")
    public void user_adds_product_to_cart() {
        try {
            WebElement addToCartSpan = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(By.id("greenButtonText"))
            );

            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", addToCartSpan);

            WebElement addToCartBtn = addToCartSpan.findElement(By.xpath("./..")); // parent of span
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", addToCartBtn);

        } catch (Exception e) {
            throw new RuntimeException("Add to Cart button not found or not clickable", e);
        }
    }

    // ================= CART AND CHECKOUT =================
    @And("user goes to cart and proceeds to checkout")
    public void user_goes_to_cart_and_proceeds_to_checkout() {
        try {
            // Wait and click Cart icon using the correct ID
            WebElement cartButton = wait.until(
                    ExpectedConditions.elementToBeClickable(By.id("headerRightShopCartIcon"))
            );
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", cartButton);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", cartButton);

            // Wait and click Checkout button
            WebElement checkoutButton = wait.until(
                    ExpectedConditions.elementToBeClickable(By.id("lv-ybdetailbtn-txt"))
            );
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", checkoutButton);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", checkoutButton);

        } catch (TimeoutException e) {
            throw new RuntimeException("Cart or Checkout button not found. Check selectors!", e);
        }
    }


    // ================= CLOSE BROWSER =================
    @After
    public void close_browser() {
        if (driver != null) {
            driver.quit();
        }
    }
}
