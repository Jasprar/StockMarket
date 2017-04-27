package StockMarket;


import javafx.application.Platform;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.*;

/**
 * Controller class. Handles the the users input and updates the gui every second using Timer Task.
 *
 * @Author 132224
 * @Version 24/04/2017
 */


//TODO Put all code that needs timers in 1 method - stops repeating the data after testing (Easier to test when its in seperate methods

public class Controller {
    //Views

    @FXML
    MenuItem runSim;
    @FXML
    Label dateEntry;
    @FXML
    Label eventEntry;
    @FXML
    Label timeEntry;
    @FXML
    Label shareEntry;
    @FXML
    Label marketEntry;
    @FXML
    TextArea Hardware;
    @FXML
    TextArea Food;
    @FXML
    TextArea HiTech;
    @FXML
    TextArea Property;
    @FXML
    TableColumn Company;
    @FXML
    TableView tableview;
    @FXML
    LineChart linechart;
    @FXML
    NumberAxis x;
    @FXML
    NumberAxis y;

    //Java fields
    int duration;
    Timer timer = new Timer();
    Simulator sim = new Simulator();
    int count;


    /**
     * File menubar -> menu items methods
     */
    @FXML
    public void getFile() {
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle("Success!");
        dialog.setHeaderText("Files have been added");
        dialog.show();

    }

    ; //needed??? Seems to be hardcoded in

    @FXML
    public void runSimulation() {

        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle("Duration Needed");
        dialog.setHeaderText("Enter duration (Minutes)");
        dialog.setContentText("Duration: ");
        dialog.show();

        //TODO: Wait for the user to enter duration then do sim.runSimulation(duration)
        // Currently doesnt work
        // duration = Integer.parseInt("3");
        // if(duration != 0) sim.runSimulation(duration);

        callMethod();
    }


    @FXML
    public void callMethod() {
        Food();
        Hardware();
        HiTech();
        Property();
        time();
        share();
        date();
        MarketType();
        event();
        graph();
        clientTable();
        companyTable();


    }


    @FXML
    public void quit() {
        System.exit(0);
    }


    /**
     * Help menubar --> menu item methods
     */

    public void howToUse() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText("How to use");
        alert.setContentText("This application is intended as a game and to help people learn abotu stock marketing." +
                "Select File - Duration and enter how long you want the application to run for, in minutes." +
                "Start clicking around and watch stock prices rising and falling per client. If you understand ");

        alert.showAndWait();

    }


    @FXML
    public void aboutUs() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText("About us");
        alert.setContentText("Formed in 1965, W&G Ltd are one of London’s premier financial trading companies. In 1983, they\n" +
                "expanded upon the acquisition of Duke and Duke Corp (after that company ran into difficulties over\n" +
                "a scandal in the frozen orange juice commodities market). W&G provide investment management\n" +
                "for their clients, helping them to get the best returns of their investment through buying and selling\n" +
                "of stock on the London stock exchange");

        alert.showAndWait();

    }


    /**
     * Legal menuBar --> Menu item methods
     */
    @FXML
    public void License() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(null);
        alert.setContentText("I have a great message for you!");

        alert.showAndWait();
    }


    @FXML
    public void copyRight() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Copyright");
        alert.setHeaderText(null);
        alert.setContentText("This belong to the University of Sussex");

        alert.showAndWait();
    }


    @FXML
    public void credits() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Credits");
        alert.setHeaderText(null);
        alert.setContentText("Jasper Weymouth,Feroze Saeed,Sheel Shah,Steven Shum");
        alert.showAndWait();
    }


    /**
     * Left panel --> Commodities methods
     */
    @FXML
    public void Food() {
       // Food.appendText("Get Food data");

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    Food.setText("Food: " + "\n" + "50%");
                });
            }
        }, 0, 1000);
    }



    // Needs to be changed to labels

    // TODO: Change textfields to labels and create new labels for the data to get added to there // - learn how to bind.
    @FXML
    public void Hardware() {
        //Hardware.appendText("Get Hardware data");

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    Hardware.setText("Hardware: " + "\n" + "50%");
                });
            }
        }, 0, 1000);
    }


    @FXML
    public void HiTech() {

       // HiTech.appendText("Get HiTech data");

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    HiTech.setText("HiTech: " + "\n" + "50%");
                });
            }
        }, 0, 1000);

    }


    @FXML
    public void Property() {

      //  Property.appendText("Get Property data");

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    Property.setText("Property: " + "\n" + "50%");
                });
            }
        }, 0, 1000);
    }


    /**
     * Bottom panel (Below graph) --> Information panel
     * Market type, ShareIndex, Event, Day
     */

    @FXML
    public void time() {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    timeEntry.setText(sim.getTime());
                });
            }
        }, 0, 1000);
    }

    // Textfield for timeEntry

    public void date() {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    dateEntry.setText(sim.getDate());
                });
            }
        }, 0, 1000);
    }

    // Textfield for dayEntry

    public void MarketType() {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    marketEntry.setText(sim.getMarketType());
                });
            }
        }, 0, 1000);


    }

    // Text field marketEntry

    public void event() {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    if (sim.getEvent() == null) eventEntry.setText("");
                    else eventEntry.setText(String.valueOf(sim.getEvent().getMessage()));
                });
            }
        }, 0, 1000);
    }

    //TODO test this using timer


    public void share() {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    String share = Integer.toString(sim.getShareIndex()); //TODO convert every type of interger to decimal, eg 1000 -> .0001, 100 --> 0.001
                    shareEntry.setText(share + "p");
                });
            }
        }, 0, 1000);
    }

    public void graph() {
        //linechart.setTitle("Stock market");

        XYChart.Series series = new XYChart.Series();
        series.setName("Share Index");
        x.setLabel("Month");
        y.setLabel("Share Index / Event ");


        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    series.getData().add(new XYChart.Data<>(counter(), sim.getShareIndex()));
                });

            }
        }, 0, 3000); //Calculation needed to display every month
        linechart.getData().addAll(series);
    }

    /**
     * Back end tab --> 2 tables
     */

    public void companyTable() {

        List<Collection<Integer>> companies = new ArrayList<>();
        companies.add(sim.getCompanyValues());


        ObservableList<Collection<Integer>> observableList = FXCollections.observableList(companies);


       // Company.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue()));


        //Company.setCellValueFactory(new PropertyValueFactory<Collection<Integer>,String>("companies"));
        tableview.setItems(observableList);





        System.out.println(sim.getCompanyNames());
        System.out.println(sim.getCompanyValues());
        // System.out.println(formattedList);

    }

    public void clientTable() {
        //System.out.println(sim.getPortfolios());
    }

    private int counter() {
    if(count < 12 ) count++;
        return count;
    }


}
