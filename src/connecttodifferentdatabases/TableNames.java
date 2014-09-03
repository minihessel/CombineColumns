/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connecttodifferentdatabases;

import javafx.beans.property.SimpleStringProperty;

/**
 * 
 * @author Eskil Hesselroth
 */
public class TableNames {
    private final SimpleStringProperty oracleName;
    private final SimpleStringProperty mysqlName;
    private final SimpleStringProperty mssqlName;
 
    TableNames(String oName, String myName, String msName) {
        this.oracleName = new SimpleStringProperty(oName);
        this.mysqlName = new SimpleStringProperty(myName);
        this.mssqlName = new SimpleStringProperty(msName);
    }
 
    public String getOracleName() {
        return oracleName.get();
    }
    public void setOracleName(String oName) {
        oracleName.set(oName);
    }
        
    public String getMySqlName() {
        return mysqlName.get();
    }
    public void setMySqlName(String myName) {
        mysqlName.set(myName);
    }
    
    public String getMsSqlName() {
        return mssqlName.get();
    }
    public void setMsSqlName(String msName) {
        mssqlName.set(msName);
    }
        
}