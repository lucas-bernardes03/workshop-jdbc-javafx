package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.util.Callback;
import model.db.DbException;
import model.entities.Planet;
import model.entities.Satellite;
import model.exceptions.ValidationException;
import model.services.PlanetService;
import model.services.SatelliteService;

public class SatelliteFormController implements Initializable {
    private Satellite satellite;
    private SatelliteService service;
    private PlanetService planetService;
    private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
    
    @FXML
    private TextField txtId;
    @FXML
    private TextField txtName;
    @FXML
    private ComboBox<Planet> comboBoxPlanet;

    @FXML
    private Label labelErrorName;

    @FXML
    private Button btSave;
    @FXML
    private Button btCancel;

    private ObservableList<Planet> obsList;

    @FXML
    public void onBtSaveAction(ActionEvent event){
        if(satellite == null) throw new IllegalStateException("Satellite was null.");
        if(service == null) throw new IllegalStateException("Service was null.");
        try{
            satellite = getFormData();
            service.saveOrUpdate(satellite);
            notifyDataChangeListeners();
            Utils.currentStage(event).close();
        }
        catch(DbException e){
            Alerts.showAlert("Error saving object", null, e.getMessage(), AlertType.ERROR);
        }
        catch(ValidationException e){
            setErrorMessages(e.getErrors());
        }
    }

    @FXML
    public void onBtCancelAction(ActionEvent event){
        Utils.currentStage(event).close();
    }

    public void subscribeDataChangeListener(DataChangeListener listener){
        dataChangeListeners.add(listener);
    }

    public void notifyDataChangeListeners(){
        for(DataChangeListener l : dataChangeListeners) l.onDataChange();
    }

    public void setSatellite(Satellite satellite){
        this.satellite = satellite;
    }

    public void setSatelliteServices(SatelliteService service, PlanetService planetService){
        this.service = service;
        this.planetService = planetService;
    }

    private Satellite getFormData(){
        Satellite tmpSatellite = new Satellite();
        ValidationException exception = new ValidationException("Validation error");

        tmpSatellite.setId(Utils.tryParseToInt(txtId.getText()));

        if(txtName.getText() == null || txtName.getText().trim().equals("")){
            exception.addError("SatelliteName", "Field cannot be empty");
        }
        tmpSatellite.setName(txtName.getText());

        tmpSatellite.setPlanet(comboBoxPlanet.getValue());

        if(exception.getErrors().size() > 0) throw exception;

        return tmpSatellite;
    }

    public void updateFormData(){
        if(satellite == null) throw new IllegalStateException("Satellite was null.");
        txtId.setText(String.valueOf(satellite.getId()));
        txtName.setText(satellite.getName());
        if(satellite.getPlanet() == null) comboBoxPlanet.getSelectionModel().selectFirst();
        else comboBoxPlanet.setValue(satellite.getPlanet());
    }

    private void setErrorMessages(Map<String,String> errors){
        Set<String> fields = errors.keySet();
        labelErrorName.setText(fields.contains("SatelliteName") ? errors.get("SatelliteName"):"");
    }

    public void loadAssociatedObjects(){
        if(planetService == null) throw new IllegalStateException("Service was null");
        List<Planet> list = planetService.findAll();
        obsList = FXCollections.observableArrayList(list);
        comboBoxPlanet.setItems(obsList);
    }
    
    private void initializeComboBoxPlanet(){
        Callback<ListView<Planet>,ListCell<Planet>> factory = lv -> new ListCell<Planet>(){
            @Override
            protected void updateItem(Planet planet, boolean empty){
                super.updateItem(planet, empty);
                setText(empty ? "" : planet.getName());
            }
        };
        comboBoxPlanet.setCellFactory(factory);
        comboBoxPlanet.setButtonCell(factory.call(null));
    }

    private void initializeNodes(){
        Constraints.setTextFieldInteger(txtId);
        Constraints.setTextFieldMaxLength(txtName, 30);
        initializeComboBoxPlanet();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeNodes();
    }

    
}
