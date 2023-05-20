module com.example.lsystemgenerator {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires com.almasb.fxgl.all;

    opens com.example.lsystemgenerator to javafx.fxml;
    exports com.example.lsystemgenerator;
    exports com.example.lsystemgenerator.util;
    opens com.example.lsystemgenerator.util to javafx.fxml;
}