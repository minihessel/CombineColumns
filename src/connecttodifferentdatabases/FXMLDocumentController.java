/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connecttodifferentdatabases;

import java.io.InputStream;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import org.controlsfx.dialog.Dialogs;

/**
 *
 * @author Eskil Hesselroth
 */
public class FXMLDocumentController implements Initializable {

    private ArrayList<TreeView> listemedView = new ArrayList<TreeView>();


    private ArrayList<VBox> vBoxes = new ArrayList<VBox>();
    public int tabPaneCounter = 0;

    TreeItem<String> duckRoot = new TreeItem<String>("Root");

    @FXML
    private TextField textField;

    @FXML
    private TreeView treeViewCombined;

    @FXML
    private TabPane tabPane;

    private static TreeItem DRAGGEDSOURCE;
    private static TreeItem DRAGGEDTARGET;
    private static int DRAGGEDINDEX;

    List<Kolonne> listofCombined = new ArrayList<>();

    TreeItem<String> rootNode
            = new TreeItem<String>("New Table with combined");

    @FXML
    TableView tableViewCombined;
    SQL_manager sql_manager = new SQL_manager();

    List<TableColumn> lstClm = new ArrayList<TableColumn>();

    ObservableList<List<String>> dataForMYSQL = FXCollections.observableArrayList();
    ObservableList<List<String>> dataforOracle = FXCollections.observableArrayList();
    ObservableList<List<String>> dataCombined = FXCollections.observableArrayList();

    List<List<Kolonne>> ListOfLists = new ArrayList<List<Kolonne>>();
    List<String> listOfColumnNames = new ArrayList<String>();

    private ArrayList<Table> tablesList = new ArrayList<Table>();
    Table tbl = new Table();
    Table tbl2 = new Table();
    Table tbl3 = new Table();
    @FXML
    AnchorPane anchorPane;
    @FXML
    AnchorPane anchorPane2;

    //Map  = new HashMap();
    Map<TreeItem, List> vehicles = new HashMap<TreeItem, List>();
    
        Map<List, Table> tablesAndColumns = new HashMap<List, Table>();

    Tab combinedTab = new Tab();

    private VBox vBox2 = new VBox();

    TreeItem<String> newRoot = new TreeItem<String>("List of combined columns");
    private final Node rootIcon = new ImageView(new Image(getClass().getResourceAsStream("root.png")));

    Image nodeImage = new Image(
            getClass().getResourceAsStream("root.png"));

    @FXML
    private void handleGroup1(ActionEvent event) throws SQLException {

    }

    @FXML
    private void handleGroup2(ActionEvent event) throws SQLException {

    }

    //For combining
    @FXML
    private void handleButtonAction(ActionEvent event) throws SQLException {

        tbl3.listofColumns.clear();
        tableViewCombined.getItems().clear();
        tableViewCombined.getColumns().clear();

        int counter = 0;
        for (List<Kolonne> list : ListOfLists) {
            Table whichTable = tablesAndColumns.get(list);
          

            tbl3.loadCombinedColumns(list, listOfColumnNames.get(counter),  tablesList.size());
            counter++;
        }

        tableViewCombined = tbl3.makeTableView(tableViewCombined);

    }

    @FXML
    private void handleButtonNewGroup(ActionEvent event) throws SQLException {
        createTabPaneWithTable();
    }

    @FXML
    private void handleButtonAction4(ActionEvent event) throws SQLException {

    }

    @FXML
    private void handleCreateNewCombined(ActionEvent event) throws SQLException {
        TreeItem treItem = new TreeItem(" ", new ImageView(nodeImage));
        System.out.println(treItem.getGraphic());

        Optional<String> response = Dialogs.create()
                .title("Text Input Dialog")
                .masthead("Look, a Text Input Dialog")
                .message("Please enter your name:")
                .showTextInput("walter");

        final String navnetPaaKolonnen;
// The Java 8 way to get the response value (with lambda expression).
        response.ifPresent(name
                -> treItem.setValue(name));

        if (response.isPresent() == false) {
            treItem.setValue("unnamed");

        }

        treItem.expandedProperty().set(true);

        List<Kolonne> listen = new ArrayList<Kolonne>();
        ListOfLists.add(listen);
        listOfColumnNames.add(treItem.getValue().toString());
        vehicles.put(treItem, ListOfLists.get(ListOfLists.size() - 1));
  
   
        System.out.println("HER " + ListOfLists.get(treeViewCombined.getRoot().getChildren().size()));

        newRoot.getChildren().add(treItem);

    }

