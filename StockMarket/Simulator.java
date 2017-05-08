package StockMarket;

import java.io.*;
import java.text.*;
import java.util.*;

/**
 * Represents the Stock Market and Stock Exchange, and therefore handles the buying and selling of all Shares between Traders.
 * Reads initial data about clients, companies and events and instantiates all required classes based on this data, ready
 * for the simulation to begin. Keeps track of the date, time and events that occur, and responds to these events when
 * the time is right.
 *
 * @author 146803
 * @version 06/05/2017
 */
public class Simulator {
    private GregorianCalendar calendar;

    private ArrayList<Trader> traders;
    private ArrayList<Event> events;
    private HashMap<String, Integer> numberOfShares;
    private double shareIndex; // in pence.
    private int marketType;
    private double yesterdayShareIndex; // for calculating whether it has risen/fallen since yesterday.
    private Event eventInProgress;
    private int duration;
    private HashMap<String, Double> sharePrices;
    protected static final int SIZE_DATA = 19;
    private static final int SIZE_EVENTS = 16;
    private static final Date END_DATE = new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2018", new ParsePosition(0)); // 1st Jan 2018 at midnight.
    private static final Date GOOD_FRIDAY = new SimpleDateFormat("dd/MM/yyyy kk:mm").parse("14/04/2017 09:00", new ParsePosition(0));
    private static final Date CHRISTMAS_DAY = new SimpleDateFormat("dd/MM/yyyy kk:mm").parse("25/12/2017 09:00", new ParsePosition(0));
    // No need for Boxing Day or Easter Monday, as we will just skip over those days when we reach Christmas Day/Good Friday.

    /**
     * Instantiates the Simulation, setting the date to 01/01/2017, reads data about clients and companies from
     * InitialDataV2.csv, and imports data about events from ExternalEventsData.csv. After instantiation, the simulation
     * is completely ready to be run.
     */
    public Simulator() {
        //System.out.println("Creating Simulator");
        this.duration = duration;
        calendar = new GregorianCalendar(2017, calendar.JANUARY, 2, 9, 0); // 01/01/2017 is a Sunday.
        numberOfShares = new HashMap<>();
        sharePrices = new HashMap<>();
        traders = new ArrayList<>();
        events = new ArrayList<>();
        marketType = 0;
        initialiseData();
        calculateShareIndex();
        yesterdayShareIndex = shareIndex;
        initialiseEvents();
    }

    /**
     * Runs the simulation for 1 year (until 31/01/2017, 17:00) - Every 15 minutes, traders buy and sell shares and the share index is adjusted (along with
     * share prices and net worths for companies). Skips over hours and days where the Stock Market is closed
     * (17:00 - 09:00 Monday - Friday and all day Saturday - Sunday, Christmas Day, Boxing Day and Good Friday).
     */
    public void runSimulation() {
        //System.out.println("Running Simulation for " + duration + " minutes.");
        while(calendar.getTime().before(END_DATE)) {
            System.out.println("The date is now " + calendar.getTime());
            // Update the marketType counter.
            if(yesterdayShareIndex < shareIndex) {
                //System.out.println("The market has grown since yesterday.");
                if(marketType < 3) {
                    marketType++;
                }
            } else if(yesterdayShareIndex > shareIndex) {
                //System.out.println("The market has shrunk since yesterday.");
                if(marketType > -3) {
                    marketType--;
                }
            }
            yesterdayShareIndex = shareIndex;
            // Switch trader modes.
            for(Trader t : traders) {
                t.switchMode();
            }
            // Perform run15Mins until trading closes for the day.
            Date endOfDay = getEndOfDay();
            while(calendar.getTime().before(endOfDay)) {
                //System.out.println("The time is now " + calendar.getTime());
                if(eventInProgress == null) {
                    if ((eventInProgress = checkEvent()) != null) { // checkEvent returns an Event when it is the starting time of that event.
                        System.out.println("An event has started: " + eventInProgress.getMessage());
                        for (Trader t : traders) {
                            t.setEvent(eventInProgress.getName());
                            if (eventInProgress.isBuy()) {
                                t.setMode(RandomTrader.EVENTBUYER);
                            } else {
                                t.setMode(RandomTrader.EVENTSELLER);
                            }
                        }
                    }
                } else if(calendar.getTime().equals(eventInProgress.getEndDateTime())) {
                    System.out.println("The event is over!");
                    eventInProgress = null;
                    for(Trader t : traders) {
                        t.setEvent(null);
                        t.setMode(RandomTrader.BALANCED);
                    }
                }
                run15Mins();
            }
            // End of trading day has been reached, the date must now be changed to the 9:00AM on the next working day.
            GregorianCalendar nextDayCal = new GregorianCalendar();
            nextDayCal.setTime(calendar.getTime());
            nextDayCal.add(nextDayCal.HOUR_OF_DAY, 17);
            //System.out.println("The next day is: " + nextDayCal.getTime());
            if(nextDayCal.get(nextDayCal.DAY_OF_WEEK) == nextDayCal.SATURDAY) {
                nextDayCal.add(nextDayCal.DATE, 2); // Skips over Sunday.
            }
            if(nextDayCal.getTime().compareTo(GOOD_FRIDAY) == 0) {
                nextDayCal.add(nextDayCal.DATE, 4);
            } else if(nextDayCal.getTime().compareTo(CHRISTMAS_DAY) == 0) {
                nextDayCal.add(nextDayCal.DATE, 2);
            }
            calendar.setTime(nextDayCal.getTime());
            System.out.println("The traders now have " + totalSharesInPortfolios() + ", there should be " + totalSharesForCompanies() + " shares.");
        }
    }

