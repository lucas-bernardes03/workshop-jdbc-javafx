package gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import app.App;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.db.DbIntegrityException;
import model.entities.Satellite;
import model.services.SatelliteService;

public class SatelliteListController implements Initializable, DataChangeListener {
    private SatelliteService service;
    
    @FXML
    private TableView<Satellite> tableViewSatellite;
    @FXML
    private TableColumn<Satellite,Integer> tableColumnId;
    @FXML
    private TableColumn<Satellite,String> tableColumnName;
    @FXML
    private TableColumn<Satellite,String> tableColumnPlanet;

    @FXML
    private TableColumn<Satellite,Satellite> tableColumnEdit;
    @FXML
    private TableColumn<Satellite,Satellite> tableColumnRemove;
    @FXML
    private Button btNewSatellite;
    
    private ObservableList<Satellite> obsList;

    @FXML
    public void onBtNewSatelliteAction(ActionEvent event){
        Stage parentStage = Utils.currentStage(event);
        Satellite satellite = new Satellite();
        createDialogForm(satellite, parentStage, "/gui/SatelliteForm.fxml");
    }

    public void setSatelliteService(SatelliteService service){
        this.service = service;
    }

    private void initializeNodes() {
        tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tableColumnPlanet.setCellValueFactory(new PropertyValueFactory<>("planet"));

        Stage stage = (Stage) App.getMainScene().getWindow();
        tableViewSatellite.prefHeightProperty().bind(stage.heightProperty());
    }
    
    public void updateTableView(){
        if(service == null) throw new IllegalStateException("Service was null");
        List<Satellite> list = service.findAll();
        obsList = FXCollections.observableArrayList(list);
        tableViewSatellite.setItems(obsList);
        initEditButtons();
        initRemoveButtons();
    }

    private void createDialogForm(Satellite satellite, Stage parentStage, String absoluteName){
        // try{
        //     FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
        //     Pane pane = loader.load();

        //     SatelliteFormController controller = loader.getController();
        //     controller.setSatellite(satellite);
        //     controller.setSatelliteService(new SatelliteService());
        //     controller.subscribeDataChangeListener(this);
        //     controller.updateFormData();

        //     Stage dialogStage = new Stage();
        //     dialogStage.setTitle("Enter Satellite data");
        //     dialogStage.setScene(new Scene(pane));
        //     dialogStage.setResizable(false);
        //     dialogStage.initOwner(parentStage);
        //     dialogStage.initModality(Modality.WINDOW_MODAL);
        //     dialogStage.showAndWait();

        // }
        // catch(IOException e){
        //     e.printStackTrace();
        //     Alerts.showAlert("IO Exception", "Error loading layout", e.getMessage(), AlertType.ERROR);
        // }
    }
    
    private void initEditButtons(){
        tableColumnEdit.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        tableColumnEdit.setCellFactory(param -> new TableCell<Satellite,Satellite>(){
            private final Button bt = new Button("Edit");

            @Override
            protected void updateItem(Satellite satellite, boolean empty){
                super.updateItem(satellite, empty);
                if(satellite == null){
                    setGraphic(null);
                    return;
                }
                
                setGraphic(bt);
                bt.setOnAction(event -> createDialogForm(satellite, Utils.currentStage(event), "/gui/SatelliteForm.fxml"));
            }
        });
    }

    private void initRemoveButtons(){
        tableColumnRemove.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        tableColumnRemove.setCellFactory(param -> new TableCell<Satellite,Satellite>(){
            private final Button bt = new Button("Remove");

            @Override
            protected void updateItem(Satellite satellite, boolean empty){
                super.updateItem(satellite, empty);
                if(satellite == null){
                    setGraphic(null);
                    return;
                }
                setGraphic(bt);
                bt.setOnAction(event -> removeSatellite(satellite));
            }
        });
    }

    private void removeSatellite(Satellite satellite){
        Optional<ButtonType> result = Alerts.showconfirmation("Confirmation", "Are you sure to delete?");
        if(result.get() == ButtonType.OK){
            if(service == null) throw new IllegalStateException("Service was null");
            try{
                service.remove(satellite);
                updateTableView();
            }
            catch(DbIntegrityException e){
                Alerts.showAlert("Error removing Satellite", null, e.getMessage(), AlertType.ERROR);
            }
        }
    }

    @Override
    public void onDataChange() {
        updateTableView();
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeNodes();
    }

}
