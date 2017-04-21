package StockMarket;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Simulator {
    private GregorianCalendar calendar;
    HashMap<Trader, ArrayList<Share>> toBeSold;
    HashMap<Trader, HashMap<String, Integer>> toBeBought; // Inner HashMap maps Company Name to number of shares sought for purchase.
    ArrayList<Trader> traders;
    ArrayList<Event> events;
    ArrayList<Portfolio> portfolios;
    int stockIndex; // in pence.
    String marketType; // Bull, Bear, Stable.
    private static final int SIZE_DATA = 19;
    private static final int SIZE_EVENTS = 16;

    // In place of runSimulation.
    public Simulator(int duration) {
        calendar = new GregorianCalendar(2017, 0, 1);
        traders = new ArrayList<>();
        events = new ArrayList<>();
        portfolios = new ArrayList<>();
        marketType = "Stable";
        initialiseData();
        initialiseEvents();
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
                    for(int i = 4; i < SIZE_DATA - 1; i++) {
                        if(row[i].length() != 0) {
                            ArrayList<Share> shares = new ArrayList<>();
                            for(int k = 0; k < Integer.parseInt(row[i]); k++) {
                                shares.add(new Share(row[0], row[2], Integer.parseInt(row[3])));
                            }
                            portfolios.get(j).addShares(shares);
                            j++;
                        }
                    }
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
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void run15Mins() {
        // TODO: runs 1 15 minute period, i.e. traders trade, clock is updated, etc.
    }

    // excess will be negative when Supply > Demand.
    private void changeSharePrice(String companyName, int excess) {
        // TODO: increase if Demand > Supply, and vice versa.
    }

    private Event checkEvent() {
        // TODO: return event if event is NOW, else null.
        return null;
    }

    private void removeAllShares(String companyName) {
        // TODO: removes shares from all portfolios with name matching companyName.
    }

    private int getSharePrice(int companyName) {
        // TODO: returns share price of companyName.
        return -1;
    }

}
