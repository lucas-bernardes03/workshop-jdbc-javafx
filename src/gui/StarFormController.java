package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

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
import model.entities.Star;
import model.services.StarService;

public class StarFormController implements Initializable {
    private Star star;
    private StarService service;
    private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
    
    @FXML
    private TextField txtId;
    @FXML
    private TextField txtName;
    @FXML
    private TextField txtStellarClass;
    @FXML
    private TextField txtMass;

    @FXML
    private Label labelErrorName;
    @FXML
    private Label labelErrorStellarClass;
    @FXML
    private Label labelErrorMass;

    @FXML
    private Button btSave;
    @FXML
    private Button btCancel;

    @FXML
    public void onBtSaveAction(ActionEvent event){
        if(star == null) throw new IllegalStateException("Star was null.");
        if(service == null) throw new IllegalStateException("Service was null.");
        try{
            star = getFormData();
            service.saveOrUpdate(star);
            notifyDataChangeListeners();
            Utils.currentStage(event).close();
        }
        catch(DbException e){
            Alerts.showAlert("Error saving object", null, e.getMessage(), AlertType.ERROR);
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

    public void setStar(Star star){
        this.star = star;
    }

    public void setStarService(StarService service){
        this.service = service;
    }

    private Star getFormData(){
        Star tmpStar = new Star();
        tmpStar.setId(Utils.tryParseToInt(txtId.getText()));
        tmpStar.setName(txtName.getText());
        tmpStar.setStellarClass(txtStellarClass.getText());
        tmpStar.setMass(Utils.tryParseToDouble(txtMass.getText()));
        return tmpStar;
    }

    public void updateFormData(){
        if(star == null) throw new IllegalStateException("Star was null.");
        txtId.setText(String.valueOf(star.getId()));
        txtName.setText(star.getName());
        txtStellarClass.setText(star.getStellarClass());
        txtMass.setText(String.valueOf(star.getMass()));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeNodes();
    }

    private void initializeNodes(){
        Constraints.setTextFieldInteger(txtId);
        Constraints.setTextFieldMaxLength(txtName, 30);
        Constraints.setTextFieldMaxLength(txtStellarClass, 15);
        Constraints.setTextFieldDouble(txtMass);
    }
    
}
