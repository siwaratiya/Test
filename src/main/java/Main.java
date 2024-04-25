/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application
{
    private static Stage stage;
    private static Scene scene;

    @Override
    public void start(Stage stage) throws Exception
    {
        stage.initStyle(StageStyle.UNDECORATED);
        setPrimaryStage(stage);
        setPrimaryScene(scene);
        Parent root = FXMLLoader.load(getClass().getResource("view/fxml/alwaysdisplayed/Main.fxml"));

        scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args)
    {
        launch(args);
    }

    private void setPrimaryStage(Stage stage)
    {
        Main.stage = stage;
    }

    public static Stage getMainStage()
    {
        return Main.stage;
    }

    private void setPrimaryScene(Scene scene)
    {
        Main.scene = scene;
    }

    public static Scene getMainScene()
    {
        return Main.scene;
    }
}
