package connecttodifferentdatabases;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Eskil Hesselroth
 */
public class Kolonne  {

    public final String NAVN;
        public final Table tbl; 


    private List<String> fields;

    public Kolonne(String kolonneNavn,Integer kolonneIndex,Table tbl1) {
        NAVN = kolonneNavn;
         tbl = tbl1;

        
        fields = new ArrayList<>();
    }

    public Kolonne(List<Kolonne> listOfColumns, String navn,Table tbl1){
        fields = new ArrayList<>();  
        for (Kolonne kol: listOfColumns){
            fields.addAll(kol.fields);
        }
        tbl= tbl1;
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
