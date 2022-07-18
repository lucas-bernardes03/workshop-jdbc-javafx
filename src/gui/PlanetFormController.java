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
import model.entities.Star;
import model.exceptions.ValidationException;
import model.services.PlanetService;
import model.services.StarService;

public class PlanetFormController implements Initializable {
    private Planet planet;
    private PlanetService service;
    private StarService starService;
    private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
    
    @FXML
    private TextField txtId;
    @FXML
    private TextField txtName;
    @FXML
    private TextField txtType;
    @FXML
    private TextField txtDiameter;
    @FXML
    private TextField txtMass;
    @FXML
    private TextField txtGravity;
    @FXML
    private TextField txtOrbitalSpeed;
    @FXML
    private ComboBox<Star> comboBoxStar;

    @FXML
    private Label labelErrorName;
    @FXML
    private Label labelErrorType;
    @FXML
    private Label labelErrorDiameter;
    @FXML
    private Label labelErrorMass;
    @FXML
    private Label labelErrorGravity;
    @FXML
    private Label labelErrorOrbitalSpeed;

    @FXML
    private Button btSave;
    @FXML
    private Button btCancel;

    private ObservableList<Star> obsList;

    @FXML
    public void onBtSaveAction(ActionEvent event){
        if(planet == null) throw new IllegalStateException("Planet was null.");
        if(service == null) throw new IllegalStateException("Service was null.");
        try{
            planet = getFormData();
            service.saveOrUpdate(planet);
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

    public void setPlanet(Planet planet){
        this.planet = planet;
    }

    public void setServices(PlanetService service, StarService starService){
        this.service = service;
        this.starService = starService;
    }

    private Planet getFormData(){
        Planet tmpPlanet = new Planet();
        ValidationException exception = new ValidationException("Validation error");

        tmpPlanet.setId(Utils.tryParseToInt(txtId.getText()));

        if(txtName.getText() == null || txtName.getText().trim().equals("")){
            exception.addError("PlanetName", "Field cannot be empty");
        }
        tmpPlanet.setName(txtName.getText());

        if(txtType.getText() == null || txtType.getText().trim().equals("")){
            exception.addError("PlanetType", "Field cannot be empty");
        }
        tmpPlanet.setType(txtType.getText());

        if(txtDiameter.getText() == null || txtDiameter.getText().trim().equals("")){
            exception.addError("PlanetDiameter", "Field cannot be empty");
        }
        tmpPlanet.setDiameter(Utils.tryParseToDouble(txtDiameter.getText()));

        if(txtMass.getText() == null || txtMass.getText().trim().equals("")){
            exception.addError("PlanetMass", "Field cannot be empty");
        }
        tmpPlanet.setMass(Utils.tryParseToDouble(txtMass.getText()));

        if(txtGravity.getText() == null || txtGravity.getText().trim().equals("")){
            exception.addError("PlanetGravity", "Field cannot be empty");
        }
        tmpPlanet.setGravity(Utils.tryParseToDouble(txtGravity.getText()));

        if(txtOrbitalSpeed.getText() == null || txtOrbitalSpeed.getText().trim().equals("")){
            exception.addError("PlanetOrbitalSpeed", "Field cannot be empty");
        }
        tmpPlanet.setOrbitalSpeed(Utils.tryParseToDouble(txtOrbitalSpeed.getText()));

        tmpPlanet.setStar(comboBoxStar.getValue());

        if(exception.getErrors().size() > 0) throw exception;

        return tmpPlanet;
    }

    public void updateFormData(){
        if(planet == null) throw new IllegalStateException("Planet was null.");
        txtId.setText(String.valueOf(planet.getId()));
        txtName.setText(planet.getName());
        txtType.setText(planet.getType());
        txtDiameter.setText(String.valueOf(planet.getDiameter()));
        txtMass.setText(String.valueOf(planet.getMass()));
        txtGravity.setText(String.valueOf(planet.getGravity()));
        txtOrbitalSpeed.setText(String.valueOf(planet.getOrbitalSpeed()));
        if(planet.getStar() == null) comboBoxStar.getSelectionModel().selectFirst();
        else comboBoxStar.setValue(planet.getStar());
    }

    private void setErrorMessages(Map<String,String> errors){
        Set<String> fields = errors.keySet();
        labelErrorName.setText(fields.contains("PlanetName") ? errors.get("PlanetName"):"");
        labelErrorType.setText(fields.contains("PlanetType") ? errors.get("PlanetType"):"");
        labelErrorDiameter.setText(fields.contains("PlanetDiameter") ? errors.get("PlanetDiameter"):"");
        labelErrorMass.setText(fields.contains("PlanetMass") ? errors.get("PlanetMass"):"");
        labelErrorGravity.setText(fields.contains("PlanetGravity") ? errors.get("PlanetGravity"):"");
        labelErrorOrbitalSpeed.setText(fields.contains("PlanetOrbitalSpeed") ? errors.get("PlanetOrbitalSpeed"):"");
    }

    public void loadAssociatedObjects(){
        if(starService == null) throw new IllegalStateException("Service was null");
        List<Star> list = starService.findAll();
        obsList = FXCollections.observableArrayList(list);
        comboBoxStar.setItems(obsList);
    }

    private void initializeComboBoxStar(){
        Callback<ListView<Star>,ListCell<Star>> factory = lv -> new ListCell<Star>(){
            @Override
            protected void updateItem(Star star, boolean empty){
                super.updateItem(star, empty);
                setText(empty ? "" : star.getName());
            }
        };
        comboBoxStar.setCellFactory(factory);
        comboBoxStar.setButtonCell(factory.call(null));
    }

    private void initializeNodes(){
        Constraints.setTextFieldInteger(txtId);
        Constraints.setTextFieldMaxLength(txtName, 30);
        Constraints.setTextFieldMaxLength(txtType, 20);
        Constraints.setTextFieldDouble(txtDiameter);
        Constraints.setTextFieldDouble(txtMass);
        Constraints.setTextFieldDouble(txtGravity);
        Constraints.setTextFieldDouble(txtOrbitalSpeed);
        initializeComboBoxStar();
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeNodes();
    }

    
}
