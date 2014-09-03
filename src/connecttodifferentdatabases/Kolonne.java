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

    Kolonne(String kolonneNavn) {
        NAVN = kolonneNavn;
        fields = new ArrayList<>();
    }

    public Kolonne(Kolonne kol1, Kolonne kol2, String navn){
        NAVN= navn; 
        fields = new ArrayList<>(kol1.fields.size() + kol2.fields.size());
        fields.addAll(kol1.fields);
        fields.addAll(kol2.fields);
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
