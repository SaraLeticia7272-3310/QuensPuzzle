module com.tlp2.mavenproject1 {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.tlp2.mavenproject1 to javafx.fxml;
    exports com.tlp2.mavenproject1;
}
