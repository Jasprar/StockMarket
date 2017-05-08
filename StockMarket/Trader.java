package StockMarket;

import java.util.*;

/**
 * The Trader superclass is used to combine all RandomTraders and IntelligentTraders into one neat package, so they can
 * be iterated through together & to reduce code redundancy.
 * @Author 164875 & 146803
 * @Version 04/05/2017
 */
public abstract class Trader {
    protected ArrayList<Portfolio> portfolios;
    protected String event; // Contains the name of the company or commodity (or "Any") that the RandomTrader has to buy/sell during an event.
    protected ArrayList<ClientTracker> clientTrackers;

    /**
     * Initializes traders, this also initializes trackers to be used for every
     * pair of client and shares. Each traders has several portfolios (clients) and is
     * kept track with an ArrayList. allShares is used as an ArrayList to store all
     * shares available to be purchased.
     * @param portfolios The portfolios that this trader manages.
     * @param allShares All the shares in the simulation, used to initialise the ClientTrackers.
     */
    public Trader(ArrayList<Portfolio> portfolios, ArrayList<Share> allShares) {
        this.portfolios = portfolios;
        // Initialise ClientTrackers.
        clientTrackers = new ArrayList<>();
        for(Portfolio p : portfolios) {
            initialiseTrackers(allShares, p.getClientName());
            updateTrackers(p.getShares(), p.getClientName());
        }
    }


    /**
     * This method returns the list of portfolios handled by this trader.
     * @return An ArrayList of portfolios that are managed by this Trader.
     */
    public ArrayList<Portfolio> getPortfolios() {
        return portfolios;
    }

    /**
     * This method is only used by the Random Trader, for more information, see RandomTrader. Required in the superclass
     * to avoid errors when iterating through all traders.
     * @param mode The mode to set the random trader to.
     * @see RandomTrader
     */
    public void setMode(int mode) {
        // Does nothing for IntelligentTrader (but required for iteration through Trader list), overridden in RandomTrader.
    }

    /**
     * This method is only used by the Random Trader, for more information, see RandomTrader. Required in the superclass
     * to avoid errors when iterating through all traders.
     * @see RandomTrader
     */
    public void switchMode() {
        // Does nothing for IntelligentTrader (but required for iteration through Trader list), overridden in RandomTrader
    }

    /**
     * Creates a HashMap of share wanting to buy with the company name and the number of shares
     * the trader wants to buy.
     * @param sharePrices A HashMap of all companies to their share prices. Used to make sure the trader does not
     *                    spend too much.
     * @return A Hashmap of shares wanted to buy with key "company name" and value
     * "number of shares to buy".
     */
    public abstract HashMap<String,Integer> buy(HashMap<String, Double> sharePrices);

    /**
     * Creates an ArrayList of Share which is passed on to simulator to calculate the number
     * of that share wanted to buy.
     * @return An ArrayList of Share that the trader wishes to sell on the Stock Exchange.
     */
    // ArrayList is the Shares the trader wishes to sell - remember to remove them from the portfolios & increment totalWorth!
    public abstract ArrayList<Share> sell(HashMap<String, Double> sharePrices);

    /**
     * Sets the event for the random traders to switch into that mode.
     * Only used by the RandomTrader
     * @param event
     */
    public void setEvent(String event) {
        this.event = event;
    }

    /**
     * This method is called when all shares have been bought/ sold and then returned
     * if it is unable to fulfill the demand/share. It resets the number wanted to buy
     * and sell to 0 to prepare for the next 15 minutes cycle
     */
    public void checkTrackers() {
        ArrayList<ClientTracker> trackers = new ArrayList<>();
        for(int i = 0; i < clientTrackers.size(); i++) {
            ClientTracker ct = clientTrackers.get(i);
            ct.resetAmountSold();
            ct.resetAmountBought();
        }
    }

