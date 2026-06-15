module com.tlp2.queenspuzzle {

    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.tlp2.queenspuzzle.dao;
    opens com.tlp2.queenspuzzle to javafx.fxml;
    opens com.tlp2.queenspuzzle.controller to javafx.fxml;
    opens com.tlp2.queenspuzzle.model to javafx.base;

    exports com.tlp2.queenspuzzle;
    exports com.tlp2.queenspuzzle.dao;
}