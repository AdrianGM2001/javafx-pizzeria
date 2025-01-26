module adrian {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.base;
    requires org.testfx;
    requires kernel;
    requires layout;
    requires org.apache.pdfbox;
    requires java.desktop;

    opens adrian to javafx.fxml;
    exports adrian;

    opens adrian.modelo to javafx.base;
    exports adrian.modelo;

    opens adrian.controlador to javafx.fxml;
    exports adrian.controlador to javafx.fxml;
}