    /**
     * Updates the tracker when the client wants to buy a number of a share.
     * It increments the number wanted to sell as a way to track which client sold which share
     * since when the shares are returned to the trader, it is impossible to know who sold
     * which share
     * @param shares
     * @param clientName
     */
    private void updateTrackers(ArrayList<Share> shares, String clientName) {
        int j = 1;
        for(Share s : shares) {
            boolean found = false;
            int i = 0;
            while(!found && i < clientTrackers.size()) {
                ClientTracker ct = clientTrackers.get(i);
                if(ct.getCompanyName().equals(s.getCompanyName()) && ct.getClientName().equals(clientName)) {
                    found = true;
                    ct.incrementAmount();
                    ct.setBuyPrice(s.getSharePrice());
                }
                i++;
            }
            if(!found) { // No such ClientTracker exists, create one.
                System.err.println("THIS SHOULD NOT HAVE OCCURRED");
            }
            j++;
        }
    }

    /**
     * This method deleted a tracker when a company is removed from the simulator
     * (bankrupt)
     * @param companyName
     */
    public void removeTrackers(String companyName) {
        ArrayList<ClientTracker> toRemove = new ArrayList<>();
        for(ClientTracker ct : clientTrackers) {
            if(ct.getCompanyName().equals(companyName)) {
                toRemove.add(ct);
            }
        }
        clientTrackers.removeAll(toRemove);
    }

    /**
     * This method starts all trackers at the start of the simulation since clients
     * already have shares in their portfolio (InitialDataV2.csv).
     * @param allShares
     * @param clientName
     */
    private void initialiseTrackers(ArrayList<Share> allShares, String clientName) {
        for(Share s : allShares) {
            boolean done = false;
            for(ClientTracker ct : clientTrackers) {
                if(ct.getCompanyName().equals(s.getCompanyName()) && ct.getClientName().equals(clientName)) {
                    // Already done this share.
                    done = true;
                }
            }
            if(!done) {
                //System.out.println("Creating a new ClientTracker for " + clientName + " tracking " + s.getCompanyName());
                clientTrackers.add(new ClientTracker(clientName, s.getCompanyName(), s.getCommodity()));
            }
        }
    }

    /**
     * This method gets the shares of all portfolios and returns the total number
     * @return Integer of all shares handled by the trader
     */
    public int getShares() {
        int total = 0;
        for(Portfolio p : portfolios) {
            total += p.getShares().size();
        }
        return total;
    }

    /**
     * This method returns shares unable to be sold from demand being lower than the supply
     * It splits the the shares proportionally to how many the client wanted to sell and then
     * adds them back to the portfolio
     * @param shares
     * @param companyName
     * @param sellTotal
     */
    public void returnShares(ArrayList<Share> shares, String companyName, int sellTotal) {
        //System.out.println("Returning Shares...");
        HashMap<Portfolio, Integer> amountForEachPortfolio = new HashMap<>();
        for(Portfolio p : portfolios) {
            for(ClientTracker ct : clientTrackers) {
                if(p.getClientName().equals(ct.getClientName()) && companyName.equals(ct.getCompanyName())) {
                    //System.out.println(p.getClientName() + ": " + shares.size() + " * " + ct.getAmountSold() + " / " + sellTotal + " = " + (int) Math.floor((double) shares.size() * ((double)ct.getAmountSold() / (double)sellTotal)));
                    amountForEachPortfolio.put(p, (int) Math.floor((double) shares.size() * ((double)ct.getAmountSold() / (double)sellTotal)));
                    //System.out.println(p.getClientName() + ": " + amountForEachPortfolio.get(p));
                }
            }
            if(!amountForEachPortfolio.containsKey(p)) {
                amountForEachPortfolio.put(p, 0);
            }
        }
        ArrayList<Share> leftOverShares = addShares(shares, companyName, new HashMap<>(amountForEachPortfolio));
        while(!leftOverShares.isEmpty()) {
            //System.out.println("While leftovershares isnt empty returning...");
            for (Portfolio p : portfolios) {
                int amount = amountForEachPortfolio.get(p);
                for (ClientTracker ct : clientTrackers) {
                    if(shares.isEmpty()) {
                        break;
                    }
                    if (ct.getClientName().equals(p.getClientName()) && ct.getCompanyName().equals(companyName) && (ct.getAmountSold() - amount) > -1 && leftOverShares.get(0).getSharePrice() < p.getCashHolding()) {
                        Share s = leftOverShares.remove(0);
                        ArrayList<Share> temp = new ArrayList<>();
                        temp.add(s);
                        p.addShares(temp);
                        //System.out.println("Total requested to buy of " + ct.getCompanyName() + " by " + ct.getClientName() + " = " + ct.getAmountSold());
                        updateTrackers(temp, p.getClientName());
                        amount--;
                    }
                }
                amountForEachPortfolio.put(p, amount);
            }
        }
    }

