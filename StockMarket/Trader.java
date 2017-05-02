package StockMarket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public abstract class Trader {
    private ArrayList<Portfolio> portfolios;
    private HashMap<Portfolio, ArrayList<Share>> sharesRemoved; // Used for returning shares to their respective portfolios if they don't sell.
    protected String event; // Contains the name of the company or commodity (or "Any") that the RandomTrader has to buy/sell during an event.
    protected ArrayList<ClientTracker> clientTrackers;

    public Trader(ArrayList<Portfolio> portfolios) {
        this.portfolios = portfolios;
        for(Portfolio p : portfolios) {
            for(Share s : p.getShares()) {
                for(ClientTracker ct : clientTrackers) {
                    if(ct.getCompanyName().equals(s.getCompanyName())) {
                        ct.addAmount(1);
                        break;
                    } else { // No such ClientTracker exists, create a new one.
                        clientTrackers.add(new ClientTracker(p.getClientName(), s.getCompanyName(), s.getSharePrice()));
                        break;
                    }
                }
            }
        }
    }

    public ArrayList<Portfolio> getPortfolios() {
        return portfolios;
    }

    public void setMode(int mode) {
        // Does nothing for IntelligentTrader (but required for iteration through Trader list), overridden in RandomTrader.
    }

    public void switchMode() {
        // Does nothing for IntelligentTrader (but required for iteration through Trader list), overridden in RandomTrader
    }

    // HashMap is company name : # sought for purchase.
    public abstract HashMap<String,Integer> buy(ArrayList<String> availableCompanies);

    // ArrayList is the Shares the trader wishes to sell - remember to remove them from the portfolios & increment totalWorth!
    public abstract ArrayList<Share> sell();

    public void returnShares(ArrayList<Share> shares, String companyName) {
        // TODO: Add these shares back to their respective portfolios (you know where they came from due to sharesRemoved) and decrement totalWorths.

    }

    public void addNewShares(ArrayList<Share> sharesBought) {
        // TODO: Add these new shares into the portfolios (fairly sure you can just evenly divide these up) and decrement total worths.
    }

    public void setEvent(String event) {
        this.event = event;
    }
}
