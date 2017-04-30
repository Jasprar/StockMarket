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
import javafx.stage.Stage;
import javafx.util.Duration;
import tray.notification.TrayNotification;

import java.util.*;
import java.util.stream.Collectors;

import static tray.animations.AnimationType.POPUP;

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
    MenuItem RunSim;
    @FXML
    Label dateEntry, eventEntry,timeEntry,shareEntry,marketEntry;
    @FXML
    TextArea Hardware, Food, HiTech,Property;

    @FXML
    TableView<CompanyData> companyDataTableView;
    @FXML
    TableColumn<CompanyData, String> companyName;
    @FXML
    TableColumn<CompanyData,Number> companyShares;
    @FXML
    TableColumn<CompanyData,Number> totalShares;
    @FXML
    TableView<ClientData> clientDataTableView;
    @FXML
    TableColumn<ClientData,String> client;
    @FXML
    TableColumn<ClientData,Number> clientWealth,cashHolding,clientShares,managedBy;
    @FXML
    LineChart linechart;
    @FXML
    NumberAxis x,y;
    @FXML
    Tab backEnd;
    @FXML
    Button runButton;
    @FXML
    AnchorPane ClientPane,CompanyPane;
    @FXML
    VBox frame;

    //Java fields
    int duration;

    int count;
    int clicked;
    final int COMMODOTIES_TIME = 1000;
    int TABLE_REFRESH_RATE = 6000;

    Timer timer = new Timer();
    Simulator sim = new Simulator();


    /***
     * Initializes the set up of the program
     */
    @FXML
    public void initialize(){

    }
    /**
     * Runs the simulation.
     * Pop up for the user to enter duration which gets passed in to Sim.runsim(duration).
     */
    @FXML
    public void runSimulation() throws Exception {
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

    private void globalTimer() throws Exception {


    }


    /***
     * Once run simulation duration as been entered, call these methods
     */
    @FXML
    public void callMethod() {

        commodities();
        currentTime();
        share();
        currentDate();
        MarketType();
        event();
        graph();
        globalspeedControl();
        clientTable();
        companyTable();
        backEnd();
        quiz();
    }

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

    private void globalspeedControl() {
        CompanyPane.addEventFilter(MouseEvent.MOUSE_PRESSED, e ->{
            if(e.isSecondaryButtonDown()){
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
     * Quit the program
     */
    @FXML
    public void quit() {
        System.exit(0);
    }


    /**
     * Help menubar --> menu item methods
     *
     * This following message displays a popup showing how to use.
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

    /**
     * popup message when clicked upon displaying about us.
     */
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
     * This method displays the current license agreement
     */
    @FXML
    public void License() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(null);
        alert.setContentText("I have a great message for you!");
        alert.showAndWait();
    }

    /**
     * popup message when clicked upon displaying the copyright agreement
     */
    @FXML
    public void copyRight() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Copyright");
        alert.setHeaderText(null);
        alert.setContentText("This belong to the University of Sussex");
        alert.showAndWait();
    }

    /**
     * Popup message displaying the developers names
     */
    @FXML
    public void credits() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Credits");
        alert.setHeaderText(null);
        alert.setContentText("Jasper Weymouth,Feroze Saeed,Sheel Shah,Steven Shum");
        alert.showAndWait();
    }

    /**
     * Displays the commodoties values . Updates accordingly to COMMODOTIES_TIME
     */
    public void commodities(){
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
     * Bottom panel (Below graph) --> Information panel
     * Displays the current time, updates every second.
     */
    @FXML
    public void currentTime() {
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

    public void currentDate() {
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

    public void share() {
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
     * Displays share index. Shows the share index value per every month.
     */
    public void graph() {
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
     * Back end tab, Company Table. Displays Company Name, Company values, Share Price, and total shares.
     * Retrieves the object from CompanyData and appends each row per every instance is created
     */
    public  void companyTable() {
    //   for(CompanyData s: companydataList()){


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
     * Creates a new instance of companydata and passes the data inside
     * @return CompanyData object .
     */
    private List<CompanyData> companydataList(){
        List<String> companyNames = new ArrayList<>();
        Set<String> companyNames1 =  sim.getCompanyNames();
        companyNames.addAll(companyNames1);


        List<Integer> clientNetworth = new ArrayList<>();

        List<Integer> companyValues = new ArrayList<>();
        Collection<Integer> companyValues1 = sim.getCompanyValues();
        companyValues.addAll(companyValues1);

        List<Integer> sharePrice = new ArrayList<>();
        for(String s: companyNames) {
            sharePrice.add(sim.getSharePrice(s));
            clientNetworth.add(sim.getNetWorth(s));
        }

        List<CompanyData> companyData = new ArrayList<>();

        for(int i = 0; i < companyNames.size(); i++){
            String name = companyNames.get(i);
            int sharePrices = sharePrice.get(i);
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
     * Retrieves the object from ClientData and appends each row per every instance is created
     */
    public void clientTable() {


        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    clientDataTableView.getItems().clear();
        for (ClientData s : clientdataList()) { //Needs to be changed to global timer
            clientDataTableView.getItems().add(s);
        }



                });

            }

        }, 0, TABLE_REFRESH_RATE);


        Tooltip tooltip = new Tooltip();
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
        //Currently not working
    }

    /***
     *
     * @return
     */
    private List<ClientData> clientdataList() {



        //Getting client Names and appending to list
        List<String> ClientNames = new ArrayList<>();
        ClientNames.addAll(sim.getClientNames());

        //Getting Cash holding and appending to list
        List<Integer> CashHolding = new ArrayList<>();
        Random rand = new Random();
        int x = rand.nextInt(50)+1;
        for(int i = 0; i <= 10; i++) {
            CashHolding.add(x);
        }
        //Getting total worth and appending to list
        List<Integer> TotalWorth = new ArrayList<>(); //Wealth
        TotalWorth.addAll(sim.getTotalWorth());

        //Getting Shares and appending it to a arraylist of an arraylist
        List<ArrayList<Share>> getShares = new ArrayList<>();
        getShares.addAll(sim.getShares());

        //Populating arraylist<share> from the arraylist of arraylist
        ArrayList<Share> toSingleArrayShares = new ArrayList<>();
        for(int i = 0; i < getShares.size(); ++i){
            toSingleArrayShares.addAll(getShares.get(i));

        }

        //Converting Shares List to String for the table

        List<String> Shares = toSingleArrayShares.stream()
                .map(Share::toString)
                .collect(Collectors.toList());

        //Clearing the redundant array lists
        getShares.clear();
        toSingleArrayShares.clear();


        List<ClientData> clientData = new ArrayList<>();

        for(int i = 0; i < ClientNames.size(); i++) {
            String clientNames = ClientNames.get(i);
            int cashHolding = CashHolding.get(i);
            int totalWorth = TotalWorth.get(i);
            String clientShares = Shares.get(i);
            ClientData client = new ClientData("Test", 1, 1,"3","Random");//Creating a object  per row
            client.setPFClient(clientNames);
            client.setPFCashHolding(cashHolding);
            client.setPFWealth(totalWorth);
          //  client.setPFShares(clientShares);
            clientData.add(client);
        }
        return clientData;
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
     * One time only popup when BackEnd tab is clicked upon.
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
}