    // Imports data from the InitialDataV2.csv file, creating the Shares, Portfolios and Traders.
    private void initialiseData() {
        ArrayList<Share> allShares = new ArrayList<>();
        //System.out.println("Initializing clients and shares...");
        try {
            ArrayList<Portfolio> portfolios = new ArrayList<>();
            BufferedReader br = new BufferedReader(new FileReader("InitialDataV2.csv"));
            String line;
            while ((line = br.readLine()) != null) {
                String[] row = line.split(",");
                if(row.length == SIZE_DATA && row[0].length() == 0) { // Contains the names of the Clients.
                    for(int i = 0; i < SIZE_DATA - 1; i++) { // Final column is Total Shares Issued.
                        if(row[i].length() != 0) {
                            //System.out.println("Creating a portfolio for " + row[i]);
                            portfolios.add(new Portfolio(row[i]));
                        }
                    }
                } else if(row.length == SIZE_DATA) { // This is a column of data.
                    int j = 0;
                    int totalShares = 0;
                    for(int i = 4; i < SIZE_DATA - 1; i++) {
                        if(row[i].length() != 0) {
                            ArrayList<Share> shares = new ArrayList<>();
                            //System.out.println("Adding shares for company " + row[0] + " to " + portfolios.get(j).getClientName() + "'s portfolio.");
                            sharePrices.put(row[0], (double) Integer.parseInt(row[3]));
                            for(int k = 0; k < Integer.parseInt(row[i]); k++) {
                                Share s = new Share(row[0], row[2], (double)Integer.parseInt(row[3]));
                                shares.add(s);
                                allShares.add(s);
                            }
                            portfolios.get(j).addSharesInit(shares);
                            totalShares += Integer.parseInt(row[i]);
                            j++;
                        }
                    }
                    numberOfShares.put(row[0], totalShares);
                } else if(row.length > 0 && row[0].equals("CASH HOLDING (Pounds)")) { // I know, really ugly hard-coded way of doing this, I'm sorry.
                    int j = 0;
                    for(int i = 4; i < SIZE_DATA - 2; i++) {
                        if(row[i].length() != 0) {
                            //System.out.println(portfolios.get(j).getClientName() + " has £" + row[i]);
                            portfolios.get(j).setCashHolding(Integer.parseInt(row[i]));
                            j++;
                        }
                    }
                }
            }
            // Initialise Traders.
            //System.out.println("Initializing traders...");
            ArrayList<Portfolio> port = new ArrayList<>();
            port.add(portfolios.get(0)); // Norbert DaVinci.
            port.add(portfolios.get(7)); // Justine Thyme.
            portfolios.remove(7);
            portfolios.remove(0);
            traders.add(new IntelligentTrader(port, allShares));
            while(portfolios.size() >= 2) {
                port = new ArrayList<>();
                port.add(portfolios.get(0));
                portfolios.remove(0);
                port.add(portfolios.get(0)); // As we have just removed index 0, there is a new element at the front of the list.
                portfolios.remove(0);
                traders.add(new RandomTrader(port, allShares));
            }
            if(portfolios.size() != 0) { // Must be one 'left over' portfolio.
                traders.add(new RandomTrader(portfolios, allShares));
            }
            //System.out.println("There are now " + traders.size() + " traders!");
            //System.out.println("There are " + sharePrices.size() + " companies.");
        } catch(IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    // Creates the events from the ExternalEventsData.csv file.
    private void initialiseEvents() {
        //System.out.println("Initializing events...");
        try {
            BufferedReader br = new BufferedReader(new FileReader("ExternalEventsData.csv"));
            String line;
            while((line = br.readLine()) != null) {
                String[] row = line.split(",");
                if(row.length == SIZE_EVENTS && !row[0].equals("Date")) { // i.e. not the header.
                    //System.out.println("Adding event '" + row[2] + "'.");
                    events.add(new Event(new SimpleDateFormat("MMM dd yyyy HH:mm").parse(row[0] + " " + row[2], new ParsePosition(0)), row[4], row[15]));
                }
            }
            //System.out.println("There are now " + events.size() + " events!");
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }

    }

    void run15Mins() {
        //System.out.println("Running one 15 minute cycle...");
        HashMap<Trader, ArrayList<Share>> toBeSold = new HashMap<>();
        HashMap<Trader, HashMap<String, Integer>> toBeBought = new HashMap<>(); // Inner HashMap maps Company Name to number of shares sought for purchase.
        HashMap<String, Integer> buyTotals = new HashMap<>();
        HashMap<String, Integer> sellTotals = new HashMap<>();
        for(String companyName : numberOfShares.keySet()) {
            buyTotals.put(companyName, 0);
            sellTotals.put(companyName, 0);
        }
        // Get what everyone wants to buy and sell.
        //System.out.println("Traders buy and sell...");
        int totalPutUpForSale = 0;
        for(Trader t : traders) {
            HashMap<String, Integer> traderBuys = t.buy(sharePrices);
            toBeBought.put(t, traderBuys);
            for(String companyName : traderBuys.keySet()) {
                buyTotals.put(companyName, (buyTotals.get(companyName) + traderBuys.get(companyName)));
            }
            ArrayList<Share> shares = t.sell(sharePrices);
            totalPutUpForSale += shares.size();
            toBeSold.put(t, shares);
            for(Share s : shares) {
                if(sellTotals.containsKey(s.getCompanyName())) {
                    sellTotals.put(s.getCompanyName(), (sellTotals.get(s.getCompanyName()) + 1));
                } else {
                    sellTotals.put(s.getCompanyName(), 1);
                }
            }
        }
        //System.out.println("Calculating shares to distribute...");
        // Work out how many shares are being sold/bought.
        int numberReturned = 0;
        int numberAdded = 0;
        for(String companyName : numberOfShares.keySet()) {
            ArrayList<Share> sharesForSale = new ArrayList<>();
            ArrayList<Share> sharesBought;
            int buyTotal = buyTotals.get(companyName);
            int sellTotal = sellTotals.get(companyName);
            //System.out.println("Total requested to be bought, sold for " + companyName + ": " + buyTotal + ", " + sellTotal);
            if(buyTotal < sellTotal) { // Supply > Demand.
                //System.out.println("Supply > Demand for " + companyName);
                for(Trader t : traders) {
                    ArrayList<Share> sharesOfCompany = new ArrayList<>();
                    for(Share s : toBeSold.get(t)) {
                        if(s.getCompanyName().equals(companyName)) {
                            sharesOfCompany.add(s);
                        }
                    }
                    int sharesSold = (int)Math.round((double)buyTotal * (double)sharesOfCompany.size() / (double)sellTotal);
                    sharesForSale.addAll(new ArrayList<>(sharesOfCompany.subList(0, sharesSold)));
                    ArrayList<Share> returnedShares = new ArrayList<>(sharesOfCompany.subList(sharesSold, sharesOfCompany.size()));
                    numberReturned += returnedShares.size();
                    t.returnShares(returnedShares, companyName, sellTotal);
                }
                // By this point sharesForSale should exactly equal the total number of shares sought for purchase (for this company).
                int leftOver = sharesForSale.size() - buyTotal;
                if(leftOver > 0) {
                    for(int i = 0; i < leftOver; i++) {
                        boolean returnedOne = false;
                        while(!returnedOne) {
                            Trader t = traders.get(new Random().nextInt(traders.size()));
                            if(toBeSold.get(t).contains(sharesForSale.get(0))) {
                                ArrayList<Share> temp = new ArrayList<>();
                                temp.add(sharesForSale.remove(0));
                                t.returnShares(temp, companyName, sellTotal);
                                numberReturned++;
                                returnedOne = true;
                            }
                        }
                    }
                } else if(leftOver < 0) {
                    buyTotal += leftOver;
                }
                Collections.shuffle(traders); // So the last trader isn't always getting fewer.
                for(Trader t : traders) {
                    sharesBought = new ArrayList<>();
                    try {
                        int numberBought = toBeBought.get(t).get(companyName);
                        while(sharesBought.size() < numberBought && !sharesForSale.isEmpty()) {
                            sharesBought.add(sharesForSale.remove(0));
                        }
                        numberAdded += sharesBought.size();
                        t.addNewShares(sharesBought, companyName, buyTotal);
                    } catch(NullPointerException e) {/* Trader requested none of this share to buy. */}
                }
                changeSharePrice(companyName, buyTotal - sellTotal);
            } else if(buyTotal > sellTotal) { // Supply < Demand.
                //System.out.println("Supply < Demand for " + companyName);
                for(Trader t : traders) {
                    for(Share s : toBeSold.get(t)) {
                        if(s.getCompanyName().equals(companyName)) {
                            sharesForSale.add(s);
                        }
                    }
                }
                Collections.shuffle(traders);
                for(Trader t : traders) {
                    try {
                        int numberBought = toBeBought.get(t).get(companyName);
                        //System.out.println("The trader managing " + t.getPortfolios().get(0).getClientName() + " requested " + numberBought + " shares of " + companyName);
                        int sharesPurchased = (int) Math.round((double)sellTotal * ((double)numberBought / (double)buyTotal));
                        if (sharesPurchased < sharesForSale.size()) {
                            sharesBought = new ArrayList<>(sharesForSale.subList(0, sharesPurchased));
                            sharesForSale.removeAll(sharesBought);
                        } else {
                            sharesBought = new ArrayList<>(sharesForSale); // Stops an error if there was rounding in sharesPurchased calculation.
                            sharesForSale.clear();
                        }
                        numberAdded += sharesBought.size();
                        t.addNewShares(sharesBought, companyName, buyTotal);
                    } catch(NullPointerException e) {/* This trader requested no shares for this company*/}
                }
                while(!sharesForSale.isEmpty()) {
                    //System.out.println("Returning a share to even things out...");
                    Trader t = traders.get(new Random().nextInt(traders.size()));
                    if(toBeSold.get(t).contains(sharesForSale.get(0))) {
                        ArrayList<Share> temp = new ArrayList<>();
                        temp.add(sharesForSale.remove(0));
                        t.returnShares(temp, companyName, sellTotal);
                        numberReturned++;
                        sellTotal--;
                    }
                }
                changeSharePrice(companyName, buyTotal - sellTotal);
            } else { // Supply = Demand.
                //System.out.println("Supply = Demand for " + companyName);
                // Calculate the shares that are up for sale for this company.
                for(Trader t : traders) {
                    for(Share s : toBeSold.get(t)) {
                        if(s.getCompanyName().equals(companyName)) {
                            sharesForSale.add(s);
                        }
                    }
                }
                // Give the trader the number of shares (from sharesForSale) that they asked for - there are exactly enough.
                for(Trader t : traders) {
                    try {
                        int numberBought = toBeBought.get(t).get(companyName);
                        if (sharesForSale.size() > numberBought) {
                            sharesBought = new ArrayList<>(sharesForSale.subList(0, numberBought));
                            sharesForSale.removeAll(sharesBought);
                            numberAdded += sharesBought.size();
                            t.addNewShares(sharesBought, companyName, buyTotal);
                        } else {
                            numberAdded += sharesForSale.size();
                            t.addNewShares(sharesForSale, companyName, buyTotal);
                            sharesForSale.clear();
                        }
                    } catch(NullPointerException e) {/* Trader requested no shares for this company. */}
                }
            }
        }
        // Check to see if any traders have sold all shares for a particular company-client combination.
        for(Trader t : traders) {
            t.checkTrackers();
        }
        // Removes any companies that became worthless this cycle.
        ArrayList<String> companyNames = new ArrayList<>(numberOfShares.keySet());
        for(String companyName : companyNames) {
            if(numberOfShares.get(companyName) == 0) {
                numberOfShares.remove(companyName);
                sharePrices.remove(companyName);
            }
        }
        calculateShareIndex();
        calendar.add(calendar.MINUTE, 15);
        //System.out.println("Difference = " + (numberAdded + numberReturned - totalPutUpForSale));
    }

    private void calculateShareIndex() {
        //System.out.println("Changing the share index...");
        int newShareIndex = 0;
        for(double sharePrice : sharePrices.values()) {
            newShareIndex += sharePrice;
        }
        shareIndex = (double)newShareIndex / (double)numberOfShares.size();
        //System.out.println("Share index is now " + shareIndex);
    }

    // excess will be negative when Supply > Demand.
    private void changeSharePrice(String companyName, int excess) {
        //System.out.println("Changing the share price for " + companyName + ", using an excess of " + excess);
        double newSharePrice = sharePrices.get(companyName) + (((double)excess / (double)numberOfShares.get(companyName)) * sharePrices.get(companyName));
        sharePrices.put(companyName, newSharePrice);
        for(Trader t : traders) {
            for(Portfolio p : t.getPortfolios()) {
                for(Share s : p.getShares()) {
                    if(s.getCompanyName().equals(companyName)) {
                        s.setSharePrice(newSharePrice);
                    }
                }
            }
        }
        //System.out.println("The share price of " + companyName + " is now " + newSharePrice + ".");
        if(newSharePrice <= 1) { // Company is worthless. They must be removed from the simulation.
            //System.out.println(companyName + " is worthless!");
            removeAllShares(companyName);
        }
    }

    private Event checkEvent() {
        for(Event e : events) {
            if(e.getStartDateTime().equals(calendar.getTime())) {
                return e;
            }
        }
        return null;
    }

    /**
     *  Does not actually remove all shares for a client, but sets the boolean sellAll in their portfolio to true, alerting
     * their trader that they must attempt to sell all those shares every cycle.
     * @param clientName the client that wishes to leave.
     */
    public void leaveSimulation(String clientName) {
        //System.out.println(clientName + " wishes to leave the simulation.");
        for(Trader t : traders) {
            for(Portfolio p : t.getPortfolios()) {
                if(p.getClientName().equals(clientName)) {
                    p.setSellAll();
                    return;
                }
            }
        }
    }

    Date getEndOfDay() {
        GregorianCalendar dayCal = new GregorianCalendar();
        dayCal.setTime(calendar.getTime());
        dayCal.add(dayCal.HOUR_OF_DAY, 7);
        //System.out.println("End of day = " + dayCal.getTime());
        return dayCal.getTime();
    }

    private void removeAllShares(String companyName) {
        for(Trader t : traders) {
            for(Portfolio p : t.getPortfolios()) {
                //System.out.println("Removing all shares of " + companyName + " from " + p.getClientName());
                p.removeAllShares(companyName);
            }
            t.removeTrackers(companyName);
        }
        numberOfShares.put(companyName, 0);
    }

    /**
     * Given the name of a company, returns its share price.
     * @param companyName An (exact) string representation of a company's name.
     * @return The share price of the company.
     */
    public double getSharePrice(String companyName) {
        return sharePrices.get(companyName);
    }

    /**
     * Returns the time portion of the current date and time in the simulation.
     * @return A string representation of the current time (HH:MM:SS).
     */
    public String getTime() {
        Format formatter = new SimpleDateFormat("HH:mm:ss");
        String s = formatter.format( calendar.getTime());
        return s;
    }

    /**
     * Returns the date portion of the current date and time in the simulation.
     * @return A string representation of the current date (DD-MM-YYYY).
     */
    public String getDate(){
        Format formatter = new SimpleDateFormat("dd-MM-yyyy");
        String s = formatter.format(calendar.getTime());
        return s;
    }

    /**
     * Returns the event currently in progress (if exists).
     * @return The event in progress at this date and time, or null if no such event exists.
     */
    public Event getEvent(){ return eventInProgress; }

    /**
     * Returns the share index of the stock market at this time.
     * @return The share index (sum of all share prices / number of companies).
     */
    public double getShareIndex() {
        return shareIndex;
    }

    /**
     * Returns the current market type (bear/bull/stable).
     * @return The current market type.
     */
    public String getMarketType() {
        if(marketType >= 3) { // Rising for 3+ days.
            return "Bull";
        } else if(marketType <= -3) { // Falling for 3+ days.
            return "Bear";
        } else {
            return "Stable";
        }
    }

    /**
     * Provides a list of all portfolios (clients and information relating to them) within the simulation.
     * @return A list of all Portfolio instances in the simulation.
     */
    public List<Portfolio> getPortfolios(){
        ArrayList<Portfolio> portfolios = new ArrayList<>();
        for(Trader t : traders) {
            for(Portfolio p : t.getPortfolios()){
                portfolios.add(p);
            }
        }
        return portfolios;
    }


    /**
     * Provides a list of client names within the simulation.
     * @return A list of client names from all Portfolio instances in the simulation.
     */
    public List<String> getClientNames(){
        ArrayList<String> portfolios = new ArrayList<>();
        for(Trader t : traders) {
            for(Portfolio p : t.getPortfolios()){
                portfolios.add(p.getClientName());
            }
        }
        return portfolios;
    }

    /**
     * Provides a list of Cash holdings correlating to client names within the simulation.
     * @return A list of Cash holdings from all Portfolio instances in the simulation.
     */
    public ArrayList<Double> getCashHolding(){
        ArrayList<Double> portfolios = new ArrayList<>();
        for(Trader t : traders) {
            for(Portfolio p : t.getPortfolios()){
                portfolios.add(p.getCashHolding());
            }
        }
        return portfolios;
    }

    /**
     * Provides a list of Total Worth correlating to client names within the simulation.
     * @return A list of Total Worth from all Portfolio instances in the simulation.
     */
    public ArrayList<Double> getTotalWorth(){
        ArrayList<Double> portfolios = new ArrayList<>();
        for(Trader t : traders) {
            for(Portfolio p : t.getPortfolios()){
                portfolios.add(p.getTotalWorth());
            }
        }
        System.out.println("total worth " + portfolios.toString());
        return portfolios;
    }

    /**
     * Provides a list of Total Worth correlating to client names within the simulation.
     * @return A list of Total Worth from all Portfolio instances in the simulation.
     */
    public ArrayList<ArrayList<Share>> getShares(){
        ArrayList<ArrayList<Share>> portfolios = new ArrayList<>();
        for(Trader t : traders) {
            for(Portfolio p : t.getPortfolios()){
                portfolios.add(p.getShares());
            }
        }
        return portfolios;
    }

    /**
     * Returns the names of all companies in the simulation.
     * @return A set of the names of all companies in the simulation.
     */
    public Set<String> getCompanyNames(){ return numberOfShares.keySet(); }

    /**
     * Returns the number of shares that each company has.
     * @return A list of integers representing each the number of shares that there are for each company.
     */
    public Collection<Integer> getCompanyValues(){
        return numberOfShares.values();
    }

    /**
     * Returns the dictionary from company name (String) to number of shares (int).
     * @return a HashMap from String (companyName) to int (numberOfShares).
     */
    public HashMap<String, Integer> getCompanyDetails() { return numberOfShares;}

    public int totalSharesForCompanies() {
        int total = 0;
        for(int numberOfShares : numberOfShares.values()) {
            total += numberOfShares;
        }
        return total;
    }

    // Used for checking there are the same number of shares in the portfolio as there are across the simulator's numberOfShares hashmap.
    int totalSharesInPortfolios() {
        int total = 0;
        for(Trader t : traders) {
            total += t.getShares();
        }
        return total;
    }

    /**
     * Returns the list of all traders in the simulator.
     * @return The ArrayList of Trader objects.
     */
    public ArrayList<Trader> getTraders() {
        return traders;
    }

    /**
     * Given the name of a company, returns its net worth.
     * @param companyName An (exact) string representation of the company's name.
     * @return The net worth (share price * number of shares) for the company.
     */
    public double getNetWorth(String companyName) {
        return (double) numberOfShares.get(companyName) * sharePrices.get(companyName);
    }
}