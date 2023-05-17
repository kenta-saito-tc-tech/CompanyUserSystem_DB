package com.example.companyusersystem_db;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.List;

public class WorkPlace {
    private IntegerProperty id;
    private StringProperty name;

    public WorkPlace(int anId, String aName) {
        id = new SimpleIntegerProperty(anId);
        name = new SimpleStringProperty(aName);
    }
    public WorkPlace(String aName) {
        name = new SimpleStringProperty(aName);
    }
    public WorkPlace(List<Company> allTables, int i) {
        id = new SimpleIntegerProperty(allTables.get(i).id());
        name = new SimpleStringProperty(allTables.get(i).name());
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void deleteAll(){
        id.setValue(null);
        name.setValue(null);
    }

    public void setALl(){

    }
}
