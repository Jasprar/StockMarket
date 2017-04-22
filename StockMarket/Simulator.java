package StockMarket;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;

public class Simulator {
    private GregorianCalendar calendar;
    private HashMap<Trader, ArrayList<Share>> toBeSold;
    private HashMap<Trader, HashMap<String, Integer>> toBeBought; // Inner HashMap maps Company Name to number of shares sought for purchase.
    private HashMap<String, Integer> numberOfShares;
    private ArrayList<Trader> traders;
    private ArrayList<Event> events;
    private ArrayList<Portfolio> portfolios;
    private int stockIndex; // in pence.
    private String marketType; // Bull, Bear, Stable.
    private static final int SIZE_DATA = 19;
    private static final int SIZE_EVENTS = 16;

    public Simulator() {
        calendar = new GregorianCalendar(2017, 0, 1);
        numberOfShares = new HashMap<>();
        traders = new ArrayList<>();
        events = new ArrayList<>();
        portfolios = new ArrayList<>();
        marketType = "Stable";
        initialiseData();
        initialiseEvents();
    }

    public void runSimulation(int duration) {
        // TODO
    }

    private void initialiseData() {
        try {
            BufferedReader br = new BufferedReader(new FileReader("InitialDataV2.csv"));
            String line = "";
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
            String line = "";
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

    private void run15Mins() {

        for(Trader t : traders) {
            HashMap<String, Integer> traderBuys = t.buy();
            toBeBought.put(t, traderBuys);
            toBeSold.put(t, t.sell());
        }
        calendar.add(calendar.MINUTE, 15);
    }

    // excess will be negative when Supply > Demand, zero when Supply = Demand.
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

    private int getSharePrice(int companyName) {
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

}
