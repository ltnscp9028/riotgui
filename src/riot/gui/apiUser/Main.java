package riot.gui.apiUser;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;
public class Main extends Application {
    public static void main(String[] args) { launch(args); }
    public void start(Stage primaryStage) throws Exception {
        Font.loadFont(getClass().getResourceAsStream("NanumBarunGothic_0.ttf"),.14);
        Parent root =
                FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("RiotSearchUser");
        primaryStage.setScene(new Scene(root, 1000, 600));
        primaryStage.show();
    }
}

