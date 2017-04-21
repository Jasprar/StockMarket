package StockMarket;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Simulator {
    private GregorianCalendar calendar;
    HashMap<Trader, ArrayList<Share>> toBeSold;
    HashMap<Trader, HashMap<String, Integer>> toBeBought; // Inner HashMap maps Company Name to number of shares sought for purchase.
    ArrayList<Trader> traders;
    ArrayList<Event> events;
    ArrayList<Porfolio> porfolios;
    int stockIndex; // in pence.
    String marketType; // Bull, Bear, Stable.
    private static final int SIZE_DATA = 19;

    // In place of initialiseData and runSimulation.
    public Simulator(int duration) {
        calendar = new GregorianCalendar(2017, 0, 1);
        traders = new ArrayList<>();
        events = new ArrayList<>();
        porfolios = new ArrayList<>();
        marketType = "Stable";

        BufferedReader br = null;
        String line = "";
        try {
            br = new BufferedReader(new FileReader("InitialDataV2.csv"));
            int count = 0;
            while ((line = br.readLine()) != null) {
                String[] row = line.split(",");
                if(row.length == SIZE_DATA && row[0].length() == 0) { // Contains the names of the Clients.
                    for(int i = 0; i < SIZE_DATA - 1; i++) { // Final column is Total Shares Issued.
                        if(row[i].length() != 0) {

                        }
                    }
                } else if(row.length == SIZE_DATA) { // This is a column of data.

                }
            }
        } catch(IOException e) {
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
