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
import org.openqa.selenium.StaleElementReferenceException;
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

public class Main extends Application {

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
        testUsbButton.setOnAction(event -> testAdresMac(extractTableData(), "1111"));
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
        WebElement loginPasswordInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login_password")));
        loginPasswordInput.sendKeys("sagemcom2023");
        WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("login_save")));
        loginButton.click();

        WebElement link1 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='wm3']/div[2]/div[3]/div")));
        link1.click();
        WebElement link2 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='wm3']/div[2]/div[3]/div[2]")));
        link2.click();
        WebElement link3 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='systemInformation']/div")));
        link3.click();
        WebElement iframe = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("iframeapp")));
        driver.switchTo().frame(iframe);

        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("loading_screen")));

        WebElement table = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("testzebra_table")));

        List<WebElement> rows = table.findElements(By.tagName("tr"));

        List<TableRowData> tableData = new ArrayList<>();
        for (WebElement row : rows) {
            try {
                List<WebElement> cells = row.findElements(By.tagName("td"));
                if (cells.size() >= 2) {
                    String columnName = cells.get(1).getText();
                    WebElement values = row.findElement(By.className("conf-table-cell-zebra-value"));
                    wait.until(ExpectedConditions.visibilityOf(values)); // Attente jusqu'à ce que la valeur soit visible
                    String cellValue = values.getText();
                    System.out.println("Clé: " + columnName + ", Valeur: " + cellValue);
                    TableRowData systemInfo = new TableRowData(columnName, cellValue);
                    tableData.add(systemInfo);
                }
            } catch (StaleElementReferenceException e) {
                System.out.println("L'élément n'est plus attaché au DOM : " + e.getMessage());
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
    private void testAdresMac(List<TableRowData> extractedData, String userInput) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Test USB");
        dialog.setHeaderText("Test Adres Mac");
        dialog.setContentText("Veuillez entrer une adresse MAC pour le test :");

        // Afficher la boîte de dialogue et attendre la réponse de l'utilisateur
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(macAddress -> {
            // Récupérer la cinquième valeur du tableau
            String fifthValue = extractedData.get(4).getColumn2();

            if (macAddress.equals(fifthValue)) {
                // Afficher un message de réussite si les deux valeurs sont égales
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Test USB");
                successAlert.setHeaderText(null);
                successAlert.setContentText("Le test d'adresse MAC est réussi !");
                successAlert.showAndWait();
            } else {
                // Afficher un message d'erreur si les deux valeurs ne sont pas égales
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Test USB");
                errorAlert.setHeaderText(null);
                errorAlert.setContentText("Le test d'adresse MAC a échoué !");
                errorAlert.showAndWait();
            }
        });
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
