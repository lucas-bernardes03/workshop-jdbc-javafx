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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import model.db.DbException;
import model.entities.Planet;
import model.exceptions.ValidationException;
import model.services.PlanetService;

public class PlanetFormController implements Initializable {
    private Planet planet;
    private PlanetService service;
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

    public void setPlanetService(PlanetService service){
        this.service = service;
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
    }

    private void setErrorMessages(Map<String,String> errors){
        Set<String> fields = errors.keySet();
        if(fields.contains("PlanetName")) labelErrorName.setText(errors.get("PlanetName"));
        if(fields.contains("PlanetType")) labelErrorType.setText(errors.get("PlanetType"));
        if(fields.contains("PlanetDiameter")) labelErrorDiameter.setText(errors.get("PlanetDiameter"));
        if(fields.contains("PlanetMass")) labelErrorMass.setText(errors.get("PlanetMass"));
        if(fields.contains("PlanetGravity")) labelErrorGravity.setText(errors.get("PlanetGravity"));
        if(fields.contains("PlanetOrbitalSpeed")) labelErrorOrbitalSpeed.setText(errors.get("PlanetOrbitalSpeed"));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeNodes();
    }

    private void initializeNodes(){
        Constraints.setTextFieldInteger(txtId);
        Constraints.setTextFieldMaxLength(txtName, 30);
        Constraints.setTextFieldMaxLength(txtType, 20);
        Constraints.setTextFieldDouble(txtDiameter);
        Constraints.setTextFieldDouble(txtMass);
        Constraints.setTextFieldDouble(txtGravity);
        Constraints.setTextFieldDouble(txtOrbitalSpeed);
    }
    
}
