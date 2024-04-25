import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import io.github.bonigarcia.wdm.WebDriverManager;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class Sansespace extends Application {

    private WebDriver driver;
    private List<List<String>> data;

    @Override
    public void start(Stage primaryStage) {
        WebDriverManager.edgedriver().setup(); // Configuration de WebDriverManager
        driver = new EdgeDriver(); // Initialisation de WebDriver
        data = new ArrayList<>();

        // Récupération des données dans un thread séparé
        new Thread(() -> {
            retrieveData();
            Platform.runLater(() -> showUI(primaryStage));
        }).start();
    }

    private void retrieveData() {
        String ipAddress = "http://192.168.1.1/home/index.html";
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
            if ((element.isDisplayed() && !element.getText().isEmpty())) {
                for (WebElement span : spans) {
                    rowData.add(span.getText());
                }
            }
            data.add(rowData);
        }

        // Fermeture du navigateur
        driver.quit();
    }

    private void showUI(Stage primaryStage) {
        VBox root = new VBox();
        if (!data.isEmpty() && !data.get(0).isEmpty()) {
            String title = data.get(0).get(0);
            Label titleLabel = new Label(title);
            titleLabel.setStyle("-fx-font-weight: bold;");
            root.getChildren().add(titleLabel);
        }

        // Création du tableau
        TableView<NameValuePair> tableView = new TableView<>();
        ObservableList<NameValuePair> items = FXCollections.observableArrayList();

        // Parcours des données
        for (List<String> rowData : data) {
            if (!rowData.isEmpty()) {
                // Parcours des paires clé-valeur
                for (int i = 0; i < rowData.size(); i += 2) {
                    // Récupération de la clé et de la valeur
                    if (i + 1 < rowData.size()) {
                        String key = rowData.get(i);
                        String value = rowData.get(i + 1);

                        // Ajout de la paire clé-valeur à la liste des éléments du tableau
                        items.add(new NameValuePair(key, value));

                        // Ajout des colonnes supplémentaires pour chaque paire clé-valeur
                        TableColumn<NameValuePair, String> column = new TableColumn<>(key);
                        column.setCellValueFactory(new PropertyValueFactory<>(value));
                        tableView.getColumns().add(column);
                    }
                }
            }
        }

        // Ajout des colonnes clé-valeur au tableau
        TableColumn<NameValuePair, String> keyColumn = new TableColumn<>("Clé");
        keyColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<NameValuePair, String> valueColumn = new TableColumn<>("Valeur");
        valueColumn.setCellValueFactory(new PropertyValueFactory<>("value"));

        tableView.getColumns().addAll(keyColumn, valueColumn);

        // Ajout des éléments au tableau
        tableView.setItems(items);

        // Ajout du tableau à la VBox
        root.getChildren().add(tableView);

        // Affichage de l'interface utilisateur
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Informations des éléments HTML");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    // Classe auxiliaire pour représenter une paire clé-valeur
    public static class NameValuePair {
        private final String name;
        private final String value;

        public NameValuePair(String name, String value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public String getValue() {
            return value;
        }
    }
}
