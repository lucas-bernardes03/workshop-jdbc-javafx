package gui;

import java.net.URL;
import java.util.ResourceBundle;

import app.App;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Star;

public class StarListController implements Initializable {
    @FXML
    private TableView<Star> tableViewStar;
    @FXML
    private TableColumn<Star,Integer> tableColumnId;
    @FXML
    private TableColumn<Star,String> tableColumnName;
    @FXML
    private TableColumn<Star,String> tableColumnStellar;
    @FXML
    private TableColumn<Star,Double> tableColumnMass;
    @FXML
    private Button btNewStar;
    
    @FXML
    public void onBtNewStarAction(){
        System.out.println("Dust clouds are gathering...");
    }

    private void initializeNodes() {
        tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tableColumnStellar.setCellValueFactory(new PropertyValueFactory<>("stellarClass"));
        tableColumnMass.setCellValueFactory(new PropertyValueFactory<>("mass"));

        Stage stage = (Stage) App.getMainScene().getWindow();
        tableViewStar.prefHeightProperty().bind(stage.heightProperty());
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeNodes();
    }

}
