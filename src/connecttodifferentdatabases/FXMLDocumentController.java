/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connecttodifferentdatabases;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author Eskil Hesselroth
 */
public class FXMLDocumentController implements Initializable {

    TreeItem<String> duckRoot = new TreeItem<String>("Root");
    @FXML
    private Label label;
    @FXML
    private ListView listViewTB1;
    @FXML
    private ListView listViewTB2;

    @FXML
    private TextField textField;

    @FXML
    private TreeView treeView;

    @FXML
    TableView tableView1;
    @FXML
    TableView tableView2;
    @FXML
    TableView tableViewCombined;
    SQL_manager sql_manager = new SQL_manager();

    List<TableColumn> lstClm = new ArrayList<TableColumn>();

    ObservableList<List<String>> dataForMYSQL = FXCollections.observableArrayList();
    ObservableList<List<String>> dataforOracle = FXCollections.observableArrayList();
    ObservableList<List<String>> dataCombined = FXCollections.observableArrayList();
    Table tbl = new Table();
    Table tbl2 = new Table();
    Table tbl3 = new Table();
    @FXML
    AnchorPane anchorPane;

    @FXML
    private void handleButtonAction(ActionEvent event) throws SQLException {
       
     //   mysqlTableView = tbl2.makeTableView(mysqlTableView);

        List<Kolonne> listofCombined = new ArrayList<>();
        int valgEn = Integer.parseInt((listViewTB1.getSelectionModel().getSelectedItem().toString()));
        int valgTo = Integer.parseInt((listViewTB2.getSelectionModel().getSelectedItem().toString()));
        
        listofCombined.add(tbl.listofColumns.get(valgEn)); 
       listofCombined.add(tbl2.listofColumns.get(valgTo));
        tbl3.loadCombinedColumns(listofCombined);
        tableViewCombined = tbl3.makeTableView(tableViewCombined);
    }

    @FXML
    private void handleButtonAction1(ActionEvent event) throws SQLException {
        sql_manager.getConnection("localhost", 8889, "eskildb");
        tableView1.getColumns().clear();
        tbl.loadData("select * from test", sql_manager);
        tableView1 = tbl.makeTableView(tableView1);

        for (Kolonne kol : tbl.listofColumns) {
            listViewTB1.getItems().add(kol.KOLONNEINDEX);

        }
            

   
    
        
   
            
        
    
}
        
      

    

     
    
    @FXML
    private void handleButtonAction2(ActionEvent event) throws SQLException {
        sql_manager.getConnection("localhost", 8889, "eskilDB");
     //   tableView2.getColumns().clear();
        tbl2.loadData("select * from test1", sql_manager);
        tableView2 = tbl2.makeTableView(tableView2);

        for (Kolonne kol : tbl2.listofColumns) {
            listViewTB2.getItems().add(kol.KOLONNEINDEX);

        }

    }

    @FXML
    private void handleButtonAction4(ActionEvent event) throws SQLException {

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    private void getDataForSelectedTable(String tableName, TableView tableView, ObservableList data, String SQL, Table table) throws SQLException {

    }

    private void addCheckBoxesToColumns(TableColumn col) {
        EventHandler<ActionEvent> handler = new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                ComboBox cb = (ComboBox) event.getSource();
                TableColumn column = (TableColumn) cb.getUserData();

                if (cb.getValue() == " ") {
                    lstClm.remove(column);
                } else {
                    lstClm.remove(column);
                    column.setUserData(cb.getValue().toString());
                    System.out.println("kanskje her? " + column.getUserData());
                    System.out.println(column + " " + column.getId());

                    lstClm.add(column);

                }

            }
        };
        ComboBox cb = new ComboBox();
        cb.getItems().add("Name");
        cb.getItems().add("Value");
        cb.getItems().add(" ");
        cb.setUserData(col);
        cb.setOnAction(handler);
        col.setGraphic(cb);

    }

}
