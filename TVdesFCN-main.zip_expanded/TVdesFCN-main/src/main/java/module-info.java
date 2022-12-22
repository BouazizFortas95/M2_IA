module org.wh.tv_fcn {
    requires java.logging;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires com.jfoenix;

    opens org.wh.tv_fcn to javafx.fxml;
    opens org.wh.tv_fcn.controllers to javafx.fxml, javafx.web;

    exports org.wh.tv_fcn;
    exports org.wh.tv_fcn.controllers;
}
