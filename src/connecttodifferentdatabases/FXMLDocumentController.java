/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connecttodifferentdatabases;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
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
import javafx.scene.chart.PieChart;
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
import org.fxmisc.easybind.EasyBind;

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

    List<List<Kolonne>> listOfCombinedColumns = new ArrayList<List<Kolonne>>();
    List<String> listOfColumnNames = new ArrayList<String>();

    private ArrayList<Table> tablesList = new ArrayList<Table>();
    Table tbl = new Table();
    Table tbl2 = new Table();
    Table tbl3 = new Table();
    @FXML
    AnchorPane anchorPane;
    @FXML
    AnchorPane anchorPane2;
       @FXML
    PieChart pieChart; 

    //Map  = new HashMap();
    Map<TreeItem, List> mapOverKolonnerOgTreItems = new HashMap<TreeItem, List>();

    Map<List, Table> tablesAndColumns = new HashMap<List, Table>();

    Tab combinedTab = new Tab();

    private VBox vBox2 = new VBox();

    TreeItem<String> kombinerteKolonnerRoot = new TreeItem<String>("List of combined columns");

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

        makeTableViewWithCombinedColumns(tbl3, tableViewCombined);
        tabPane.getSelectionModel().select(0);
        getPieChartData(0, 1);

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
        createNewCombinedColumn();

    }

    private void createNewCombinedColumn() {
        //metode for å lage en ny kombinert kolonne
        //først lager vi trenoden i treeviewen
        TreeItem treItem = new TreeItem(" ", new ImageView(nodeImage));
        System.out.println(treItem.getGraphic());

        //vi puncher ut en melding og spør brukeren hva han vil kalle den kombinerte kolonnen
        Optional<String> response = Dialogs.create()
                .title("Text Input Dialog")
                .masthead("Look, a Text Input Dialog")
                .message("Please enter your name:")
                .showTextInput("walter");

        //den kombinerte kolonnen skal hete:
        response.ifPresent(name
                -> treItem.setValue(name));

        if (response.isPresent() == false) {
            treItem.setValue("unnamed");

        }

        //noden i treeviewet skal være utvidet
        treItem.expandedProperty().set(true);

        //vi lager en liste(som tilsvarer en kombinert kolonne). Listen skal inneholde kolonner(altså den kombinerte kolonnen skal inneholde hvilke kolonner den skal være)   
        List<Kolonne> combinedColumn = new ArrayList<Kolonne>();
        //vi legger til den nye combinedColumn(den nye kombinerte kolonnen i combinedColumn over kombinerte kolonner
        listOfCombinedColumns.add(combinedColumn);
        listOfColumnNames.add(treItem.getValue().toString());
        //deretter mapper vi den nye kombinerte kolonnen opp mot treitemet, sånn at vi senere kan hente ut den kombinerte kolonnen
        mapOverKolonnerOgTreItems.put(treItem, listOfCombinedColumns.get(listOfCombinedColumns.size() - 1));
        //Deretter putter vi kolonnen i treeviewet for kombinerte kolonner
        kombinerteKolonnerRoot.getChildren().add(treItem);
    }

    private void makeTableViewWithCombinedColumns(Table tbl, TableView tableView) {
        tbl.listofColumns.clear();
        tableView.getItems().clear();
        tableView.getColumns().clear();

        int counter = 0;
        tbl.numberofRows = 0;
        //Først looper vi igjennom combinedColumn av lister med kolonner(med andre ord er en liste i denne combinedColumn en kombinert kolonne)
        for (List<Kolonne> list : listOfCombinedColumns) {
            Collections.sort(list, new ColumnTableComperator());
            int antallRader = 0;

            //deretter looper vi igjennom alle kolonnene i den kombinerte kolonnen for å sjekke hvor mange rader det er
            for (Kolonne kol : list) {

                antallRader += kol.tbl.numberofRows;
            }
            //Hvis det er FLER rader i denne kombinerte kolonnen i noe annet i tbl3, blir dette antall rader. Dette fordi en kolonne kan ha 7 rader, mens en annen 6
            if (antallRader > tbl.numberofRows) {
                tbl.numberofRows
                        = antallRader;
            }

            //Deretter lager vi den kombinerte kolonnen
            tbl.loadCombinedColumns(list, listOfColumnNames.get(counter), tbl);
            counter++;
        }

        //deretter lager vi tableviewet med alle de kombinerte kolonnene. 
        tableView = tbl.fillTableView(tableView, tbl);
    }

    private void createTabPaneWithTable() throws SQLException {
        //Hver gang brukeren kobler til en ny tabell, lager vi en ny tabpane
        //dette for å kunne organisere tabeller og vite hvilken rekkefølge de er i
        VBox vBox = new VBox();

        Table tabellen = new Table();
        String query = textField.getText();

        //her skjer oppkoblingen
        sql_manager.getConnection("localhost", 8889, "eskildb");

        //laster inn dataen med en query
        tabellen.loadData(query, sql_manager, tabellen, tabPaneCounter);

        //legger til den nye tilkoblede tabellen i listen over tilkoblede tabeller
        tablesList.add(tabPaneCounter, tabellen);
        TableView tableViewet = new TableView();
        //legger til tableviewet i tabben
        vBox.getChildren().add(tableViewet);
        tableViewet = tabellen.fillTableView(tableViewet, tabellen);
        vBox.setId("" + tabPaneCounter);

        //lager en ny treeview med en liste over alle kolonnene i tabellen
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

        //spør brukeren hva denne tabpanen skal hete
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

        tabPane.getSelectionModel().select(tabPaneCounter);
    }

    private void addDragAndDrop(TreeCell<String> treeCell) {
        //denne metoden legger til mulighet for drag and drop på treeviews.
        //ved å legge til drag and drop kan brukeren dra kolonner for å lage kombinerte kolonner. 
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
            //brukeren har tatt tak i et treitem og drar det
            @Override
            public void handle(MouseEvent event) {
                System.out.println("setOnDragDetected");

                Dragboard db = treeCell.startDragAndDrop(TransferMode.ANY);

                ClipboardContent content = new ClipboardContent();

                content.putString(event.toString());
                db.setContent(content);
                //Først setter vi hvilket item brukeren har tatt tak i
                DRAGGEDSOURCE = treeCell.getTreeItem();
                //og hvilken index det har
                DRAGGEDINDEX = (treeCell.getTreeView().getSelectionModel().getSelectedIndex());

                event.consume();

            }
        });

        treeCell.setOnDragOver(new EventHandler<DragEvent>() {
            //brukeren har dragget det over et element
            public void handle(DragEvent event) {

                DRAGGEDTARGET = treeCell.getTreeItem();

                if (event.getGestureSource() != treeCell
                        && event.getDragboard().hasString()) {

                    event.acceptTransferModes(TransferMode.COPY_OR_MOVE);

                }

                event.consume();
            }

        });

        treeCell.setOnDragDropped(new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {

                //brukeren har sluppet elementet og vi må sjekke at elementet brukeren drar over faktisk er et treitem
                //dette for å sørge for at brukeren ikke kan slippe tre itemet hvor som helst
                if (DRAGGEDSOURCE != null && DRAGGEDTARGET != null) {
                    if (DRAGGEDTARGET.getGraphic() != null) {
                        System.out.println("hva med her");

                        DRAGGEDTARGET.getChildren().add(DRAGGEDSOURCE);

                        System.out.println("skjer det");

                        int hvilkenTabell = Integer.parseInt(tabPane.getSelectionModel().getSelectedItem().getId());
                        System.out.println(hvilkenTabell);

                        //kaller på den kombinerte kolonnen ved å bruke map og sende inn treitemet
                        //deretter sier vi at vi skal legge til denne kolonnen i den kombinerte kolonnen
                        mapOverKolonnerOgTreItems.get(treeCell.getTreeItem()).add(tablesList.get(hvilkenTabell).listofColumns.get(DRAGGEDINDEX));
                    } else {
                        Dialogs.create()
                                .title("Information Dialog")
                                .masthead(null)
                                .message("This is not a combined column head")
                                .showInformation();

                        treeCell.setText("asd");
                    }
                } else {
                    Dialogs.create()
                            .title("Information Dialog")
                            .masthead(null)
                            .message("You got to drag the column on to a combined column head")
                            .showInformation();

                }

                boolean success = false;

                /* data dropped */
                event.setDropCompleted(success);

                event.consume();

            }

        });
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        treeViewCombined.setRoot(kombinerteKolonnerRoot);
        treeViewCombined.setShowRoot(false);
        makeTreeViewDragAble(treeViewCombined);

        Tab tab2 = new Tab("Combined columns");
        vBox2.getChildren().add(tableViewCombined);
        tab2.setContent(vBox2);
        tabPane.getTabs().add(tab2);

    }
    
    
      protected void getPieChartData(Integer userChosenDataValueColumnIndex, Integer userChosenDataNameColumnIndex) {
         userChosenDataNameColumnIndex = 0;
           userChosenDataValueColumnIndex= 1;
    ObservableList<List<String>> dataen = FXCollections.observableArrayList();
    dataen = tbl3.dataen;
        System.out.println("Kolonne id for data er " + userChosenDataValueColumnIndex + "Kolonne id for name er " + userChosenDataNameColumnIndex);
        ObservableList<PieChart.Data> pieChartData
                = FXCollections.observableArrayList(EasyBind.map(dataen, rowData -> {
                    String name = (String) rowData.get(0);
                    int value = Integer.parseInt(rowData.get(1));
                    return new PieChart.Data(name, value);
                }));
        System.out.println("aa " + pieChartData.get(0));
        pieChart.setData(pieChartData);

        
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

      
    
}
