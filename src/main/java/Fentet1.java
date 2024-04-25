import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Fentet1 extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Création d'une image avec le logo Sagemcom
        Image logoImage = new Image(getClass().getResourceAsStream("/logoo.png"));

        // Créer un ImageView pour afficher l'image
        ImageView imageView = new ImageView(logoImage);

        // Redimensionner l'image à une largeur et une hauteur spécifiques
        double newWidth = 200; // largeur souhaitée
        double newHeight = 100; // hauteur souhaitée
        imageView.setFitWidth(newWidth);
        imageView.setFitHeight(newHeight);

        // Création des éléments de la popup
        TextField inputField = new TextField();

        // Mise en place du layout de la popup
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20));

        // Ajout des éléments à la grille
        // Centrer l'image horizontalement dans la première ligne
        GridPane.setHalignment(imageView, HPos.CENTER);
        gridPane.add(imageView, 0, 0, 2, 1); // L'image sur deux colonnes
        gridPane.add(new javafx.scene.control.Label("Entrer le numéro de série du produit :"), 0, 1);
        gridPane.add(inputField, 1, 1);

        // Charger la feuille de style CSS
        gridPane.getStylesheets().add(getClass().getResource("alertStyles.css").toExternalForm());

        // Création de la scène et affichage de la fenêtre
        Scene scene = new Scene(gridPane, 400, 200);
        primaryStage.setScene(scene);

        primaryStage.show();

        // Action du bouton

        // Écouteur d'événements pour la touche "Entrée" dans le champ de texte
        inputField.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                submitAction(inputField);
            }
        });
    }

    // Méthode pour l'action de soumission
    private void submitAction(TextField inputField) {
        // Récupération du texte saisi dans le champ d'entrée
        String userInput = inputField.getText();

        if (userInput.isEmpty()) {
            // Affichage d'une alerte si le champ est vide
            Alert emptyAlert = new Alert(Alert.AlertType.WARNING);
            emptyAlert.setHeaderText(null);
            emptyAlert.setContentText("Veuillez entrer le numéro de série, s'il vous plaît.");
            emptyAlert.showAndWait();
        } else {
            // Affichage d'une alerte avec le message saisi
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Commencer les tests ");
            alert.setHeaderText(null);
            alert.setContentText("Commencer les tests de  " + userInput + "!");
            alert.showAndWait();

            // Trace du début des tests avec le numéro de série
            HTMLLogger.log("Début des tests de " + userInput, "");

            // Affichage de l'interface cmdClass lorsque le bouton "Soumettre" est cliqué
            Stage cmdStage = new Stage();
            cmdClass cmd = new cmdClass();
            cmd.start(cmdStage);
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
