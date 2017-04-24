package StockMarket;

import sun.security.provider.SHA;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;

public class Simulator {
    private GregorianCalendar calendar;
    private ArrayList<Trader> traders;
    private ArrayList<Event> events;
    private HashMap<String, Integer> numberOfShares;
    private int shareIndex; // in pence.
    private String marketType; // Bull, Bear, Stable.
    private Event eventInProgress;
    private static final int SIZE_DATA = 19;
    private static final int SIZE_EVENTS = 16;
    private static final Date END_DATE = new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2018", new ParsePosition(0)); // 1st Jan 2018 at midnight.
    private static final Date GOOD_FRIDAY = new SimpleDateFormat("dd/MM/yyyy kk:mm").parse("14/04/2017 09:00", new ParsePosition(0));
    private static final Date CHRISTMAS_DAY = new SimpleDateFormat("dd/MM/yyyy kk:mm").parse("25/12/2017 09:00", new ParsePosition(0));
    // No need for Boxing Day or Easter Monday, as we will just skip over those days when we reach Christmas Day/Good Friday.

    public Simulator() {
        calendar = new GregorianCalendar(2017, calendar.JANUARY, 1);
        numberOfShares = new HashMap<>();
        traders = new ArrayList<>();
        events = new ArrayList<>();
        marketType = "Stable";
        initialiseData();
        calculateShareIndex();
        initialiseEvents();
    }

    public void runSimulation(int duration) {
        while(calendar.getTime().before(END_DATE)) {
            for(Trader t : traders) {
                t.switchMode();
            }
            Date endOfDay = getEndOfDay();
            while(calendar.getTime().before(endOfDay)) {
                if(eventInProgress == null) {
                    if ((eventInProgress = checkEvent()) != null) { // checkEvent returns an Event when it is the starting time of that event.
                        for (Trader t : traders) {
                            t.setEvent(eventInProgress.getName());
                            if (eventInProgress.isBuy()) {
                                t.setMode(RandomTrader.BUYER);
                            } else {
                                t.setMode(RandomTrader.SELLER);
                            }
                        }
                    }
                } else if(calendar.getTime().equals(eventInProgress.getEndDateTime())) { // There is already an event in progress.
                    eventInProgress = null;
                    for(Trader t : traders) {
                       t.setEvent(null);
                       t.switchMode();
                    }
                }
                run15Mins();
                // Handles making the simulation run for a duration (in minutes).
                try {
                    Thread.sleep((duration * 60000) / (365 * 7 * 4)); // 365 days in the year * 7 hours open a day * 4 times an hour.
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // Shouldn't occur.
                }
            }
            // End of trading day has been reached, the date must now be changed to the 9:00AM on the next working day.
            GregorianCalendar nextDayCal = new GregorianCalendar();
            nextDayCal.setTime(calendar.getTime());
            nextDayCal.add(nextDayCal.HOUR_OF_DAY, 17);
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
        try {
            ArrayList<Portfolio> portfolios = new ArrayList<>();
            BufferedReader br = new BufferedReader(new FileReader("InitialDataV2.csv"));
            String line;
            while ((line = br.readLine()) != null) {
                String[] row = line.split(",");
                if(row.length == SIZE_DATA && row[0].length() == 0) { // Contains the names of the Clients.
                    for(int i = 0; i < SIZE_DATA - 1; i++) { // Final column is Total Shares Issued.
                        if(row[i].length() != 0) {
                            portfolios.add(new Portfolio(row[i]));
                        }
                    }
                } else if(row.length == SIZE_DATA) { // This is a column of data.
                    int j = 0;
                    int totalShares = 0;
                    for(int i = 4; i < SIZE_DATA - 1; i++) {
                        if(row[i].length() != 0) {
                            ArrayList<Share> shares = new ArrayList<>();
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
                            portfolios.get(j).setCashHolding(Integer.parseInt(row[i]));
                            j++;
                        }
                    }
                }
            }
            // Initialise Traders.
            ArrayList<Portfolio> port = new ArrayList<>();
            port.add(portfolios.get(0)); // Norbert DaVinci.
            port.add(portfolios.get(7)); // Justine Thyme.
            portfolios.remove(7);
            portfolios.remove(0);
            traders.add(new IntelligentTrader(port));
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
        } catch(IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    private void initialiseEvents() {
        try {
            BufferedReader br = new BufferedReader(new FileReader("ExternalEventsData.csv"));
            String line;
            while((line = br.readLine()) != null) {
                String[] row = line.split(",");
                if(row.length == SIZE_EVENTS && !row[0].equals("Date")) { // i.e. not the header.
                    events.add(new Event(new SimpleDateFormat("MMM dd yyyy HH:mm").parse(row[0] + " " + row[2], new ParsePosition(0)), row[4], row[15]));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }

    }

    //TODO: TEST THIS METHOD.
    private void run15Mins() {
        HashMap<Trader, ArrayList<Share>> toBeSold = new HashMap<>();
        HashMap<Trader, HashMap<String, Integer>> toBeBought = new HashMap<>(); // Inner HashMap maps Company Name to number of shares sought for purchase.
        HashMap<String, Integer> buyTotals = new HashMap<>();
        HashMap<String, Integer> sellTotals = new HashMap<>();
        for(String companyName : numberOfShares.keySet()) {
            buyTotals.put(companyName, 0);
            sellTotals.put(companyName, 0);
        }
        // Get what everyone wants to buy & sell.
        for(Trader t : traders) {
            HashMap<String, Integer> traderBuys = t.buy();
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
        // Work out how many shares are being sold/bought.
        for(String companyName : numberOfShares.keySet()) {
            ArrayList<Share> sharesForSale = new ArrayList<>();
            ArrayList<Share> sharesBought;
            int buyTotal = buyTotals.get(companyName);
            int sellTotal = sellTotals.get(companyName);
            if(buyTotal < sellTotal) { // Supply > Demand.
                for(Trader t : traders) {
                    ArrayList<Share> sharesOfCompany = new ArrayList<>();
                    for(Share s : toBeSold.get(t)) {
                        if(s.getCompanyName().equals(companyName)) {
                            sharesOfCompany.add(s);
                        }
                    }
                    int sharesSold = buyTotal * (sharesOfCompany.size() / sellTotal);
                    sharesForSale.addAll(sharesOfCompany.subList(0, sharesSold));
                    t.returnShares(new ArrayList<>(sharesOfCompany.subList(sharesSold, sharesOfCompany.size())));
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
        calculateShareIndex();
        calendar.add(calendar.MINUTE, 15);
    }

    private void calculateShareIndex() {
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
    }

    // excess will be negative when Supply > Demand.
    private void changeSharePrice(String companyName, int excess) {
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
        if(newSharePrice <= 0) { // Company is worthless. They must be removed from the simulation.
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
    private void leaveSimulation(String clientName) {
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
        return dayCal.getTime();
    }

    private void removeAllShares(String companyName) {
        for(Trader t : traders) {
            for(Portfolio p : t.getPortfolios()) {
                p.removeAllShares(companyName);
            }
        }
    }

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

    public Event getEvent(){ return eventInProgress; }
    public int getShareIndex() {
        return shareIndex;
    }
    public String getMarketType() {
        return marketType;
    }

}
