package StockMarket;


import javafx.application.Platform;
import javafx.event.*;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.util.Duration;
import tray.notification.TrayNotification;
import java.util.*;

import static tray.animations.AnimationType.POPUP;

/**
 * Controller class. Handles the the users input and updates the gui every second using Timer Task.
 * The annotation - "FXML" enables the FXMLLoader in GUI.java to inject values defined
 * in the FXML file into references in the controller class.  For example, if we annotate
 * a menu item 'run sim' with @FXML then it will be initialized by the FXMLLoader when the
 * load() method is called by an element int he FXML file with "<MenuItem fx:id="RunSim></MenuItem>"
 *
 * The @FXML above methods makes the method handle the action event. This only works if the FXML file has component of
 * onAction="#(MethodName)". For example <MenuItem fx:id="runSim" onAction = "#RunSimulation"></MenuItem>
 *
 * @Author 132224
 * @Version 24/04/2017
 */



public class Controller {
    //Views, the FXML annontations that is linked with the FXML file references.
    // Needed to access the type of component and to add events/ listeners.

    @FXML
    private MenuItem RunSim;
    @FXML
    private Label dateEntry, eventEntry,timeEntry,shareEntry,marketEntry;
    @FXML
    private TextArea Hardware, Food, HiTech,Property;
    @FXML
    private TableView<CompanyData> companyDataTableView;
    @FXML
    private  TableColumn<CompanyData, String> companyName;
    @FXML
    private TableColumn<CompanyData,Number> companyShares;
    @FXML
    private TableColumn<CompanyData,Number> totalShares;
    @FXML
    private TableView<ClientData> clientDataTableView;
    @FXML
    private TableColumn<ClientData,String> client;
    @FXML
    private TableColumn<ClientData,Number> clientWealth,cashHolding,clientShares,managedBy;
    @FXML
    private LineChart linechart;
    @FXML
    private NumberAxis x,y;
    @FXML
    private Tab backEnd;
    @FXML
    private Button runButton;
    @FXML
    private AnchorPane ClientPane,CompanyPane;
    @FXML
    private VBox frame;

    //Java fields
    private int duration;
    private int count;
    private int clicked;
    private final int COMMODOTIES_TIME = 1000;
    private int TABLE_REFRESH_RATE = 6000;

    Timer timer = new Timer();
    Simulator sim = new Simulator(1);

    /**
     * Calls runSimulation on clickevent of "Run Simulation" / "Run".
     * Event created:
     * Dialogue popup, asking the user to enter the duration in minutes.
     * Runs the simulation for that certain minutes by calling sim.RunSimulation(duration).
     * Sets the buttons to disable so the user cannot run the simulation again whilst the current
     * simulation is running.
     */
    @FXML
    private void runSimulation() throws Exception {
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle("Duration Needed");
        dialog.setHeaderText("Enter duration (Minutes)");
        dialog.setContentText("Duration: ");
        dialog.show();
        //TODO: Wait for the user to enter duration then do sim.runSimulation(duration)
        // Currently doesnt work
        // duration = Integer.parseInt("3");
        // if(duration != 0) sim.runSimulation(duration){
        //int totalTime = duration * 60; // Minutes to seconds
        RunSim.setDisable(true);
        runButton.setVisible(false);
        callMethod();
    //}
        }

    private void globalTimer() throws Exception {}

    /**
     * Quits the program by calling System.exit(0);. Clears all background data (Notifications) automatically.
     */
    @FXML
    private void quit() {
        System.exit(0);
    }