    private void createTabPaneWithTable() throws SQLException {
        VBox vBox = new VBox();

        Table tabellen = new Table();
        String query = textField.getText();
        sql_manager.getConnection("localhost", 8889, "eskildb");

        tabellen.loadData(query, sql_manager);

        tablesList.add(tabPaneCounter, tabellen);
        TableView tableViewet = new TableView();
        vBox.getChildren().add(tableViewet);
        tableViewet = tabellen.makeTableView(tableViewet);
        vBox.setId("" + tabPaneCounter);

        MenuItem menuItem = new MenuItem("GRUPPE");

        TreeView treeView = new TreeView();

        TreeItem<String> treeView2Root = new TreeItem<String>("MYSQL");

        for (Kolonne kol : tabellen.listofColumns) {
            treeView2Root.getChildren().add(new TreeItem<>(kol.NAVN));
        }
        treeView.setRoot(treeView2Root);
        treeView.setShowRoot(false);
        makeTreeViewDragAble(treeView);
        listemedView.add(treeView);

        Tab tab = new Tab();

        Optional<String> response = Dialogs.create()
                .title("Text Input Dialog")
                .masthead("Look, a Text Input Dialog")
                .message("Please enter your name:")
                .showTextInput("walter");

// The Java 8 way to get the response value (with lambda expression).
        response.ifPresent(name
                -> tab.setText(name));

        if (response.isPresent() == false) {
            tab.setText("Unnamed");
        }
        treeView2Root.setValue(tab.getText());
        tabPane.getTabs().add(tab);

        tab.setContent(vBox);
        tab.setId("" + tabPaneCounter);
        vBox.getChildren().add(treeView);
        tabPaneCounter++;
    }

    private void addDragAndDrop(TreeCell<String> treeCell) {

        /*    treeCell.setOnMouseClicked(new EventHandler<MouseEvent>(){
         @Override
         public void handle(MouseEvent event) {
         treeView.setEditable(true);
         treeView.edit(treeCell.getTreeItem());
            
         }
            
             
         });**/
        treeCell.setOnMouseClicked(new EventHandler<MouseEvent>() {

            int clickCount = 0;

            @Override
            public void handle(MouseEvent event) {

                if (clickCount >= 2 && treeCell.getTreeItem().valueProperty().get() == "ColumnTrue") {
                    Optional<String> response = Dialogs.create()
                            .title("Text Input Dialog")
                            .masthead("Look, a Text Input Dialog")
                            .message("Please enter your name:")
                            .showTextInput("walter");

                    treeCell.setText("asd");

// The Java 8 way to get the response value (with lambda expression).
                    response.ifPresent(name
                            -> treeCell.getTreeItem().setValue(name));
                }
                clickCount++;
            }
        });

        treeCell.setOnDragDetected(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println("setOnDragDetected");

                Dragboard db = treeCell.startDragAndDrop(TransferMode.ANY);

                ClipboardContent content = new ClipboardContent();

                content.putString(event.toString());
                db.setContent(content);
                DRAGGEDSOURCE = treeCell.getTreeItem();
                DRAGGEDINDEX = (treeCell.getTreeView().getSelectionModel().getSelectedIndex());

                event.consume();

            }
        });

        treeCell.setOnDragOver(new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {

                DRAGGEDTARGET = treeCell.getTreeItem();
                if (event.getGestureSource() != treeCell
                        && event.getDragboard().hasString()) {

                    System.out.println("DU ER OVER");

                    event.acceptTransferModes(TransferMode.COPY_OR_MOVE);

                }

                event.consume();
            }

        });

        treeCell.setOnDragDropped(new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {

                /* if there is a string data on dragboard, read it and use it */
                //Dragboard db = event.getDragboard();
                if (DRAGGEDSOURCE != null && DRAGGEDTARGET != null) {

                    System.out.println("hva med her");

                    // treeCell.getTreeView()
                    DRAGGEDTARGET.getChildren().add(DRAGGEDSOURCE);

                    //   System.out.println(treeCell.getTreeItem());
                    System.out.println("skjer det");

                    int hvilkenTabell = Integer.parseInt(tabPane.getSelectionModel().getSelectedItem().getId());
                    System.out.println(hvilkenTabell);
                    
                    vehicles.get(treeCell.getTreeItem()).add(tablesList.get(hvilkenTabell).listofColumns.get(DRAGGEDINDEX));
                  
                 tablesAndColumns.put(vehicles.get(treeCell.getTreeItem()), tablesList.get(hvilkenTabell));
             

                }

                boolean success = false;

                /* data dropped */
                event.setDropCompleted(success);

                event.consume();

              //  listofCombined.add(tablesList.get(Integer.parseInt(tabPane.getSelectionModel().getSelectedItem().getId())).listofColumns.get(DRAGGEDINDEX-1));
                    //  rootNode2.getChildren().add(DRAGGEDSOURCE);
                // if (db.hasString()) {
                //     target.setText(db.getString());
                //  success = true;
                //  }
                        /* let the source know whether the string was successfully
                 * transferred and used */
                // treeView2.getRoot().getChildren().add(DRAGGEDTARGET);
            }

        });
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        treeViewCombined.setRoot(newRoot);
        treeViewCombined.setShowRoot(false);
        makeTreeViewDragAble(treeViewCombined);

        Tab tab2 = new Tab("Combine columns");
        tab2.setContent(vBox2);
        tabPane.getTabs().add(tab2);

    }

    private void makeTreeViewDragAble(TreeView treeView) {

        treeView.setCellFactory(new Callback<TreeView<String>, TreeCell<String>>() {
            @Override
            public TreeCell<String> call(TreeView<String> stringTreeView) {
                TreeCell<String> treeCell = new TreeCell<String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (!empty && item != null) {
                            setText(item);
                            setGraphic(getTreeItem().getGraphic());

                        } else {
                            setText(null);
                            setGraphic(null);
                        }
                    }
                };

                addDragAndDrop(treeCell);
                treeView.setEditable(true);
                return treeCell;
            }
        });
    }

    static class ImageImpl extends Image {

        public ImageImpl(InputStream resourceAsStream) {
            super(resourceAsStream);
        }
    }

}
