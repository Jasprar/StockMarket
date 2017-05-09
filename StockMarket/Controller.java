package StockMarket;


import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
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

import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

import static tray.animations.AnimationType.POPUP;
import static tray.animations.AnimationType.SLIDE;

/**
 * Controller class. Handles the the users input and updates the gui every second using Timer Task.
 * The annotation - "FXML" enables the FXMLLoader in GUI.java to inject values defined
 * in the FXML file into references in the controller class.  For example, if we annotate
 * a menu item 'run sim' with @FXML then it will be initialized by the FXMLLoader when the
 * load() method is called by an element int he FXML file with "<MenuItem fx:id="runSim></MenuItem>"
 *
 * The @FXML above methods makes the method handle the action event. This only works if the FXML file has component of
 * onAction="#(MethodName)". For example <MenuItem fx:id="runSim" onAction = "#RunSimulation"></MenuItem>.
 *
 * @author 132224
 * @version 05/05/2017
 */



public class Controller{
    //Views, the FXML annontations that is linked with the FXML file references.
    // Needed to access the type of component and to add events/ listeners.

    @FXML
    private MenuItem runSim;
    @FXML
    private Label dateEntry, eventEntry,timeEntry,shareEntry,marketEntry;
    @FXML
    private TableView<CompanyData> companyDataTableView;
    @FXML
    private TableView<ClientData> clientDataTableView;
    @FXML
    private LineChart lineChart;
    @FXML
    private NumberAxis x,y;
    @FXML
    private Tab backEnd;
    @FXML
    private Button runButton;
    @FXML
    private AnchorPane companyPane;

    //Java fields
    private int duration;
    private int count;
    private int clicked;
    private int TABLE_REFRESH_RATE = 6000;

    Timer timer = new Timer();
    Simulator sim = new Simulator();
    XYChart.Series series = new XYChart.Series();


    /**
     * Initialises the FXML and begins running funFacts().
     */
    @FXML
    public void initialize(){
        funFacts();
    }

