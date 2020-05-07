module riotApiGui {
    requires javafx.controls;
    opens riot.gui.apiUser;
    requires javafx.fxml;
    requires gson;
}