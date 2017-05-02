package StockMarket;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 *
 * Creates Graphical user interface of the stock market simulation.
 * Loads guiview.fxml which represents the view.
 * Guiview takes in Controller.java which lets the user interact with it by using Event listeners and onAction.
 * See controller class javadoc for examples.
 *
 * @author 132224
 * @version 25/04/2017
 */
public class GUI extends Application {

    /****
     * Connection between guiview and controller. A PrimaryStage is used (Frame) as root which loads the guiview on top of
     * the frame. Sets the title (Name of the application), the size of the GUI, sets it to not resizeable and loads the
     * icon for the GUI then shows the GUI.
     * @param primaryStage sets the GUI
     * @throws Exception if guiview.fxml could not be loaded
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("guiview.fxml"));
        primaryStage.setTitle("Wolf & Gecko Stockmarket Simulation");
        primaryStage.setScene(new Scene(root, 1110, 695));
        primaryStage.setResizable(false);
        primaryStage.getIcons().add(new Image("StockMarket/img/logo.png"));
        primaryStage.show(); //Displays the GUI
    }

    public static void main(String[] args) {
        launch(args);
    }
}
