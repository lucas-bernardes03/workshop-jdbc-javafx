package gui;

import java.net.URL;
import java.util.ResourceBundle;

import gui.util.Constraints;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Star;

public class StarFormController implements Initializable {
    private Star star;
    
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
    public void onBtSaveAction(){

    }

    @FXML
    public void onBtCancelAction(){

    }

    public void setStar(Star star){
        this.star = star;
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
