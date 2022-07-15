package gui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;

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
        System.out.println("To the stars!");
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
        System.out.println("About.");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {    
    }
    
}
