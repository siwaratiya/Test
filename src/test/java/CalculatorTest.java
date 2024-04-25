import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.JavascriptExecutor;

public class CalculatorTest {

    /*private WebDriver driver;
    private boolean testPassed;

    @BeforeClass
    public void setUp() {
        System.setProperty("webdriver.edge.driver", "C:/Users/siwar/Downloads/edgedriver_win64/msedgedriver.exe");
        driver = new EdgeDriver();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    @Test
    public void testFTTHSpeed() throws UnsupportedEncodingException {
        driver.get("https://www.speedtest.net/");

        Duration timeout = Duration.ofSeconds(60);
        WebDriverWait wait = new WebDriverWait(driver, timeout);

        WebElement goButton = driver.findElement(By.className("start-text"));
        goButton.click();

        WebElement downloadResult = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"container\"]/div/div[3]/div/div/div/div[2]/div[3]/div[3]/div/div[3]/div/div/div[2]/div[1]/div[1]/div/div[2]/span")));
        WebElement uploadResult = driver.findElement(By.className("upload-speed"));

        String downloadSpeed = downloadResult.getText();
        String uploadSpeed = uploadResult.getText();

        System.out.println("Vitesse de téléchargement : " + downloadSpeed);
        System.out.println("Vitesse de téléversement : " + uploadSpeed);

        Assert.assertNotEquals(downloadSpeed, "0 Mbps", "Vitesse de téléchargement invalide");
        Assert.assertNotEquals(uploadSpeed, "0 Mbps", "Vitesse de téléversement invalide");

        testPassed = true;
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            ((JavascriptExecutor) driver).executeScript("alert('" + (testPassed ? "Succès du test !" : "Échec du test !") + "')");

          // driver.quit();
        }
    }*/
}
