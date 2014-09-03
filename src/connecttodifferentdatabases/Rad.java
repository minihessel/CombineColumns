/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package connecttodifferentdatabases;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Eskil Hesselroth
 */
public class Rad {
    
    private List<String> listOfRows;
      Rad(String kolonneNavn) {

    }
      
         void addRow(String item) {
        listOfRows.add(item);
    }
      
      List<String> allRows() {
    return new ArrayList<String>(listOfRows);
}
      
      
    
}
