module com.tlp2.nightfallz {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.tlp2.nightfallz to javafx.fxml;
    exports com.tlp2.nightfallz;
}
