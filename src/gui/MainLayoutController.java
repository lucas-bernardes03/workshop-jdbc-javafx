package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import app.App;
import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;
import model.services.StarService;

public class MainLayoutController implements Initializable{
    @FXML
    private MenuItem menuItemStar;
    @FXML
    private MenuItem menuItemPlanet;
    @FXML
    private MenuItem menuItemSatellite;
    @FXML
    private MenuItem menuItemAbout;

    @FXML
    public void onMenuItemStarAction(){
        loadLayout("/gui/StarList.fxml", (StarListController controller) -> {
            controller.setStarService(new StarService());
            controller.updateTableView();
        });
    }
    
    @FXML
    public void onMenuItemPlantetAction(){
        System.out.println("The earth is one of the planets in history!");
    }
    
    @FXML
    public void onMenuItemSatelliteAction(){
        System.out.println("We gonna steal the moon!");
    }
    
    @FXML
    public void onMenuItemAboutAction(){
        loadLayout("/gui/About.fxml", x -> {});
    }

    private synchronized <T> void loadLayout(String absoluteName, Consumer<T> initializeAction){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
            VBox newVBox = loader.load();

            Scene mainScene = App.getMainScene();
            VBox mainVBox = ((VBox)((ScrollPane)mainScene.getRoot()).getContent());

            Node mainMenu = mainVBox.getChildren().get(0);
            mainVBox.getChildren().clear();
            mainVBox.getChildren().add(mainMenu);
            mainVBox.getChildren().addAll(newVBox.getChildren());

            T controller = loader.getController();
            initializeAction.accept(controller);
        }
        catch(IOException e){
            Alerts.showAlert("IO Exception", "Error loading layout", e.getMessage(), AlertType.ERROR);
        }
    }

    


    @Override
    public void initialize(URL location, ResourceBundle resources) {    
    }
    
}
