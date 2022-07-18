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
    private TableColumn<Star,Star> tableColumnEdit;
    @FXML
    private TableColumn<Star,Star> tableColumnRemove;
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
        initEditButtons();
        initRemoveButtons();
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
            e.printStackTrace();
            Alerts.showAlert("IO Exception", "Error loading layout", e.getMessage(), AlertType.ERROR);
        }
    }
    
    private void initEditButtons(){
        tableColumnEdit.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        tableColumnEdit.setCellFactory(param -> new TableCell<Star,Star>(){
            private final Button bt = new Button("Edit");

            @Override
            protected void updateItem(Star star, boolean empty){
                super.updateItem(star, empty);
                if(star == null){
                    setGraphic(null);
                    return;
                }
                
                setGraphic(bt);
                bt.setOnAction(event -> createDialogForm(star, Utils.currentStage(event), "/gui/StarForm.fxml"));
            }
        });
    }

    private void initRemoveButtons(){
        tableColumnRemove.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        tableColumnRemove.setCellFactory(param -> new TableCell<Star,Star>(){
            private final Button bt = new Button("Remove");

            @Override
            protected void updateItem(Star star, boolean empty){
                super.updateItem(star, empty);
                if(star == null){
                    setGraphic(null);
                    return;
                }
                setGraphic(bt);
                bt.setOnAction(event -> removeStar(star));
            }
        });
    }

    private void removeStar(Star star){
        Optional<ButtonType> result = Alerts.showconfirmation("Confirmation", "Are you sure to delete?");
        if(result.get() == ButtonType.OK){
            if(service == null) throw new IllegalStateException("Service was null");
            try{
                service.remove(star);
                updateTableView();
            }
            catch(DbIntegrityException e){
                Alerts.showAlert("Error removing Star", null, e.getMessage(), AlertType.ERROR);
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
