import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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
import java.util.concurrent.TimeUnit;

public class MyJFrame extends Application {

    @Override
    public void start(Stage primaryStage) {
        VBox root = new VBox();

        // Création du TableView
        TableView<TableRowData> tableView = new TableView<>();

        // Création des colonnes du TableView
        TableColumn<TableRowData, String> column1 = new TableColumn<>("Colonne 1");
        column1.setCellValueFactory(cellData -> cellData.getValue().column1Property());

        TableColumn<TableRowData, String> column2 = new TableColumn<>("Colonne 2");
        column2.setCellValueFactory(cellData -> cellData.getValue().column2Property());

        // Ajout des colonnes au TableView
        tableView.getColumns().addAll(column1, column2);

        // Extraction des données de la table HTML
        List<TableRowData> tableData = extractTableData();

        // Affichage des résultats dans la console
        for (TableRowData rowData : tableData) {
            System.out.println("Colonne 1 : " + rowData.getColumn1());
            System.out.println("Colonne 2 : " + rowData.getColumn2());
            System.out.println();
        }

        // Ajout des données au TableView
        ObservableList<TableRowData> data = FXCollections.observableArrayList(tableData);
        tableView.setItems(data);

        // Ajout du TableView à la scène
        root.getChildren().add(tableView);

        // Affichage de la scène
        Scene scene = new Scene(root, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.setTitle("TableView from HTML");
        primaryStage.show();
    }

    private List<TableRowData> extractTableData() {
        WebDriver driver = new EdgeDriver();
        //  driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
        Duration timeout = Duration.ofSeconds(60);
        WebDriverWait wait = new WebDriverWait(driver, timeout);
        // Charger la page Web contenant le tableau HTML
        String ipAddress = "http://192.168.1.1";
        driver.get(ipAddress);

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
        // Attendre que le contenu soit chargé
        WebElement mainContent = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("main_content")));

        // Récupérer tous les divs avec la classe "clearboth"
        List<WebElement> rows = mainContent.findElements(By.className("clearboth"));

        List<TableRowData> tableData = new ArrayList<>();

        for (WebElement row : rows) {
            // Trouver le span à l'intérieur du div avec la classe "control-label"
            WebElement label = row.findElement(By.className("control-label"));
            WebElement span = label.findElement(By.tagName("span"));
            String columnName = span.getText();
            System.out.println(columnName);

            // Trouver le span à l'intérieur du div avec la classe "controls-content"
            List<WebElement> spansInContent = row.findElements(By.cssSelector(".controls.controls-content span"));
            for (WebElement spanValue : spansInContent) {
                String cellValue = spanValue.getText();


                // Ajouter les données à la liste uniquement si cellValue n'est pas vide
                if (!cellValue.isEmpty()) {
                    tableData.add(new TableRowData(columnName, cellValue));

                }
            }
        }


        driver.quit();

        return tableData;
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static class TableRowData {
        private final SimpleStringProperty column1;
        private final SimpleStringProperty column2;

        public TableRowData(String column1, String column2) {
            this.column1 = new SimpleStringProperty(column1);
            this.column2 = new SimpleStringProperty(column2);
        }

        public String getColumn1() {
            return column1.get();
        }

        public SimpleStringProperty column1Property() {
            return column1;
        }

        public String getColumn2() {
            return column2.get();
        }

        public SimpleStringProperty column2Property() {
            return column2;
        }
    }
}