    /**
     * This method adds shares wanted to buy from the client into their profolio
     * proportionally to how many of each client wanted to buy that share.
     * @param shares
     * @param companyName
     * @param buyTotal
     */
    public void addNewShares(ArrayList<Share> shares, String companyName, int buyTotal) {
        //System.out.println("Adding shares of " + companyName);
        HashMap<Portfolio, Integer> amountForEachPortfolio = new HashMap<>();
        for(Portfolio p : portfolios) {
            for(ClientTracker ct : clientTrackers) {
                if(p.getClientName().equals(ct.getClientName()) && companyName.equals(ct.getCompanyName())) {
                    //System.out.println(p.getClientName() + ": " + shares.size() + " * " + ct.getAmountBought() + " / " + buyTotal + " = " + (int) Math.floor((double)ct.getAmountBought() * ((double) shares.size() / (double)buyTotal)));
                    amountForEachPortfolio.put(p, (int) Math.floor((double)ct.getAmountBought() * ((double) shares.size() / (double)buyTotal)));
                }
            }
            if(!amountForEachPortfolio.containsKey(p)) {
                amountForEachPortfolio.put(p, 0);
            }
        }
        ArrayList<Share> leftOverShares = addShares(shares, companyName, new HashMap<>(amountForEachPortfolio));
        int i = 0;
        while(!leftOverShares.isEmpty()) {
            //System.out.println("While addNewShares leftOverShares blah....");
            for (Portfolio p : portfolios) {
                int amount = amountForEachPortfolio.get(p);
                for (ClientTracker ct : clientTrackers) {
                    if(shares.isEmpty()) {
                        break;
                    }
                    if (ct.getClientName().equals(p.getClientName()) && ct.getCompanyName().equals(companyName) && (ct.getAmountBought() - amount) > -1 && leftOverShares.get(0).getSharePrice() < p.getCashHolding()) {
                        Share s = leftOverShares.remove(0);
                        ArrayList<Share> temp = new ArrayList<>();
                        temp.add(s);
                        p.addShares(temp);
                        //System.out.println("Total requested to buy of " + ct.getCompanyName() + " by " + ct.getClientName() + " = " + ct.getAmountBought());
                        updateTrackers(temp, p.getClientName());
                        amount--;
                    }
                }
                amountForEachPortfolio.put(p, amount);
            }
            i++;
        }
    }

    /**
     * This private method is only used in this class. It adds the shares to the portfolio
     * with a proportional amount to how many they wanted to buy compared to other clients
     * who wanted to buy also handled by this trader
     * @param shares
     * @param companyName
     * @param amountForEachPortfolio
     * @return
     */
    private ArrayList<Share> addShares(ArrayList<Share> shares, String companyName, HashMap<Portfolio, Integer> amountForEachPortfolio) {
        int i = 0;
        while(!shares.isEmpty() & i < portfolios.size()) {
            //System.out.println("Remaining shares = " + shares.size());
            for (Portfolio p : portfolios) {
                //System.out.println(p.getClientName() + "'s turn");
                int amount = amountForEachPortfolio.get(p);
                //System.out.println(p.getClientName() + " has " + amount + " shares to add...");
                for (ClientTracker ct : clientTrackers) {
                    if(shares.isEmpty()) {
                        break;
                    }
                    if (ct.getClientName().equals(p.getClientName()) && ct.getCompanyName().equals(companyName) && amount > 0 && shares.get(0).getSharePrice() < p.getCashHolding()) {
                        i = 0;
                        Share s = shares.remove(0);
                        ArrayList<Share> temp = new ArrayList<>();
                        temp.add(s);
                        p.addShares(temp);
                        //System.out.println("Total put up for sale by " + ct.getClientName() + " of " + ct.getCompanyName() + " = " + ct.getAmountSold() + ", total requested to be bought = " + ct.getAmountBought());
                        updateTrackers(temp, p.getClientName());
                        amount--;
                    } else {
                        i++;
                    }
                }
                amountForEachPortfolio.put(p, amount);
            }
        }
        return shares;
    }
}
