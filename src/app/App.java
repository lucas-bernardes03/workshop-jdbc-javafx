package app;
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;

public class App extends Application{
    
    private static Scene mainScene;

    public void start(Stage primaryStage){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/MainLayout.fxml"));
            ScrollPane scrollPane = loader.load();
            scrollPane.setFitToHeight(true);
            scrollPane.setFitToWidth(true);

            mainScene = new Scene(scrollPane);
            primaryStage.setTitle("Sample App");
            primaryStage.setScene(mainScene);
            primaryStage.show();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    public static Scene getMainScene(){
        return mainScene;
    }
    public static void main(String[] args) {
        launch(args);
    }

}
