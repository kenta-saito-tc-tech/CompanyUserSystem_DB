package com.example.companyusersystem_db;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.List;

public class Person {
    private StringProperty company;
    private IntegerProperty id;
    private StringProperty name;
    private IntegerProperty score;

    public Person(String aCompany, int anId, String aName, int aScore) {
        company = new SimpleStringProperty(aCompany);
        id = new SimpleIntegerProperty(anId);
        name = new SimpleStringProperty(aName);
        score = new SimpleIntegerProperty(aScore);
    }
    //追加用
    public Person(String aCompany, String aName, int aScore) {
        company = new SimpleStringProperty(aCompany);
        name = new SimpleStringProperty(aName);
        score = new SimpleIntegerProperty(aScore);
    }


    public Person(List<AllTable> allTables, int i) {
        company = new SimpleStringProperty(allTables.get(i).companyName());
        id = new SimpleIntegerProperty(allTables.get(i).id());
        name = new SimpleStringProperty(allTables.get(i).name());
        score = new SimpleIntegerProperty(allTables.get(i).score());
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public void setCompany(String company) {
        this.company.set(company);
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public void setScore(int score) {
        this.score.set(score);
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public StringProperty nameProperty() {
        return name;
    }

    public StringProperty companyProperty() {
        return company;
    }

    public IntegerProperty scoreProperty() {
        return score;
    }

    public void deleteAll(){
        id.setValue(null);
        company.setValue(null);
        name.setValue(null);
        score.setValue(null);
    }

    public void setALl(){

    }
}
