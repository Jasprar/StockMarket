package StockMarket;

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
    private ArrayList<Portfolio> portfolios;
    private HashMap<String, Integer> numberOfShares;
    private int shareIndex; // in pence.
    private String marketType; // Bull, Bear, Stable.
    private static final int SIZE_DATA = 19;
    private static final int SIZE_EVENTS = 16;
    private static final Date END_DATE = new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2018", new ParsePosition(0)); // 1st Jan 2018 at midnight.

    public Simulator() {
        calendar = new GregorianCalendar(2017, 0, 1);
        numberOfShares = new HashMap<>();
        traders = new ArrayList<>();
        events = new ArrayList<>();
        portfolios = new ArrayList<>();
        marketType = "Stable";
        initialiseData();
        calculateShareIndex();
        initialiseEvents();
    }

    public void runSimulation(int duration) {
        while(calendar.getTime().before(END_DATE)) {
            Date endOfDay = getEndOfDay();
            while(calendar.getTime().before(endOfDay)) {
                Event e;
                if((e = checkEvent()) != null)  {
                    for(Trader t : traders) {
                        if(e.isBuy()) {
                            t.setMode(RandomTrader.BUYER);
                        } else {
                            t.setMode(RandomTrader.SELLER);
                        }
                    }
                }
                run15Mins();
            }
            // Handles making the simulation run for a duration (in minutes).
            try {
                Thread.sleep((duration * 1000) / 365);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Shouldn't occur.
            }
        }
    }

    private void initialiseData() {
        try {
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
        for(Trader t : traders) {
            for(Portfolio p : t.getPortfolios()) {
                for(Share s : p.getShares()) {
                    if(s.getCompanyName().equals(companyName)) {
                        s.setSharePrice((excess / numberOfShares.get(companyName)) * s.getSharePrice());
                    }
                }
            }
        }
    }

    private Event checkEvent() {
        for(Event e : events) {
            if(e.getStartDateTime() == calendar.getTime()) {
                return e;
            }
        }
        return null;
    }

    /* Does not actually remove all shares for a client, but sets the boolean sellAll in their portfolio to true, alerting
     * their trader that they must attempt to sell all those shares every cycle. */
    private void removeAllShares(String clientName) {
        for(Trader t : traders) {
            for(Portfolio p : t.getPortfolios()) {
                if(p.getClientName().equals(clientName)) {
                    p.setSellAll();
                    return;
                }
            }
        }
    }

    private int getSharePrice(String companyName) {
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

    public Date getEndOfDay() {
        GregorianCalendar dayCal = new GregorianCalendar();
        dayCal.setTime(calendar.getTime());
        dayCal.add(dayCal.HOUR_OF_DAY, 7);
        return dayCal.getTime();
    }
}
