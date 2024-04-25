import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class MainApp {

   /* private WebDriver driver;

    @Override
    public void start(Stage primaryStage) {
        Button runTestButton = new Button("Run Test");
        runTestButton.setOnAction(event -> runTest());

        VBox root = new VBox(runTestButton);
        Scene scene = new Scene(root, 300, 250);

        primaryStage.setTitle("Test Application");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void runTest() {
        // Configuration du pilote Selenium (utilisez le chemin correct vers votre pilote)
        System.setProperty("webdriver.edge.driver", "C:/Users/siwar/Downloads/edgedriver_win64/msedgedriver.exe");
        // Initialisation du navigateur
        driver = new EdgeDriver();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        // Ouvrir la page de test de débit FTTH (speedtest.net)
        driver.get("https://www.speedtest.net/");
        Duration timeout = Duration.ofSeconds(60);

        // Créer une instance de WebDriverWait avec la durée spécifiée
        WebDriverWait wait = new WebDriverWait(driver, timeout);

        WebElement goButton = driver.findElement(By.className("start-text"));
        goButton.click();

        WebElement downloadResult = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"container\"]/div/div[3]/div/div/div/div[2]/div[3]/div[3]/div/div[3]/div/div/div[2]/div[1]/div[1]/div/div[2]/span")));
        WebElement uploadResult = driver.findElement(By.className("upload-speed"));

        // Récupérer les résultats de débit
        String downloadSpeed = downloadResult.getText();
        String uploadSpeed = uploadResult.getText();

        // Afficher les résultats
        System.out.println("Vitesse de téléchargement : " + downloadSpeed);
        System.out.println("Vitesse de téléversement : " + uploadSpeed);

        // Vérifier si les vitesses sont supérieures à zéro (test basique)
        boolean testPassed = !downloadSpeed.equals("0 Mbps") && !uploadSpeed.equals("0 Mbps");

        // Afficher une alerte en fonction du résultat du test dans l'application JavaFX
        Platform.runLater(() -> showAlert(testPassed ? "Succès du test !" : "Échec du test !"));
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Résultat du test");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @Override
    public void stop() {
        // Fermer le navigateur à la fin de l'application
        if (driver != null) {
            driver.quit();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }*/
}
