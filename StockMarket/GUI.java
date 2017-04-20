package StockMarket;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.layout.StackPane;
import sun.plugin.javascript.navig.Anchor;

import java.awt.event.ActionListener;

public class GUI extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Stock simulator");
        AnchorPane anchorpane = new AnchorPane();

        MenuBar menuBar = new MenuBar(); //Initialize Menubar
        Menu File = new Menu("File");
        menuBar.getMenus().add(File);


        MenuItem importFile = new MenuItem("Import File");
        File.getItems().add(importFile);

        MenuItem RunSim = new MenuItem("Run Simulation");
        File.getItems().add(RunSim);
        //To-Do: add actionlisterner to run sim for "Duration" ;

        MenuItem Quit = new MenuItem("Quit");
        File.getItems().add(Quit);

        /**
         * New menu section - Help
         */
        Menu help = new Menu("Help");
        menuBar.getMenus().add(help);

        MenuItem playInfo = new MenuItem("How to use");
        help.getItems().add(playInfo);

        MenuItem aboutUs = new MenuItem("About us");
        help.getItems().add(aboutUs);
        help.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                final Stage dialog = new Stage();
                dialog.initModality(Modality.APPLICATION_MODAL);
                dialog.initOwner(primaryStage);
                VBox dialogVbox = new VBox(20);
                dialogVbox.getChildren().add(new Text("Formed in 1965, W&G Ltd are one of London’s premier financial trading companies. In 1983, they\n" +
                        "expanded upon the acquisition of Duke and Duke Corp (after that company ran into difficulties over\n" +
                        "a scandal in the frozen orange juice commodities market). W&G provide investment management\n" +
                        "for their clients, helping them to get the best returns of their investment through buying and selling\n" +
                        "of stock on the London stock exchange.\n"));

                Scene dialogScene = new Scene(dialogVbox, 300, 200);
                dialog.setScene(dialogScene);
                dialog.show();
            }


        });
        /**
         * adding legal
         */
        Menu legal = new Menu("Legal");
        menuBar.getMenus().add(legal);

        MenuItem license = new MenuItem("License");
        legal.getItems().add(license);

        MenuItem copyright = new MenuItem("Copyright");
        legal.getItems().add(copyright);

        MenuItem credits = new MenuItem("Credits");
        legal.getItems().add(credits);


        AnchorPane.setTopAnchor(menuBar, 0.0);
        AnchorPane.setRightAnchor(menuBar, 0.0);
        AnchorPane.setLeftAnchor(menuBar, 0.0);
        anchorpane.getChildren().addAll(menuBar);

        primaryStage.setScene(new Scene(anchorpane, 1500, 700)); //needs to be changed in correlation to screen resolution

        graph();

        primaryStage.show();
    }

    private void graph() {
        //TO-DO: Add graph
    }

    private void clientTable() {
        //TO-DO add client table
    }

    private void CompanyTable() {
        //To-Do add companyTable
    }

    /*
      Displays the share , market and event tables at the top
     */
    private void ShareMarketEvent() {

    }




    private void CurrentDay() {

    }

    public static void main(String[] args) {
        launch(args);
    }
}
