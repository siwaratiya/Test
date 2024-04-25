import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class Information extends Application {

    private WebDriver driver;
    private List<List<String>> data;

    @Override
    public void start(Stage primaryStage) {
        // Création du bouton pour afficher les informations
        Button displayButton = new Button("Afficher les informations");
        displayButton.setOnAction(event -> {
            // Initialisation de WebDriver et récupération des données
            retrieveData();
            // Affichage des données
            showUI(primaryStage);
        });

        // Affichage du bouton
        Scene scene = new Scene(new VBox(displayButton), 400, 100);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Cliquez pour afficher les informations");
        primaryStage.show();
    }

    private void retrieveData() {
        String ipAddress = "http://192.168.1.1/home/index.html";
        driver = new EdgeDriver();
        data = new ArrayList<>();

        driver.get(ipAddress);

        // Attente que la page soit chargée
        Duration timeout = Duration.ofSeconds(60);
        WebDriverWait wait = new WebDriverWait(driver, timeout);
        WebElement linkMenu = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("linkMenu")));
        linkMenu.click();

        WebElement menu = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("menu")));
        List<WebElement> menuItems = menu.findElements(By.tagName("li"));
        WebElement deuxiemeMenu = menuItems.get(1);
        deuxiemeMenu.click();

        // Connexion avec le mot de passe
        driver.findElement(By.id("f_password")).sendKeys("FixBox123@");
        driver.findElement(By.id("f_submit_login")).click();

        // Attendre que le menu système soit chargé
        WebElement linkMenu1 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("menu_system")));
        linkMenu1.click();

        // Récupération des données
        List<WebElement> elements = driver.findElements(By.className("clearboth"));
        for (WebElement element : elements) {
            List<String> rowData = new ArrayList<>();
            List<WebElement> spans = element.findElements(By.tagName("span"));
            for (WebElement span : spans) {
                rowData.add(span.getText());
            }
            data.add(rowData);
        }

        // Fermeture du navigateur
        // driver.quit();
    }

    private void showUI(Stage primaryStage) {
        VBox root = new VBox();

        // Parcours des données
        for (List<String> rowData : data) {
            if (!rowData.isEmpty()) {
                // Extraction du premier élément comme titre
                String title = rowData.get(0);
                Label titleLabel = new Label(title);
                titleLabel.setStyle("-fx-font-weight: bold;");

                // Création d'une grille pour stocker les paires clé-valeur
                GridPane gridPane = new GridPane();
                gridPane.setHgap(10);
                gridPane.setVgap(5);

                // Parcours des autres éléments comme des paires clé-valeur
                for (int i = 1; i < rowData.size(); i += 2) {
                    // Récupération de la clé et de la valeur
                    String key = rowData.get(i);
                    String value = (i + 1 < rowData.size()) ? rowData.get(i + 1) : "";

                    // Création des labels pour la clé et la valeur
                    Label keyLabel = new Label(key + ":");
                    Label valueLabel = new Label(value);
                    valueLabel.setStyle("-fx-font-family: monospace;");

                    // Ajout des labels à la grille
                    gridPane.addRow(i / 2, keyLabel, valueLabel);
                }

                // Ajout de la grille à la VBox
                root.getChildren().addAll(titleLabel, gridPane);
            }
        }

        // Ajout du VBox dans un ScrollPane
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(root);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

        // Affichage de l'interface utilisateur
        Scene scene = new Scene(scrollPane);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Informations des éléments HTML");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
