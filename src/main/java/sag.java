import io.github.bonigarcia.wdm.WebDriverManager;
import javafx.application.Platform;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class sag {
    public static void main(String[] args) {
        System.setProperty("webdriver.edge.driver", "C:/Users/siwar/Downloads/edgedriver_win64/msedgedriver.exe");

        WebDriver driver = new EdgeDriver();
        String ipAddress = "http://192.168.1.1";
        // String ipAddress = "http://localhost:63342/siwwar/expli.html?_ijt=i5a00uulgklkvlpt7dsredsvpa&_ij_reload=RELOAD_ON_SAVE";
        driver.get(ipAddress);


        Duration timeout = Duration.ofSeconds(120);
        WebDriverWait wait = new WebDriverWait(driver, timeout);
        WebElement loginPasswordInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login_password")));
        loginPasswordInput.sendKeys("EHEjYZST");

        // Attendre que le bouton de connexion soit cliquable
        WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("login_save")));
        loginButton.click();

        // Attendre que la page se charge complètement
        WebElement link1 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"wm3\"]/div[2]/div[3]/div")));
        link1.click();
        WebElement link2 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"wm3\"]/div[2]/div[3]/div[2]")));
        link2.click();
        WebElement link3 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"systemInformation\"]/div")));
        link3.click();

        // Récupération des éléments du tableau
        WebElement table = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("testzebra_table")));
        System.out.println("Tableau trouvé : " + table.getText()); // Vérifiez si le tableau est trouvé et affiche son contenu

        for (WebElement row : table.findElements(By.tagName("tr"))) {
            for (WebElement cell : row.findElements(By.tagName("td"))) {
                System.out.print(cell.getText() + "\t");
            }
            System.out.println();
        }

    }
}
