package connecttodifferentdatabases;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Eskil Hesselroth
 */
class Kolonne {

    public final String NAVN;
  

    private List<String> fields;

    Kolonne(String kolonneNavn,Integer kolonneIndex) {
        NAVN = kolonneNavn;
      
        
        fields = new ArrayList<>();
    }

    public Kolonne(List<Kolonne> listOfColumns, String navn){
        fields = new ArrayList<>();  
        for (Kolonne kol: listOfColumns){
            fields.addAll(kol.fields);
        }
        
        NAVN= navn; 
     
    }

  
       
        
  


  

    void addField(String item) {
        fields.add(item);
    }
    
    List<String> allFields() {
    return new ArrayList<String>(fields);
}
    
    
 
    
    
    

    //Overkjører toString for å isteden returnere kolonnenavn og felter
    @Override
    public String toString() {
        return NAVN + fields.toString();
    }
}
