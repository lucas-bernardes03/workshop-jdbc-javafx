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
import model.entities.Planet;
import model.services.PlanetService;
import model.services.StarService;

public class PlanetListController implements Initializable, DataChangeListener {
    private PlanetService service;
    
    @FXML
    private TableView<Planet> tableViewPlanet;
    @FXML
    private TableColumn<Planet,Integer> tableColumnId;
    @FXML
    private TableColumn<Planet,String> tableColumnName;
    @FXML
    private TableColumn<Planet,String> tableColumnType;
    @FXML
    private TableColumn<Planet,Double> tableColumnDiameter;
    @FXML
    private TableColumn<Planet,Double> tableColumnMass;
    @FXML
    private TableColumn<Planet,Double> tableColumnGravity;
    @FXML
    private TableColumn<Planet,Double> tableColumnOrbitalSpeed;
    @FXML
    private TableColumn<Planet,Planet> tableColumnEdit;
    @FXML
    private TableColumn<Planet,Planet> tableColumnRemove;
    @FXML
    private Button btNewPlanet;
    
    private ObservableList<Planet> obsList;

    @FXML
    public void onBtNewPlanetAction(ActionEvent event){
        Stage parentStage = Utils.currentStage(event);
        Planet planet = new Planet();
        createDialogForm(planet, parentStage, "/gui/PlanetForm.fxml");
    }

    public void setPlanetService(PlanetService service){
        this.service = service;
    }

    private void initializeNodes() {
        tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tableColumnType.setCellValueFactory(new PropertyValueFactory<>("type"));
        tableColumnDiameter.setCellValueFactory(new PropertyValueFactory<>("diameter"));
        Utils.formatTableColumnDouble(tableColumnDiameter, 0);
        tableColumnMass.setCellValueFactory(new PropertyValueFactory<>("mass"));
        Utils.formatTableColumnDouble(tableColumnMass, 3);
        tableColumnGravity.setCellValueFactory(new PropertyValueFactory<>("gravity"));
        Utils.formatTableColumnDouble(tableColumnGravity, 2);
        tableColumnOrbitalSpeed.setCellValueFactory(new PropertyValueFactory<>("orbitalSpeed"));
        Utils.formatTableColumnDouble(tableColumnOrbitalSpeed, 2);

        Stage stage = (Stage) App.getMainScene().getWindow();
        tableViewPlanet.prefHeightProperty().bind(stage.heightProperty());
    }
    
    public void updateTableView(){
        if(service == null) throw new IllegalStateException("Servce was null");
        List<Planet> list = service.findAll();
        obsList = FXCollections.observableArrayList(list);
        tableViewPlanet.setItems(obsList);
        initEditButtons();
        initRemoveButtons();
    }

    
    private void createDialogForm(Planet planet, Stage parentStage, String absoluteName){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
            Pane pane = loader.load();

            PlanetFormController controller = loader.getController();
            controller.setPlanet(planet);
            controller.setServices(new PlanetService(), new StarService());
            controller.loadAssociatedObjects();
            controller.subscribeDataChangeListener(this);
            controller.updateFormData();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Enter Planet data");
            dialogStage.setScene(new Scene(pane));
            dialogStage.setResizable(false);
            dialogStage.initOwner(parentStage);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.showAndWait();

        }
        catch(IOException e){
            e.printStackTrace();
            Alerts.showAlert("IO Exception", "Error loading layout", e.getMessage(), AlertType.ERROR);
        }
    }
    
    private void initEditButtons(){
        tableColumnEdit.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        tableColumnEdit.setCellFactory(param -> new TableCell<Planet,Planet>(){
            private final Button bt = new Button("Edit");

            @Override
            protected void updateItem(Planet planet, boolean empty){
                super.updateItem(planet, empty);
                if(planet == null){
                    setGraphic(null);
                    return;
                }
                
                setGraphic(bt);
                bt.setOnAction(event -> createDialogForm(planet, Utils.currentStage(event), "/gui/PlanetForm.fxml"));
            }
        });
    }

    private void initRemoveButtons(){
        tableColumnRemove.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        tableColumnRemove.setCellFactory(param -> new TableCell<Planet,Planet>(){
            private final Button bt = new Button("Remove");

            @Override
            protected void updateItem(Planet planet, boolean empty){
                super.updateItem(planet, empty);
                if(planet == null){
                    setGraphic(null);
                    return;
                }
                setGraphic(bt);
                bt.setOnAction(event -> removePlanet(planet));
            }
        });
    }

    private void removePlanet(Planet planet){
        Optional<ButtonType> result = Alerts.showconfirmation("Confirmation", "Are you sure to delete?");
        if(result.get() == ButtonType.OK){
            if(service == null) throw new IllegalStateException("Service was null");
            try{
                service.remove(planet);
                updateTableView();
            }
            catch(DbIntegrityException e){
                Alerts.showAlert("Error removing Planet", null, e.getMessage(), AlertType.ERROR);
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