    /**
     * On click event from  <MenuItem fx:id="HowToUse" Title = "How to use" onAction="#howToUse"></MenuItem>.
     * Displays an information dialogue displaying how to use the simulation.
     */
    @FXML
    private void howToUse() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText("How to use");
        alert.setContentText("This application is intended as a game and to help people learn abotu stock marketing." +
                "Select File - Duration and enter how long you want the application to run for, in minutes." +
                "Start clicking around and watch stock prices rising and falling per client. If you understand ");
        alert.showAndWait();
    }

    /**
     * On click event from  <MenuItem fx:id="aboutUs" Title = "About Us" onAction="#aboutUs"></MenuItem>.
     * Displays an information dialogue displaying the history of Wolf and Gecko.
     */
    @FXML
    private void aboutUs() {
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
     * On click event from  <MenuItem fx:id="license" Title = "License" onAction="#License"></MenuItem>.
     * Displays an information dialogue displaying the terms of the License agreement.
     */
    @FXML
    private void License() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("License");
        alert.setHeaderText(null);
        alert.setContentText("Licenced to Wolfe & Gecko Ltd.");
        alert.showAndWait();
    }

    /**
     * On click event from  <MenuItem fx:id="copyright" Title = "Copyright" onAction="#copyRight"></MenuItem>.
     * Displays an information dialogue displaying the terms of the copyright agreement.
     */
    @FXML
    private void copyRight() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Copyright");
        alert.setHeaderText(null);
        alert.setContentText("This belong to the University of Sussex");
        alert.showAndWait();
    }

    /**
     * On click event from  <MenuItem fx:id="credits" Title = "credits" onAction="#credits"></MenuItem>.
     * Displays an information dialogue displaying the credits.
     */
    @FXML
    private void credits() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Credits");
        alert.setHeaderText(null);
        alert.setContentText("Jasper Weymouth,Feroze Saeed,Sheel Shah,Steven Shum");
        alert.showAndWait();
    }

    /***
     * Once the run simulation duration as been entered, these methods are called.
     * Commodities method displays the overall commodities values. EG: Property value has
     * went down from 52% to 35%.
     *
     * CurrentTime displays the current time in the simulation in a format of 00:00:00
     * CurrentDate displays the current date in the simulation starting from 1st 2017, in the format
     * of DD/MM/YYYY
     * Market Type displays the current market type, eg: bear market
     * Event displays a message of an event has occured.
     * Graph displays the share index prices per month
     * speedControl lets the user control the speed when looking at the information, from slow to fast.
     * CompanyTable ClientTable shows their information in "Back End Tab".
     * BackEnd tab, when clicked upon , it displays a 1 time only popup tip. Telling the user how to sell clients stock
     * and how to manage the speed.
     * Quiz, a window tray notification asking quizzes at random times.
     */
    private void callMethod() {
        commodities(); currentTime(); share(); currentDate(); MarketType(); event(); graph();
        speedControl(); clientTable(); companyTable();  backEnd(); quiz();
    }


    /**
     * Gets called from callMethod(), displays the commodities values(Food, HardWare, HiTech, Property)
     * in accordance to COMMODITIES_TIME. (Updates it's values).
     */
    private void commodities(){
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    Food.setText("Food: " + "\n" + "50%");
                });
            }
        }, 0, COMMODOTIES_TIME);

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    Hardware.setText("Hardware: " + "\n" + "50%");
                });
            }
        }, 0, COMMODOTIES_TIME);

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    HiTech.setText("HiTech: " + "\n" + "50%");
                });
            }
        }, 0, COMMODOTIES_TIME);

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    Property.setText("Property: " + "\n" + "50%");
                });
            }
        }, 0, COMMODOTIES_TIME);


    }

    /**
     * Gets called from callMethod()
     * Displays the current time, updates every second.
     */
    private void currentTime() {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    timeEntry.setText(sim.getTime());
                });
            }
        }, 0, 1000);
    }


    /**
     * Gets called from callMethod()
     * Displays the current date, updates every second.
     */
    private void currentDate() {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    dateEntry.setText(sim.getDate());
                });
            }
        }, 0, 1000);
    }


    /**
     * Gets called from callMethod()
     * Displays the current market type , updates every second, eg Bear Market.
     */
    private void MarketType() {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    marketEntry.setText(sim.getMarketType());
                });
            }
        }, 0, 1000);


    }

    /**
     * Gets called from callMethod()
     * Displays a message if an event has occured. If no event has occurred then displays an empty string.
     */
    private void event() {
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

    /**
     * Gets called from callMethod()
     * Displays the current share index rate. Updates every second.
     */
    private void share() {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    String share = Double.toString(sim.getShareIndex());
                    shareEntry.setText(share + "p");
                });
            }
        }, 0, 1000);
    }

    /***
     * Gets called from callMethod()
     * Displays share index on a line chart. Shows the share index value per every month.
     */
    private void graph() {
        XYChart.Series series = new XYChart.Series();
        series.setName("Share Index");
        x.setLabel("Month");
        y.setLabel("Share Index");

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
     * Increments count every time. Makes the graph worth within 1-12. IE: Jan-Dec
     * @return (Incremental) count
     */
    private int counter() {
        if(count < 12 ) count++;
        return count;
    }


    /***
     *When the user right clicks on on anchor pane with FX:ID  = "CompanyPane" (CompanyPane is the global pane for
     * Company and Client) then a choice dialog popup occurs, asking the user to Select from the choice
     * of speed. When the user selects it's speed the "TABLE_REFRESH_RATE" value gets updated and changes
     * the speed of the table refresh rate. Causes some data to be lost / gain in response to how fast the table
     * should update its results.
     */
    private void speedControl() {
        CompanyPane.addEventFilter(MouseEvent.MOUSE_PRESSED, e ->{
            if(e.isSecondaryButtonDown()){ // If right click
                List<String> choices = new ArrayList<>();
                choices.add("Slow");
                choices.add("Normal");
                choices.add("Fast");

                ChoiceDialog<String> dialog = new ChoiceDialog<>("Normal", choices);
                dialog.setTitle("Speed Choice");
                dialog.setHeaderText("Select the speed you wish the table to simulate at");
                dialog.setContentText("Choose speed: ");
                Optional<String> result = dialog.showAndWait();

                if (result.isPresent()){
                    if(result.get().equals("Slow")){

                        this.TABLE_REFRESH_RATE = 3000;

                        System.out.println(TABLE_REFRESH_RATE);
                    }
                    if(result.get().equals("Normal")){
                        this.TABLE_REFRESH_RATE = 2000;
                        System.out.println(TABLE_REFRESH_RATE);
                    }
                    if(result.get().equals("Fast")){
                        this.TABLE_REFRESH_RATE = 1000;

                        System.out.println(TABLE_REFRESH_RATE);
                    }
                }
            }
        });
    }

    /**
     * Back end tab, Company Table. Displays Company Name, Company values, Share Price, and total shares.
     * Retrieves the object from CompanyData and appends each row per every instance created.
     * Refreshes in response to the users request when right clicking to set the speed.
     */
    private  void companyTable() {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    quiz();
                    companyDataTableView.getItems().clear();
                    for(CompanyData s: companydataList()){
                        companyDataTableView.getItems().add(s);
                    }
                });
            }
        }, 0, TABLE_REFRESH_RATE);
    }

    /***
     * Populates arraylists, Company Name, Values, Price and total shares accordingly.
     * Creates a new instance of companyData by calling the object and filling in 'dummy data'.
     * Overrites the dummy data for the first elements in the arraylists.
     * Sends the object.
     * Keeps repeating till CompanyName.Size() has been reached.
     * @return A list of CompanyData object(s)
     */
    private List<CompanyData> companydataList(){
        List<String> companyNames = new ArrayList<>();
        Set<String> companyNames1 =  sim.getCompanyNames();
        companyNames.addAll(companyNames1);


        List<Integer> clientNetworth = new ArrayList<>();

        List<Integer> companyValues = new ArrayList<>();
        Collection<Integer> companyValues1 = sim.getCompanyValues();
        companyValues.addAll(companyValues1);

        List<Double> sharePrice = new ArrayList<>();
        for(String s: companyNames) {
            sharePrice.add(sim.getSharePrice(s));
            clientNetworth.add(sim.getNetWorth(s));
        }

        List<CompanyData> companyData = new ArrayList<>();

        for(int i = 0; i < companyNames.size(); i++){
            String name = companyNames.get(i);
            Double sharePrices = sharePrice.get(i);
            int value = companyValues.get(i);
            int networth = clientNetworth.get(i);
            CompanyData company = new CompanyData("Test",1,1,1);//Creating a object  per row
            company.setPFCompanyName(name);
            company.setPFShareValues(sharePrices);
            company.setPFTotalShares(value);
            company.setPFNetWorth(networth);
            companyData.add(company);
        }
        return companyData;
    }

    /**
     * Client Table. Displays Client Name, Cash holding, total worth, shares and trader type
     * Retrieves the object from ClientData and appends each object per row per when every instance is created.
     * Refreshes in response to the users request when right clicking to set the speed.
     */
    private void clientTable() {


        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    clientDataTableView.getItems().clear();
        for (ClientData s : clientDataList()) { //Needs to be changed to global timer
            clientDataTableView.getItems().add(s);
        }
                });
            }

        }, 0, TABLE_REFRESH_RATE);


        Tooltip tooltip = new Tooltip(); //Lets us create a hover message
        tooltip.setText("\nDouble click to sell stock\n");
        clientDataTableView.setTooltip(tooltip);

        clientDataTableView.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.isPrimaryButtonDown() && event.getClickCount() == 2){
                    ClientData clientData = clientDataTableView.getSelectionModel().getSelectedItem();
                    sim.leaveSimulation(clientData.getPFClient());
                }
            }
        });
    }

    /***
     * Populates arraylists, Client Name, CashHolding, and totalWorth shares accordingly.
     * Creates a new instance of ClientData by calling the object and filling in 'dummy data'.
     * Overrites the dummy data for the first elements in the arraylists.
     * Sends the object.
     * Keeps repeating till ClientNames.Size() has been reached.
     * @return
     */
    private List<ClientData> clientDataList() {
        //Getting client Names and appending to list
        List<String> ClientNames = new ArrayList<>();
        ClientNames.addAll(sim.getClientNames());

        //Getting Cash holding and appending to list
        List<Double> CashHolding = new ArrayList<>();
        CashHolding.addAll(sim.getCashHolding());
        //Getting total worth and appending to list
        List<Double> TotalWorth = new ArrayList<>(); //Wealth
        TotalWorth.addAll(sim.getTotalWorth());

        List<ClientData> clientData = new ArrayList<>();

        for(int i = 0; i < ClientNames.size(); i++) {
            String clientNames = ClientNames.get(i);
            Double cashHolding = CashHolding.get(i);
            Double totalWorth = TotalWorth.get(i);
            ClientData client = new ClientData("Test", 1, 1,"3","Random");//Creating a object  per row
            client.setPFClient(clientNames);
            client.setPFCashHolding(cashHolding);
            client.setPFWealth(totalWorth);
            clientData.add(client);
        }
        return clientData;
    }

    /***
     * One time only popup when BackEnd tab is clicked upon, displaying the a tip message.
     * Checks if clicked < 1. If it is, then displays the message and increments clicked.
     */
    private void backEnd(){

        backEnd.setOnSelectionChanged(new EventHandler<javafx.event.Event>() {
            @Override
            public void handle(Event event) {
                if(backEnd.isSelected() && clicked < 1 ){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Tip");
                    alert.setHeaderText("Tip!");
                    alert.setContentText("Double click any client row to sell all their stock!\n" + "Right click to adjust speed!");
                    alert.showAndWait();
                    clicked++;
                }

            }
        });
    }

    /***
     * Uses a library from Github by PlusHaze: https://github.com/PlusHaze/TrayNotification
     * Uses the timer from the company timer, to save memory as it's not needed to have an independent timer.
     * Displays a quiz / "Question" regarding how a stock market works in general to test the users knowledge
     * at random timers. A user may never get a popup notification, but the chances of that is low.
     *
     */
    private void quiz() {
        TrayNotification tray = new TrayNotification();
        tray.setRectangleFill(Paint.valueOf("#ff3300"));
        tray.setAnimationType(POPUP);
        Image whatsAppImg = new Image("StockMarket/img/lightbulb.png");
        tray.setImage(whatsAppImg);
        Random ran = new Random();
        int x = ran.nextInt(100) + 1;
        switch (x) {
            case 14:
                tray.setMessage("What factors affect stock market?") ;
                tray.setTitle("QUIZ");
                tray.showAndDismiss(Duration.seconds(7));
                tray.showAndWait();

                break;
            case 51:
                tray.setMessage("What does Short Selling mean?");
                tray.setTitle("QUIZ");
                tray.showAndDismiss(Duration.seconds(7));
                tray.showAndWait();
                break;
            case 39: tray.setMessage("What is a stock?");
                tray.setTitle("QUIZ");
                tray.showAndDismiss(Duration.seconds(7));
                tray.showAndWait();
                break;
            case 74:
                tray.setMessage("Where's the oldest stock exchange in the world?");
                tray.setTitle("QUIZ");
                tray.showAndDismiss(Duration.seconds(7));
                tray.showAndWait();
                break;
            case 15:
                tray.setMessage("Which type of bond is the safest?");
                tray.setTitle("QUIZ");
                tray.showAndDismiss(Duration.seconds(7));
                tray.showAndWait();
                break;
            case 6:
                tray.setMessage("In general, if interest rates go down, then bond prices…");
                tray.setTitle("QUIZ");
                tray.showAndDismiss(Duration.seconds(7));
                tray.showAndWait();
                break;
        }
    }

}
