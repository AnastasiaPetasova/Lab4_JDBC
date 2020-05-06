import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.awt.*;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("view/main.fxml"));
        primaryStage.setTitle("Lab 4: JDBC");

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        primaryStage.setScene(new Scene(root, screenSize.width / 2.0, screenSize.height / 2.0));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
