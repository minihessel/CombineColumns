package connecttodifferentdatabases;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;

/**
 *
 * @author Eskil Hesselroth
 */
public class Table {

    //ArrayList<Rader> rows = FXCollections.observableArrayList();
    ArrayList<Kolonne> listofColumns;
    int tableNumber;
    int numberofRows;

    public Table() {
        listofColumns = new ArrayList<>();
        

    }

    /**
     * Laster inn data fra angitt tabell fra angitt db. Bruker har valgt hvilken tabell - vi laster inn kolonner og rader i tableview
     */
    public void loadData(String SQL, SQL_manager sql_manager,Table tbl,int tableNumb) throws SQLException {
       numberofRows = 0; 
        tableNumber = tableNumb;
        ResultSet rs = sql_manager.getDataFromSQL("localhost", 8889, "mysql", SQL);
        for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
            String kolonneNavn = rs.getMetaData().getColumnName(i);
            Kolonne kol = new Kolonne(kolonneNavn,i-1,tbl);
        
            listofColumns.add(kol);
            System.out.println("lzzzzegger til kolonne i settet med navn " + kol);

        }

        //deretter legges all dataen til i kolonnene ved hjelp av rader
        while (rs.next()) {
            numberofRows++;
            for (Kolonne k : listofColumns) {
                String item = rs.getString(k.NAVN);
                k.addField(item);
                
            }

        }

    }

    public void loadCombinedColumns(List<Kolonne> listOfCombined,String navn,Table tbl) {
      
        Boolean isInteger = false;
        Boolean isString = false;
        for (Kolonne kol : listOfCombined) {
            
            
            if (checkForInteger.isInteger(kol.allFields().get(0))) {
                isInteger = true;

            } else if (checkForInteger.isInteger(kol.allFields().get(0)) == false) {
                isString = true;

            }
        }
        if (isInteger && isString == false || isString && isInteger == false) {
        
        
            
            Kolonne kolComb = new Kolonne(listOfCombined, navn,tbl);
     
            listofColumns.add(kolComb);

        } else {
            System.out.println("kombo not allowed");
        }

    }
    


    public String storString() {
        return listofColumns.toString();
    }

    public TableView makeTableView(TableView tableView,Table tbl) {
        ObservableList<List<String>> dataen = FXCollections.observableArrayList();
        //først henter vi ut alle kolonnene og legger til de i tableview
        int counter = 0;

        // System.out.println("kaska kaka " + listofColumns.size());
        for (Kolonne kol : listofColumns) {
            //We are using non property style for making dynamic table

            final int j = counter;
            TableColumn col = new TableColumn(kol.NAVN);
            col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                    return new SimpleStringProperty(param.getValue().get(j).toString());

                }
            });

            col.prefWidthProperty().bind(tableView.widthProperty().divide(4)); //for å automatisere bredden på kolonnene 
            //col.setGraphic(new CheckBox());
            col.setUserData(counter);
            tableView.getColumns().add(col);
//System.out.println("enototot");
            counter++;

        }
        int teller = 0;
        System.out.println(tbl.numberofRows);
        for (int a = 1; a <=tbl.numberofRows ;a++) {
            ObservableList<String> row = FXCollections.observableArrayList();
       
            for (int i = 1; i <= listofColumns.size(); i++) {
                Kolonne kol = listofColumns.get(i - 1);

                
              try{
                  row.add(kol.allFields().get(teller));
               
                      
               }
                       
             
                catch(NullPointerException npe)
                {
                          row.add(" ");
                }
                
              

                //  System.out.println("Verdien " + kol.allFields().get(teller));
            }
            teller++;

            dataen.add(row);

        }

        tableView.setItems(dataen);

        //deretter legges all dataen til i kolonnene ved hjelp av rader
        //    for (int i = 1; i <= listofColumns.size(); i++) {
        //     tableView.getItems().add(row);
        //  tableView.setItems(dataen);
        return tableView;

    }
    public boolean isEmpty(String streng){
   if (streng == null) 
      return true;
   return false;
}

}
