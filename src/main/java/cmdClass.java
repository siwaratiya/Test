import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class cmdClass extends Application {
    private Button testLedButton;
    private boolean isLedOn = false;

    @Override
    public void start(Stage primaryStage) {
        VBox root = new VBox(10);
        root.setPadding(new Insets(10));

        // Créer une zone de texte pour afficher la sortie du ping
        TextArea pingOutputTextArea = new TextArea();
        pingOutputTextArea.setEditable(false);

        // Création du TableView pour afficher les données extraites
        TableView<TableRowData> extractionTableView = new TableView<>();

        Button testPasswordButton = new Button("Test Password");
        testLedButton = new Button("Test LED");
        Button testWifiButton = new Button("Information Modem");
        Button testUsbButton = new Button("Test USB");
        testPasswordButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        testWifiButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
        testUsbButton.setStyle("-fx-background-color: #555555; -fx-text-fill: white;");
        Image logoImage = new Image(getClass().getResourceAsStream("/logo.png"));

        // Créer un ImageView pour afficher l'image
        ImageView imageView = new ImageView(logoImage);

        // Redimensionner l'image à une largeur et une hauteur spécifiques
        double newWidth = 200; // largeur souhaitée
        double newHeight = 100; // hauteur souhaitée
        imageView.setFitWidth(newWidth);
        imageView.setFitHeight(newHeight);

        // Créer un label pour afficher le message de test
        Label testMessageLabel = new Label("Commencer les tests de 1111");

        HBox buttonBox = new HBox(10);
        buttonBox.getChildren().addAll(testPasswordButton, testLedButton, testWifiButton, testUsbButton);

        // Ajouter tous les éléments à la racine VBox
        root.getChildren().addAll(imageView, testMessageLabel, pingOutputTextArea, extractionTableView, buttonBox);

        Scene scene = new Scene(root, 600, 400);

        primaryStage.setTitle("Command Output GUI");
        primaryStage.setScene(scene);
        primaryStage.show();

        testLedButton.setOnAction(event -> testLed(primaryStage));
        testWifiButton.setOnAction(event -> {
            // Appeler la méthode pour extraire les données
            List<TableRowData> extractedData = extractTableData();
            // Effacer les colonnes précédentes
            extractionTableView.getColumns().clear();
            // Ajouter les données au TableView
            extractionTableView.getItems().clear();
            extractionTableView.getColumns().addAll(createColumns(2)); // Modifier le nombre de colonnes selon votre cas
            extractionTableView.getItems().addAll(extractedData);

            // Enregistrer les données dans le fichier HTML
            logTableData(extractedData);
        });
        try {
            ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", "ping 192.168.1.1");
            Process process = builder.start();

            // Lire la sortie du ping et l'afficher dans la zone de texte du ping
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            StringBuilder output = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            int exitCode = process.waitFor();
            output.append("Le processus CMD s'est terminé avec le code de sortie : ").append(exitCode);
            pingOutputTextArea.setText(output.toString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void testLed(Stage primaryStage) {
        // Afficher une boîte de dialogue avec deux boutons "Allumé" et "Non allumé"
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Test LED");
        alert.setHeaderText("La LED est-elle allumée ?");
        alert.setContentText("Sélectionnez une option :");

        ButtonType buttonTypeAllume = new ButtonType("Allumé");
        ButtonType buttonTypeNonAllume = new ButtonType("Non allumé");

        alert.getButtonTypes().setAll(buttonTypeAllume, buttonTypeNonAllume);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent()) {
            if (result.get() == buttonTypeAllume) {
                // Test réussi
                isLedOn = true;
                testLedButton.setStyle("-fx-background-color: green; -fx-text-fill: white;");
                showAlert(Alert.AlertType.INFORMATION, "Test LED", "Test passé", "La LED est allumée");
                HTMLLogger.log("Clique sur le bouton 'Test LED'", "La LED est allumée");

            } else if (result.get() == buttonTypeNonAllume) {
                // Test échoué
                isLedOn = false;
                testLedButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");
                showAlert(Alert.AlertType.ERROR, "Test LED", "Test échoué", "La LED n'est pas allumée");
                HTMLLogger.log("Clique sur le bouton 'Test LED'", "La LED n'est pas allumée");

            }
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String headerText, String contentText) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }

    private List<TableRowData> extractTableData() {
        WebDriver driver = new EdgeDriver();
        Duration timeout = Duration.ofSeconds(60);
        WebDriverWait wait = new WebDriverWait(driver, timeout);
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

    private ObservableList<TableColumn<TableRowData, String>> createColumns(int numberOfColumns) {
        ObservableList<TableColumn<TableRowData, String>> columns = FXCollections.observableArrayList();
        for (int i = 0; i < numberOfColumns; i++) {
            final int columnIndex = i;
            TableColumn<TableRowData, String> column = new TableColumn<>("Colonne " + (columnIndex + 1));
            column.setCellValueFactory(data -> {
                if (columnIndex == 0) {
                    return data.getValue().column1Property();
                } else {
                    return data.getValue().column2Property();
                }
            });
            columns.add(column);
        }
        return columns;
    }

    private void logTableData(List<TableRowData> tableData) {
        StringBuilder htmlContent = new StringBuilder();
        htmlContent.append("<!DOCTYPE html><html><head><style>");
        htmlContent.append("table {border-collapse: collapse; width: 100%;}");
        htmlContent.append("th, td {border: 1px solid #ddd; text-align: left; padding: 10px;}");
        htmlContent.append(".title {color: red; font-size: 20px;}");
        htmlContent.append(".column1 {color: #8B4513;}");
        htmlContent.append(".column2 {color: #4682B4;}");
        htmlContent.append(".date {color: #4682B4;}");
        htmlContent.append("</style></head><body>");
        htmlContent.append("<h1 class=\"title\">Extraction des donnees du tableau HTML</h1>");
        htmlContent.append("<table>");
        htmlContent.append("<tr><th class=\"column1\">Column 1</th><th class=\"column2\">Column 2</th></tr>");
        for (TableRowData data : tableData) {
            String column1Class = data.getColumn1().contains("-") ? "date" : "column1";
            String column2Class = data.getColumn2().contains("-") ? "date" : "column2";
            htmlContent.append("<tr><td class=\"").append(column1Class).append("\">").append(data.getColumn1()).append("</td><td class=\"").append(column2Class).append("\">").append(data.getColumn2()).append("</td></tr>");
        }
        htmlContent.append("</table>");
        htmlContent.append("<p class=\"title\">Extraction terminee</p>");
        htmlContent.append("</body></html>");

        HTMLLogger.log("Extraction terminee", htmlContent.toString());
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
