package StockMarket;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GUI extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("guiview.fxml"));
        primaryStage.setTitle("Stockmarket simulation");
        primaryStage.setScene(new Scene(root, 1110, 695));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
