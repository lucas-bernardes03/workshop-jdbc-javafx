package gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import app.App;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Star;
import model.services.StarService;

public class StarListController implements Initializable, DataChangeListener {
    private StarService service;
    
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
    
    private ObservableList<Star> obsList;

    @FXML
    public void onBtNewStarAction(ActionEvent event){
        Stage parentStage = Utils.currentStage(event);
        Star star = new Star();
        createDialogForm(star, parentStage, "/gui/StarForm.fxml");
    }

    public void setStarService(StarService service){
        this.service = service;
    }

    private void initializeNodes() {
        tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tableColumnStellar.setCellValueFactory(new PropertyValueFactory<>("stellarClass"));
        tableColumnMass.setCellValueFactory(new PropertyValueFactory<>("mass"));

        Stage stage = (Stage) App.getMainScene().getWindow();
        tableViewStar.prefHeightProperty().bind(stage.heightProperty());
    }
    
    public void updateTableView(){
        if(service == null) throw new IllegalStateException("Servce was null");
        List<Star> list = service.findAll();
        obsList = FXCollections.observableArrayList(list);
        tableViewStar.setItems(obsList);
    }

    private void createDialogForm(Star star, Stage parentStage, String absoluteName){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
            Pane pane = loader.load();

            StarFormController controller = loader.getController();
            controller.setStar(star);
            controller.setStarService(new StarService());
            controller.subscribeDataChangeListener(this);
            controller.updateFormData();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Enter Star data");
            dialogStage.setScene(new Scene(pane));
            dialogStage.setResizable(false);
            dialogStage.initOwner(parentStage);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.showAndWait();

        }
        catch(IOException e){
            Alerts.showAlert("IO Exception", "Error loading layout", e.getMessage(), AlertType.ERROR);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeNodes();
    }

    @Override
    public void onDataChange() {
        updateTableView();
    }

}
