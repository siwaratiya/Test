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

import java.util.List;
import java.util.concurrent.TimeUnit;

public class ExDynamicTable extends Application {

    @Override
    public void start(Stage primaryStage) {
        VBox root = new VBox();

        // Création du TableView
        TableView<TableRowData> tableView = new TableView<>();

        // Extraction des données de la table HTML
        List<TableRowData> tableData = extractTableData();

        // Appel de la méthode log après l'extraction des données
        HTMLLogger.log("Extraction terminée", tableData.toString());

        // Création des colonnes du TableView
        for (int i = 0; i < tableData.get(0).getColumns().size(); i++) {
            TableColumn<TableRowData, String> column = new TableColumn<>(String.format("Colonne %d", i + 1));
            int columnIndex = i;
            column.setCellValueFactory(cellData -> cellData.getValue().getColumns().get(columnIndex));
            tableView.getColumns().add(column);
        }

        // Ajout des données au TableView
        ObservableList<TableRowData> data = FXCollections.observableArrayList(tableData);
        tableView.setItems(data);

        // Ajout du TableView à la scène
        root.getChildren().add(tableView);

        // Affichage de la scène
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("TableView from HTML");
        primaryStage.show();
    }

    private List<TableRowData> extractTableData() {
        WebDriver driver = new EdgeDriver();
        driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);

        // Charger la page Web contenant le tableau HTML
        driver.get("https://demo.guru99.com/test/web-table-element.php");

        // Récupérer les lignes du tableau
        List<WebElement> rows = driver.findElements(By.xpath("//*[@id='leftcontainer']/table/tbody/tr"));

        ObservableList<TableRowData> tableData = FXCollections.observableArrayList();

        for (WebElement row : rows) {
            List<WebElement> cells = row.findElements(By.tagName("td"));

            // Ignorer les lignes vides
            if (!cells.isEmpty()) {
                TableRowData rowData = new TableRowData();
                for (WebElement cell : cells) {
                    rowData.getColumns().add(new SimpleStringProperty(cell.getText()));
                }
                tableData.add(rowData);
            }
        }

        driver.quit();

        return tableData;

    }

    public static void main(String[] args) {
        launch(args);
    }

    public static class TableRowData {
        private final ObservableList<SimpleStringProperty> columns = FXCollections.observableArrayList();

        public ObservableList<SimpleStringProperty> getColumns() {
            return columns;
        }
        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            for (SimpleStringProperty column : columns) {
                stringBuilder.append(column.get()).append(", ");
            }
            // Supprimer la virgule et l'espace en trop à la fin
            stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length());
            return stringBuilder.toString();
        }
    }
}
