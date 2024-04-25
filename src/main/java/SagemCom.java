import io.github.bonigarcia.wdm.WebDriverManager;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
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

import java.time.Duration;
import java.util.List;

public class SagemCom extends Application {

    private WebDriver driver;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        WebDriverManager.edgedriver().setup();
        driver = new EdgeDriver();
        String ipAddress = "http://192.168.1.1";
        driver.get(ipAddress);

        Duration timeout = Duration.ofSeconds(60);
        WebDriverWait wait = new WebDriverWait(driver, timeout);

        try {
            WebElement loginPasswordInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login_password")));
            loginPasswordInput.sendKeys("EHEjYZST");

            WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("login_save")));
            loginButton.click();

            WebElement link1 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"wm3\"]/div[2]/div[3]/div")));
            link1.click();
            WebElement link2 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"wm3\"]/div[2]/div[3]/div[2]")));
            link2.click();
            WebElement link3 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"systemInformation\"]/div")));
            link3.click();
            WebElement link4 =driver.findElement(By.id("tab_information_ftth"));
            link4.click();


            List<WebElement> tables = driver.findElements(By.xpath("//*[@id=\"testzebra_table\"]"));

            // Choisissez le tableau spécifique que vous voulez traiter
            WebElement table = null;
            for (WebElement currentTable : tables) {
                List<WebElement> rows = currentTable.findElements(By.tagName("tr"));
                if (rows.size() > 0) {

                    WebElement firstRow = rows.get(0);
                    List<WebElement> cells = firstRow.findElements(By.tagName("td"));
                    if (cells.size() > 0) {


                        WebElement firstCell = cells.get(0);
                        String firstCellText = firstCell.getText().trim(); // Trim pour enlever les espaces blancs
                        if (firstCellText.equals("1.1")) { // Mettez votre condition ici
                            System.out.println("hihihihihihihihihih");

                            table = currentTable;
                            break;
                        }
                    }
                }
            }

            if (table != null) {
                List<WebElement> rows = table.findElements(By.tagName("tr"));
                ObservableList<DataRow> dataRows = FXCollections.observableArrayList();

                for (WebElement row : rows) {
                    List<WebElement> cells = row.findElements(By.tagName("td"));
                    if (cells.size() == 2) {
                        String key = cells.get(0).getText();
                        String value = cells.get(1).getText();
                        dataRows.add(new DataRow(key, value));
                    }
                }

                TableView<DataRow> tableView = new TableView<>(dataRows);
                TableColumn<DataRow, String> keyColumn = new TableColumn<>("Clé");
                keyColumn.setCellValueFactory(new PropertyValueFactory<>("key"));
                TableColumn<DataRow, String> valueColumn = new TableColumn<>("Valeur");
                valueColumn.setCellValueFactory(new PropertyValueFactory<>("value"));
                tableView.getColumns().addAll(keyColumn, valueColumn);

                VBox root = new VBox(tableView);
                Scene scene = new Scene(root, 400, 300);
                primaryStage.setScene(scene);
                primaryStage.setTitle("Tableau de données");
                primaryStage.show();
            } else {
                System.out.println("Le tableau spécifique n'a pas été trouvé.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Gérer l'exception, afficher un message d'erreur, etc.
        }
    }

    @Override
    public void stop() {
        if (driver != null) {
            driver.quit();
        }
    }

    public static class DataRow {
        private final String key;
        private final String value;

        public DataRow(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }
    }
}
