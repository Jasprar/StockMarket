package StockMarket;


import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller {
    Simulator sim = new Simulator();
  // dummy test need to get duration then instantiate(?) // need to overrride it
     //Views

     @FXML MenuItem runSim;
     @FXML Label dayEntry;;
     @FXML Label eventEntry;
     @FXML TextField timeEntry;
     @FXML Label shareEntry;
     @FXML Label marketEntry;
    int duration;
        /**
         * File menubar -> menu items methods
         */
     public void getFile(){
         TextInputDialog dialog = new TextInputDialog("");
         dialog.setTitle("Success!");
         dialog.setHeaderText("Files have been added");
         dialog.show();

     }; //needed??? Seems to be hardcoded in

     public void runSimulation(){

          TextInputDialog dialog = new TextInputDialog("");
         dialog.setTitle("Duration Needed");
         dialog.setHeaderText("Enter duration (Minutes)");
         dialog.setContentText("Duration: ");
         dialog.show();
         /* Currently doesnt work
         duration = Integer.parseInt("3");
         if(duration != 0) sim.runSimulation(duration);
          */
         callMethod();
     };

     public void callMethod(){
         Food();
         Hardware();
         HiTech();
         Property();
         time();
         //day();
         //MarketType();
         //event();



     };
     public void quit(){
         System.exit(0);
     };

        /**
         * Help menubar --> menu item methods
         */

        public void howToUse(){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText("How to use");
            alert.setContentText("This application is intended as a game and to help people learn abotu stock marketing." +
                    "Select File - Duration and enter how long you want the application to run for, in minutes." +
                    "Start clicking around and watch stock prices rising and falling per client. If you understand ");

            alert.showAndWait();

        };
        public void aboutUs(){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText("About us");
            alert.setContentText("Formed in 1965, W&G Ltd are one of London’s premier financial trading companies. In 1983, they\n" +
                    "expanded upon the acquisition of Duke and Duke Corp (after that company ran into difficulties over\n" +
                    "a scandal in the frozen orange juice commodities market). W&G provide investment management\n" +
                    "for their clients, helping them to get the best returns of their investment through buying and selling\n" +
                    "of stock on the London stock exchange");

            alert.showAndWait();

        };

        /**
         * Legal menuBar --> Menu item methods
         */
        public void License(){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText("I have a great message for you!");

            alert.showAndWait();
        };
        public void copyRight(){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Copyright");
            alert.setHeaderText(null);
            alert.setContentText("This belong to the University of Sussex");

            alert.showAndWait();
        };
        public void credits(){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Credits");
            alert.setHeaderText(null);
            alert.setContentText("Jasper Weymouth,Feroze Saeed,Sheel Shah,Steven Shum");

            alert.showAndWait();
        };

        /**
         * Left panel --> Commodities methods
         */

        public void Food(){}; // Needs to be changed to labels
    // TODO: Change textfields to labels and create new labels for the data to get added to there // - learn how to bind.
        public void Hardware(){};
        public void HiTech(){};
        public void Property(){};

        /**
         * Bottom panel (Below graph) --> Information panel
         * Market type, ShareIndex, Event, Day
         */

        public void time(){
            System.out.println("Entering this");
           timeEntry = new TextField();
           timeEntry.setText("test");
            timeEntry.setStyle("-fx-border-color:red; -fx-background-color: blue; -fx-color-label-visible: true");
        }; // Textfield for timeEntry
       // public void day(){dayEntry.textProperty().bind(valueProperty); }; // Textfield for dayEntry
       // public void MarketType(){ marketEntry.textProperty().bind(sim.getMarketType()));}; // Text field marketEntry
       // public void event(){eventEntry.textProperty().bind(valueProperty);}; // Textfield for eventEntry

       // public String getMarket(){
       //     return sim.getMarketType();
        //}
       // public StringProperty PropertyMarketType(){
        //    return this.getMarketType;
        //}


}