    /*
     * Calls runSimulation on clickevent of "Run Simulation" / "Run".
     * Event created:
     * Dialogue popup, asking the user to enter the duration in minutes.
     * Runs the simulation for that certain minutes by calling sim.RunSimulation(duration).
     * Sets the buttons to disable so the user cannot run the simulation again whilst the current
     * simulation is running.
     * @throws Exception
     */
    @FXML
    private void runSimulation() throws Exception {
        Service<Void> service = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        //Background work
                        final CountDownLatch latch = new CountDownLatch(1);
                                try{
                                    sim.runSimulation();
                                }finally{
                                    latch.countDown();
                                }

                        latch.await();
                        //Keep with the background work
                        return null;
                    }
                };
            }
        };
        service.start();
        callMethod();
        runButton.setVisible(false);
        runSim.setDisable(true);
    }

    /*
     * Quits the program by calling System.exit(0);. Clears all background data (Notifications) automatically.
     */
    @FXML
    private void quit() {
        System.exit(0);
    }


    /*
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

    /*
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


    /*
     * On click event from  <MenuItem fx:id="license" Title = "License" onAction="#License"></MenuItem>.
     * Displays an information dialogue displaying the terms of the License agreement.
     */
    @FXML
    private void license() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("License");
        alert.setHeaderText(null);
        alert.setContentText("Licenced to Wolfe & Gecko Ltd.");
        alert.showAndWait();
    }

    /*
     * On click event from  <MenuItem fx:id="copyright" Title = "Copyright" onAction="#copyRight"></MenuItem>.
     * Displays an information dialogue displaying the terms of the copyright agreement.
     */
    @FXML
    private void copyright() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Copyright");
        alert.setHeaderText(null);
        alert.setContentText("This belong to the University of Sussex");
        alert.showAndWait();
    }

    /*
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

    /*
     * Once the run simulation duration as been entered, these methods are called.
     * Commodities method displays the overall commodities values. EG: property value has
     * went down from 52% to 35%.
     *
     * CurrentTime displays the current time in the simulation in a format of 00:00:00
     * CurrentDate displays the current date in the simulation starting from 1st 2017, in the format
     * of DD/MM/YYYY
     * Market Type displays the current market type, eg: bear market
     * Event displays a message of an event has occured.
     * Graph displays the share index prices per month
     * speedControl lets the user control the speed
     * when looking at the information, from slow to fast.
     * CompanyTable ClientTable shows their information in "Back End Tab".
     * BackEnd tab, when clicked upon , it displays a 1 time only popup tip. Telling the user how to sell clients stock
     * and how to manage the speed.
     * funFacts, a window tray notification displaying fun facts about stock markets  at random times.
     */
    private void callMethod() {
        speedControl();  currentTime(); graph(); removeOldValues();
           backEnd();     clientTable();
        companyTable();
    }
    /*
     * Gets called from callMethod()
     * Displays the current time, updates every second.
     */
    private void currentTime() {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                DecimalFormat df = new DecimalFormat("#");
                df.setMaximumFractionDigits(2);
                Platform.runLater(() -> {
                    timeEntry.setText(sim.getTime()); //Gets the current time
                    dateEntry.setText(sim.getDate()); //Gets the date
                    marketEntry.setText(sim.getMarketType()); // Gets the market type
                    if (sim.getEvent() == null) eventEntry.setText("");
                    else eventEntry.setText(String.valueOf(sim.getEvent().getMessage()));
                    eventEntry.setWrapText(true);
                    String share = Double.toString(Double.parseDouble(df.format(sim.getShareIndex()))); //Gets the share index
                    shareEntry.setText(share + "p");


                });
            }
        }, 0, 10);
    }


    /*
     * Gets called from callMethod()
     * Displays share index on a line chart. Shows the share index value per every month.
     */
    private void graph() {
        series.setName("Share Index");
        x.setLabel("Per 15 Minute Cycle");
        y.setLabel("Share Index");
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    series.getData().add(new XYChart.Data<>(counter(), sim.getShareIndex()));
                    series.getData().removeAll(lineChart.getData());
                });

            }
        }, 0, 500);
        lineChart.getData().addAll(series);



    }

    /***
     * Removes old values from the graph (Uses first in first out system automatically)
     */
    private void removeOldValues(){
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    series.getData().removeAll(lineChart.getData());
                });

            }
        }, 0, 40);
    }

    /*
     * Increments count every time. Makes the graph worth within 1-12. IE: Jan-Dec
     * @return (Incremental) count.
     */
    private int counter() {
        return count++;
    }


    /**
     *When the user right clicks on on anchor pane with FX:ID  = "companyPane" (companyPane is the global pane for
     * Company and Client) then a choice dialog popup occurs, asking the user to Select from the choice
     * of speed. When the user selects it's speed the "TABLE_REFRESH_RATE" value gets updated and changes
     * the speed of the table refresh rate. Causes some data to be lost / gain in response to how fast the table
     * should update its results.
     */
    @FXML
    private void speedControl() {
        companyPane.addEventFilter(MouseEvent.MOUSE_PRESSED, e ->{
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
                        try {
                            Thread.sleep(10000);
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        }
                    }
                    if(result.get().equals("Normal")){
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        }
                    }
                    if(result.get().equals("Fast")){
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        }
                    }

                }
            }
        });
    }


    /*
     * Back end tab, Company Table. Displays Company Name, Company values, Share Price, and total shares.
     * Retrieves the object from CompanyData and appends each row per every instance created.
     * Refreshes in response to the users request when right clicking to set the speed.
     */
    private  void companyTable() {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    companyDataTableView.getItems().clear();
                    for(CompanyData s: companyDataList()){
                        companyDataTableView.getItems().add(s);
                    }

                });
            }
        }, 0, 1000);
    }

    /*
     * Populates arraylists, Company Name, Values, Price and total shares accordingly.
     * Creates a new instance of companyData by calling the object and filling in 'dummy data'.
     * Overrites the dummy data for the first elements in the arraylists.
     * Sends the object.
     * Keeps repeating till CompanyName.Size() has been reached.
     * @return A list of CompanyData object(s)
     */
    private List<CompanyData> companyDataList(){
        List<String> companyNames = new ArrayList<>();
        Set<String> companyNames1 =  sim.getCompanyNames();
        companyNames.addAll(companyNames1); //Get the <set> company tables and it to a list


        List<Integer> netWorth = new ArrayList<>();

        List<Integer> companyValues = new ArrayList<>();
        Collection<Integer> companyValues1 = sim.getCompanyValues();
        companyValues.addAll(companyValues1); //Get the collection<interger> company values and it to a list

        List<Double> sharePrice = new ArrayList<>();
        for(String s: companyNames) {
            sharePrice.add(sim.getSharePrice(s)); //ERROR HERE: Trying to populate the networth arraylist by iterating through
            //each company name and call getSharePrice and getNetWorth
           netWorth.add((int) sim.getNetWorth(s));
        }

        List<CompanyData> companyData = new ArrayList<>();
        DecimalFormat df = new DecimalFormat("#");
        df.setMaximumFractionDigits(2);

        for(int i = 0; i < companyNames.size(); i++){ //Passes the 'i'th element to a variable
            String getNames = companyNames.get(i);
            Double getSharePrices = Double.valueOf(String.format("%.2f",sharePrice.get(i)));
            int getTotalShares = companyValues.get(i);
            int getNetWorth = netWorth.get(i);
            CompanyData company = new CompanyData("Test","test","test","test");//Creating a object  per row
            company.setCompanyName(getNames); //Adds the 'i'th element to the table.
            company.setShareValues(df.format(getSharePrices));
            company.setTotalShares(df.format(getTotalShares));
            company.setNetWorth(df.format(getNetWorth));
            companyData.add(company);
        }

        sharePrice.clear();
        netWorth.clear();
        companyNames.clear();

        return companyData;
    }

    /*
     * Client Table. Displays Client Name, Cash holding, total worth, shares and trader type
     * Retrieves the object from ClientData and appends each object per row per when every instance is created.
     * Refreshes in response to the users request when right clicking to set the speed.
     */
    @FXML
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

        }, 10, 3000);
    }

    /*
     * Populates arraylists, Client Name, CashHolding, and totalWorth shares accordingly.
     * Creates a new instance of ClientData by calling the object and filling in 'dummy data'.
     * Overrites the dummy data for the first elements in the arraylists.
     * Sends the object.
     * Keeps repeating till ClientNames.Size() has been reached.
     * @return List of ClientDatas.
     */
    @FXML
    private List<ClientData> clientDataList() {
        List<String> clientNames = new ArrayList<>();
        clientNames.addAll(sim.getClientNames());


        List<Double> cashHolding = new ArrayList<>();
        cashHolding.addAll(sim.getCashHolding());

        List<Double> totalWorth = new ArrayList<>(); //Wealth
        totalWorth.addAll(sim.getTotalWorth());

        List<ClientData> clientData = new ArrayList<>();
        DecimalFormat df = new DecimalFormat("#");
        df.setMaximumFractionDigits(2);

        for(int i = 0; i < clientNames.size(); i++) {
            String getClientNames = clientNames.get(i);
            Double getCashHoldings = cashHolding.get(i);
            Double getTotalWorths = totalWorth.get(i);
            ClientData client = new ClientData("Test", "test", "test","3");//Creating a object  per row
            client.setClient(getClientNames);
            client.setCashHolding(df.format(getCashHoldings));
            client.setWealth(df.format(getTotalWorths));
            clientData.add(client);
        }
        clientNames.clear();
        cashHolding.clear();
        totalWorth.clear();
        return clientData;
    }

    /*
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

    /*
     * Uses a library from Github by PlusHaze: https://github.com/PlusHaze/TrayNotification
     * Uses the timer from the company timer, to save memory as it's not needed to have an independent timer.
     * Displays a fun fact  regarding about  stock markets.
     * A user may never get a popup notification, but the chances of that is low.
     *
     */
    private void funFacts() {
        TrayNotification tray = new TrayNotification();
        tray.setRectangleFill(Paint.valueOf("Black"));
        tray.setAnimationType(POPUP);
        tray.setMessage("When the net total worth of a market  rises over time") ;
        tray.setTitle("Welcome! FUN FACT A - Bull Market is...");
        tray.showAndDismiss(Duration.seconds(7));
        tray.showAndWait();
    }
}