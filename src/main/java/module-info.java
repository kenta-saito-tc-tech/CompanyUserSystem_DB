module com.example.companyusersystem_db {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.companyusersystem_db to javafx.fxml;
    exports com.example.companyusersystem_db;
}