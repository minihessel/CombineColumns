/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connecttodifferentdatabases;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.PropertyValueFactory;
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
    private ListView listView;
    @FXML
    private ListView listView2;

    @FXML
    private TextField textField;

    @FXML
    private TreeView treeView;

    @FXML
    TableView mysqlTableView;
    @FXML
    TableView oracleTableView;
    @FXML
    TableView tableViewCombined;
    SQL_manager sql_manager = new SQL_manager();

    List<TableColumn> lstClm = new ArrayList<TableColumn>();

    ObservableList<List<String>> dataForMYSQL = FXCollections.observableArrayList();
    ObservableList<List<String>> dataforOracle = FXCollections.observableArrayList();
    ObservableList<List<String>> dataCombined = FXCollections.observableArrayList();
    Table tbl = new Table();
    Table tbl2 = new Table();
    
    @FXML
    AnchorPane anchorPane;
    
    @FXML
    private void handleButtonAction(ActionEvent event) throws SQLException {
        sql_manager.getConnection("localhost", 8889, "mysql");
        getDataForSelectedTable("test", mysqlTableView, dataForMYSQL, "select * from test",tbl);
           getDataForSelectedTable("test",oracleTableView, dataForMYSQL, "select * from test",tbl2);
    }

    @FXML
    private void handleButtonAction2(ActionEvent event) throws SQLException {
           sql_manager.getConnection("localhost", 8889, "mysql");
           tableViewCombined.getColumns().clear();
          tbl.loadData("select * from test", sql_manager);
          System.out.println("Her kommer tbl");
          System.out.println(tbl.storString());
          tableViewCombined.getItems().clear();
          tableViewCombined = tbl.makeTableView(tableViewCombined);
     
            
   
            
          
          
    }

    @FXML
    private void handleButtonAction3(ActionEvent event) throws SQLException {
  
    }

    @FXML
    private void handleButtonAction4(ActionEvent event) throws SQLException {

   
     

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        TableColumn oracleName = new TableColumn("oracleName");
        oracleName.setCellValueFactory(
                new PropertyValueFactory<TableNames, String>("oracleName"));

        TableColumn mysqlName = new TableColumn("mysqlName");
        mysqlName.setCellValueFactory(
                new PropertyValueFactory<TableNames, String>("mysqlName"));

        TableColumn mssqlName = new TableColumn("mssqlName");
        mssqlName.setCellValueFactory(
                new PropertyValueFactory<TableNames, String>("mssqlName"));

        tableViewCombined.getColumns().addAll(oracleName, mysqlName, mssqlName);
        tableViewCombined.setItems(dataCombined);

    }

    private void getDataForSelectedTable(String tableName, TableView tableView, ObservableList data, String SQL, Table table ) throws SQLException {
    
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
