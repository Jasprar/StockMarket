package StockMarket;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * Creates Graphical user inferface of the stock market simulation.
 * Loads guiview.fxml which represents the view.
 * Guiview takes in Controller.java which lets the user interact with it by using Event listeners.
 *
 * @author 132224
 * @version 25/04/2017
 */
public class GUI extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("guiview.fxml"));
        primaryStage.setTitle("Stockmarket simulation");
        primaryStage.setScene(new Scene(root, 1110, 695));
        primaryStage.setResizable(false);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
