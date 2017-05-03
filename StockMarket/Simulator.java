package StockMarket;

import java.io.*;
import java.text.*;
import java.util.*;

/**
 * Represents the Stock Market & Stock Exchange, and therefore handles the buying and selling of all Shares between Traders.
 * Reads initial data about clients, companies and events and instantiates all required classes based on this data, ready
 * for the simulation to begin. Keeps track of the date, time and events that occur, and responds to these events when
 * the time is right.
 *
 * @Author 146803
 * @Version 25/04/2017
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
        System.out.println("Creating Simulator");
        calendar = new GregorianCalendar(2017, calendar.JANUARY, 2, 9, 0);
        numberOfShares = new HashMap<>();
        traders = new ArrayList<>();
        events = new ArrayList<>();
        marketType = 0;
        initialiseData();
        calculateShareIndex();
        yesterdayShareIndex = shareIndex;
        initialiseEvents();
    }

    /**
     * Runs the simulation for 1 year (until 31/01/2017, 17:00) - Every 15 minutes, traders buy & sell shares & the share index is adjusted (along with
     * share prices & net worths for companies). Skips over hours & days where the Stock Market is closed
     * (17:00 - 09:00 Monday - Friday & all day Saturday - Sunday, Christmas Day, Boxing Day and Good Friday).
     * @param duration The number of minutes you wish the simulation to take (how fast you wish data to be updated).
     */
    public void runSimulation(int duration) {
        System.out.println("Running Simulation for " + duration + " minutes.");
        while(calendar.getTime().before(END_DATE)) {
            System.out.println("The date is now " + calendar.getTime());
            // Update the marketType counter.
            if(yesterdayShareIndex < shareIndex) {
                System.out.println("The market has grown since yesterday.");
                marketType++;
            } else if(yesterdayShareIndex > shareIndex) {
                System.out.println("The market has shrunk since yesterday.");
                marketType--;
            }
            yesterdayShareIndex = shareIndex;
            // Switch trader modes.
            for(Trader t : traders) {
                t.switchMode();
            }
            // Perform run15Mins until trading closes for the day.
            Date endOfDay = getEndOfDay();
            while(calendar.getTime().before(endOfDay)) {
                System.out.println("The time is now " + calendar.getTime());
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
                } else if(calendar.getTime().equals(eventInProgress.getEndDateTime())) { // There is already an event in progress.
                    System.out.println("The event is over!");
                    eventInProgress = null;
                    for(Trader t : traders) {
                        t.setEvent(null);
                        t.setMode(RandomTrader.BALANCED);
                    }
                }
                run15Mins();
                // Handles making the simulation run for a duration (in minutes).
                try {
                    System.out.println("Time to wait...");
                    Thread.sleep((duration * 60000) / (365 * 7 * 4)); // 365 days in the year * 7 hours open a day * 4 times an hour.
                    System.out.println("Waiting is over!");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // Shouldn't occur.
                }
            }
            // End of trading day has been reached, the date must now be changed to the 9:00AM on the next working day.
            GregorianCalendar nextDayCal = new GregorianCalendar();
            nextDayCal.setTime(calendar.getTime());
            nextDayCal.add(nextDayCal.HOUR_OF_DAY, 17);
            System.out.println("The next day is: " + nextDayCal.getTime());
            if(nextDayCal.get(nextDayCal.DAY_OF_WEEK) == nextDayCal.SATURDAY) {
                nextDayCal.add(nextDayCal.DATE, 2); // Skips over Sunday.
            } else if(nextDayCal.getTime().equals(GOOD_FRIDAY)) {
                nextDayCal.add(nextDayCal.DATE, 4);
            } else if(nextDayCal.getTime().equals(CHRISTMAS_DAY)) {
                nextDayCal.add(nextDayCal.DATE, 2);
            }
            calendar.setTime(nextDayCal.getTime());
        }
    }

    private void initialiseData() {
        System.out.println("Initializing clients & shares...");
        try {
            ArrayList<Portfolio> portfolios = new ArrayList<>();
            BufferedReader br = new BufferedReader(new FileReader("InitialDataV2.csv"));
            String line;
            while ((line = br.readLine()) != null) {
                String[] row = line.split(",");
                if(row.length == SIZE_DATA && row[0].length() == 0) { // Contains the names of the Clients.
                    for(int i = 0; i < SIZE_DATA - 1; i++) { // Final column is Total Shares Issued.
                        if(row[i].length() != 0) {
                            System.out.println("Creating a portfolio for " + row[i]);
                            portfolios.add(new Portfolio(row[i]));
                        }
                    }
                } else if(row.length == SIZE_DATA) { // This is a column of data.
                    int j = 0;
                    int totalShares = 0;
                    for(int i = 4; i < SIZE_DATA - 1; i++) {
                        if(row[i].length() != 0) {
                            ArrayList<Share> shares = new ArrayList<>();
                            System.out.println("Adding shares for company " + row[0] + " to " + portfolios.get(j).getClientName() + "'s portfolio.");
                            for(int k = 0; k < Integer.parseInt(row[i]); k++) {
                                shares.add(new Share(row[0], row[2], Integer.parseInt(row[3])));
                            }
                            portfolios.get(j).addShares(shares);
                            totalShares += Integer.parseInt(row[i]);
                            j++;
                        }
                    }
                    numberOfShares.put(row[0], totalShares);
                } else if(row.length > 0 && row[0].equals("CASH HOLDING (Pounds)")) { // I know, really ugly hard-coded way of doing this, I'm sorry.
                    int j = 0;
                    for(int i = 4; i < SIZE_DATA - 2; i++) {
                        if(row[i].length() != 0) {
                            System.out.println(portfolios.get(j).getClientName() + " has £" + row[i]);
                            portfolios.get(j).setCashHolding(Integer.parseInt(row[i]));
                            j++;
                        }
                    }
                }
            }
            // Initialise Traders.
            System.out.println("Initializing traders...");
            ArrayList<Portfolio> port = new ArrayList<>(); //Made port a field
            port.add(portfolios.get(0)); // Norbert DaVinci.
            port.add(portfolios.get(7)); // Justine Thyme.
            portfolios.remove(7);
            portfolios.remove(0);
            traders.add(new RandomTrader(port));
            while(portfolios.size() >= 2) {
                port = new ArrayList<>();
                port.add(portfolios.get(0));
                portfolios.remove(0);
                port.add(portfolios.get(0)); // As we have just removed index 0, there is a new element at the front of the list.
                portfolios.remove(0);
                traders.add(new RandomTrader(port));
            }
            if(portfolios.size() != 0) { // Must be one 'left over' portfolio.
                traders.add(new RandomTrader(portfolios));
            }
            System.out.println("There are now " + traders.size() + " traders!");
        } catch(IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    private void initialiseEvents() {
        System.out.println("Initializing events...");
        try {
            BufferedReader br = new BufferedReader(new FileReader("ExternalEventsData.csv"));
            String line;
            while((line = br.readLine()) != null) {
                String[] row = line.split(",");
                if(row.length == SIZE_EVENTS && !row[0].equals("Date")) { // i.e. not the header.
                    System.out.println("Adding event '" + row[2] + "'.");
                    events.add(new Event(new SimpleDateFormat("MMM dd yyyy HH:mm").parse(row[0] + " " + row[2], new ParsePosition(0)), row[4], row[15]));
                }
            }
            System.out.println("There are now " + events.size() + " events!");
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }

    }

    //TODO: TEST THIS METHOD.
    private void run15Mins() {
        System.out.println("Running one 15 minute cycle...");
        HashMap<Trader, ArrayList<Share>> toBeSold = new HashMap<>();
        HashMap<Trader, HashMap<String, Integer>> toBeBought = new HashMap<>(); // Inner HashMap maps Company Name to number of shares sought for purchase.
        HashMap<String, Integer> buyTotals = new HashMap<>();
        HashMap<String, Integer> sellTotals = new HashMap<>();
        for(String companyName : numberOfShares.keySet()) {
            buyTotals.put(companyName, 0);
            sellTotals.put(companyName, 0);
        }
        // Get what everyone wants to buy & sell.
        System.out.println("Traders buy and sell...");
        for(Trader t : traders) {
            HashMap<String, Integer> traderBuys = t.buy(new ArrayList<String>(numberOfShares.keySet()));
            toBeBought.put(t, traderBuys);
            for(String companyName : traderBuys.keySet()) {
                buyTotals.put(companyName, (buyTotals.get(companyName) + traderBuys.get(companyName)));
            }
            ArrayList<Share> shares = t.sell();
            toBeSold.put(t, shares);
            for(Share s : shares) {
                sellTotals.put(s.getCompanyName(), (sellTotals.get(s.getCompanyName()) + 1));
            }
        }
        System.out.println("Calculating shares to distribute...");
        // Work out how many shares are being sold/bought.
        for(String companyName : numberOfShares.keySet()) {
            ArrayList<Share> sharesForSale = new ArrayList<>();
            ArrayList<Share> sharesBought;
            int buyTotal = buyTotals.get(companyName);
            int sellTotal = sellTotals.get(companyName);
            System.out.println("Total requested to be bought, sold for " + companyName + ": " + buyTotal + ", " + sellTotal);
            if(buyTotal < sellTotal) { // Supply > Demand.
                System.out.println("Supply > Demand for " + companyName);
                for(Trader t : traders) {
                    ArrayList<Share> sharesOfCompany = new ArrayList<>();
                    for(Share s : toBeSold.get(t)) {
                        if(s.getCompanyName().equals(companyName)) {
                            sharesOfCompany.add(s);
                        }
                    }
                    int sharesSold = buyTotal * (sharesOfCompany.size() / sellTotal);
                    sharesForSale.addAll(sharesOfCompany.subList(0, sharesSold));
                    t.returnShares(new ArrayList<>(sharesOfCompany.subList(sharesSold, sharesOfCompany.size())), companyName);
                }
                // By this point sharesForSale should exactly equal the total number of shares sought for purchase (for this company).
                for(Trader t : traders) {
                    sharesBought = new ArrayList<>();
                    for(int i = 0; i < toBeBought.get(t).get(companyName); i++) {
                        if(i < sharesForSale.size()) { // Could occur that there are a couple more in toBeBought than in sharesRemoved due to rounding down for sharesSold.
                            sharesBought.add(sharesForSale.get(i));
                        }
                    }
                    t.addNewShares(sharesBought);
                    sharesForSale.remove(sharesBought);
                }
                changeSharePrice(companyName, buyTotal - sellTotal);
            } else if(buyTotal > sellTotal) { // Supply < Demand.
                System.out.println("Supply < Demand for " + companyName);
                for(Trader t : traders) {
                    for(Share s : toBeSold.get(t)) {
                        if(s.getCompanyName().equals(companyName)) {
                            sharesForSale.add(s);
                        }
                    }
                }
                for(Trader t : traders) {
                    int sharesPurchased = sellTotal * (toBeBought.get(t).get(companyName) / buyTotal);
                    if(sharesPurchased < sharesForSale.size()) { // Stops an error if there was rounding in sharesPurchased calculation.
                        sharesBought = new ArrayList<>(sharesForSale.subList(0, sharesPurchased));
                    } else {
                        sharesBought = sharesForSale;
                    }
                    t.addNewShares(sharesBought);
                    sharesForSale.remove(sharesBought);
                }
                changeSharePrice(companyName, buyTotal - sellTotal);
            } else { // Supply = Demand.
                System.out.println("Supply = Demand for " + companyName);
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
                    sharesBought = new ArrayList<>(sharesForSale.subList(0, toBeBought.get(t).get(companyName)));
                    t.addNewShares(sharesBought);
                    sharesForSale.remove(sharesBought);
                }
            }
        }
        // Check to see if any traders have sold all shares for a particular company-client combination.
        for(Trader t : traders) {
            t.checkTrackers();
        }
        calculateShareIndex();
        calendar.add(calendar.MINUTE, 15);
    }

    private void calculateShareIndex() {
        System.out.println("Changing the share index...");
        int newShareIndex = 0;
        for(String companyName : numberOfShares.keySet()) {
            boolean found = false;
            int i = 0;
            while(!found && i < traders.size()) {
                Trader t = traders.get(i);
                int j = 0;
                while(!found && j < t.getPortfolios().size()) {
                    Portfolio p = t.getPortfolios().get(j);
                    int k = 0;
                    while(!found && k < p.getShares().size()) {
                        Share s = p.getShares().get(k);
                        if(s.getCompanyName().equals(companyName)) {
                            newShareIndex += s.getSharePrice();
                            found = true;
                        }
                        k++;
                    }
                    j++;
                }
                i++;
            }
        }
        shareIndex = newShareIndex / numberOfShares.size();
        System.out.println("Share index is now " + shareIndex);
    }

    // excess will be negative when Supply > Demand.
    private void changeSharePrice(String companyName, int excess) {
        System.out.println("Changing the share price for " + companyName + ", using an excess of " + excess);
        int newSharePrice = 0;
        for(Trader t : traders) {
            for(Portfolio p : t.getPortfolios()) {
                for(Share s : p.getShares()) {
                    if(s.getCompanyName().equals(companyName)) {
                        newSharePrice = (excess / numberOfShares.get(companyName)) * s.getSharePrice();
                        s.setSharePrice(newSharePrice);
                    }
                }
            }
        }
        System.out.println("Setting the share price of " + companyName + " to " + newSharePrice + "...");
        if(newSharePrice <= 0) { // Company is worthless. They must be removed from the simulation.
            System.out.println(companyName + " is worthless!");
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

    /* Does not actually remove all shares for a client, but sets the boolean sellAll in their portfolio to true, alerting
     * their trader that they must attempt to sell all those shares every cycle. */
    public void leaveSimulation(String clientName) {
        System.out.println(clientName + " wishes to leave the simulation.");
        for(Trader t : traders) {
            for(Portfolio p : t.getPortfolios()) {
                if(p.getClientName().equals(clientName)) {
                    p.setSellAll();
                    return;
                }
            }
        }
    }

    private Date getEndOfDay() {
        GregorianCalendar dayCal = new GregorianCalendar();
        dayCal.setTime(calendar.getTime());
        dayCal.add(dayCal.HOUR_OF_DAY, 7);
        System.out.println("End of day = " + dayCal.getTime());
        return dayCal.getTime();
    }

    private void removeAllShares(String companyName) {
        for(Trader t : traders) {
            for(Portfolio p : t.getPortfolios()) {
                System.out.println("Removing all shares of " + companyName + " from " + p.getClientName());
                p.removeAllShares(companyName);
            }
        }
    }

    /**
     * Given the name of a company, returns its share price.
     * @param companyName An (exact) string representation of a company's name.
     * @return The share price of the company, or -1 if the company does not exist.
     */
    public int getSharePrice(String companyName) {
        for(Trader t : traders) {
            for(Portfolio p : t.getPortfolios()) {
                for(Share s: p.getShares()) {
                    if(s.getCompanyName().equals(companyName)) {
                        return s.getSharePrice();
                    }
                }
            }
        }
        return -1; // An error has occurred if this step is reached - no share matched companyName.
    }

    /**
     * Given the name of a company, returns its net worth.
     * @param companyName An (exact) string representation of the company's name.
     * @return The net worth (share price * number of shares) for the company.
     */
    public int getNetWorth(String companyName) {
        int netWorth = 0;
        for(Trader t : traders) {
            for(Portfolio p : t.getPortfolios()) {
                for(Share s: p.getShares()) {
                    if(s.getCompanyName().equals(companyName)) {
                        netWorth += s.getSharePrice();
                    }
                }
            }
        }
        return netWorth;
    }

    /**
     * Returns the time portion of the current date & time in the simulation.
     * @return A string representation of the current time (HH:MM:SS).
     */
    public String getTime() {
        Format formatter = new SimpleDateFormat("HH:mm:ss");
        String s = formatter.format( calendar.getTime());
        return s;
    }

    /**
     * Returns the date portion of the current date & time in the simulation.
     * @return A string representation of the current date (DD-MM-YYYY).
     */
    public String getDate(){
        Format formatter = new SimpleDateFormat("dd-MM-yyyy");
        String s = formatter.format(calendar.getTime());
        return s;
    }

    /**
     * Returns the event currently in progress (if exists).
     * @return The event in progress at this date & time, or null if no such event exists.
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
     * Provides a list of all portfolios (clients & information relating to them) within the simulation.
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
    public List<Integer> getCashHolding(){
        ArrayList<Integer> portfolios = new ArrayList<>();
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
    public List<Integer> getTotalWorth(){
        ArrayList<Integer> portfolios = new ArrayList<>();
        for(Trader t : traders) {
            for(Portfolio p : t.getPortfolios()){
                portfolios.add(p.getTotalWorth());
            }
        }
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

}
