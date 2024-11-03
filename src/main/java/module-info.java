module adrian {
    requires transitive javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.base;

    opens adrian to javafx.fxml;
    exports adrian;

    opens adrian.modelo to javafx.base;

    opens adrian.controlador to javafx.fxml;
    exports adrian.controlador to javafx.fxml;

    
}
