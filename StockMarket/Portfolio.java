package StockMarket;

import java.util.ArrayList;

/**
 * The Portfolio class represents both a Portfolio of a client and the client themselves (as all that is needed for
 * a client is their name). The Portfolio class keeps track of the amount of cash a particular client is holding at any
 * given time and the shares that the client owns.
 *
 * @Author 146803
 * @Version 05/05/2017
 */
public class Portfolio {
    private String clientName;
    private double cashHolding;
    private ArrayList<Share> shares;
    private boolean sellAll;

    /**
     * Creates an instantiation of the Portfolio class with the client name = clientName (all other information is added
     * after the creation of the class).
     * @param clientName The String representation of the client's name.
     */
    public Portfolio(String clientName) {
        this.clientName = clientName;
        shares = new ArrayList<>();
        sellAll = false;
    }

    /**
     * Returns the client's name as a String (other fields are initialised later - this is down to how the Simulator reads
     * CSV files).
     * @return A string representation of the name of the client for which this portfolio represents.
     */
    public String getClientName() {
        return clientName;
    }

    /**
     * Adds the Share objects in shares to this portfolio's collection of shares. Used during the initialisation of the
     * Simulator (as does not subtract the share prices from the amount of cash held by the client).
     * @param shares The List of Share objects to be added to the Portfolio's List of Shares.
     */
    public void addSharesInit(ArrayList<Share> shares) {
        //FOR SETUP
        this.shares.addAll(shares);
    }

    /**
     * Adds the List of shares to the shares List in the Portfolio, and deducts the price of each share from the
     * Portfolio's cashHolding (as this equates to buying each of these shares).
     * @param shares The List of Share objects to be added to the Portfolio.
     */
    public void addShares(ArrayList<Share> shares) {
        //System.out.println(shares.size() + " shares added to " + clientName + ".");
        this.shares.addAll(shares);
        for(Share s : shares) {
            //System.out.println(s.getCompanyName() + " has a share price of " + s.getSharePrice());
            addCashHolding(-s.getSharePrice());
        }
        //System.out.println(clientName + "'s cash now = " + cashHolding);
        if(cashHolding < 0) {
            // Should not occur. Was mostly used in testing, but better safe than sorry.
            System.err.println("Houston, We have a problem: " + clientName + ": " + cashHolding);
            System.exit(-1);
        }
    }

    /**
     * Returns the list of shares this client owns.
     * @return The ArrayList of Share objects kept in this Portfolio.
     */
    public ArrayList<Share> getShares() {
        return shares;
    }

    /**
     * Calculates the total worth of this client.
     * @return the amount of cash this Portfolio holds + the share price of each share owned by this Portfolio.
     */
    public double getTotalWorth() {
        return cashHolding + getSharesTotal();
    }

    /**
     * Used in setup to initially set the cash holding field.
     * @param cashHolding The integer representing the amount of money (in pounds) held by the client (as defined in the
     *                    InitialDataV2.csv file).
     */
    public void setCashHolding(int cashHolding) {
        // MUST ONLY BE USED DURING SETUP, AS MULTIPLIES PARAMETER BY 100.
        cashHolding = cashHolding * 100; // cashHolding is in pounds, we wish to store it in pence.
        this.cashHolding = cashHolding;
    }

    /**
     * Returns the amount of cash the Portfolio has for the buying of shares.
     * @return A double representing the cash (in pence) that the portfolio holds.
     */
    public double getCashHolding() {
        return cashHolding;
    }

    /**
     * Sets the sell all field to true, as the client wishes to leave the simulator.
     */
    public void setSellAll() {
        this.sellAll = true;
    }

    /**
     * Called when a company' share price reaches zero, removes all shares of this company from the list of Share objects
     * (as the company is worthless).
     */
    public void removeAllShares(String companyName) {
        ArrayList<Share> sharesToRemove = new ArrayList<>();
        for(int i = 0; i < shares.size(); i++) {
            if(shares.get(i).getCompanyName().equals(companyName)) {
                sharesToRemove.add(shares.get(i));
                cashHolding -= shares.get(i).getSharePrice();
            }
        }
        shares.removeAll(sharesToRemove);
    }

    /**
     * Returns the ArrayList of share's toString method.
     * @return The toString() method of the ArrayList of Share objects.
     */
    @Override
    public String toString(){
        return shares.toString();
    }

    /**
     * Adds the price of this share to the amount of cashHolding (negative if the sharePrice is to be deducted).
     * @param sharePrice The price of the share that is being deducted.
     */
    public void addCashHolding(double sharePrice) {
        cashHolding += sharePrice;
    }

    /**
     * Returns the total amount of money (in pence) that the client's shares are worth.
     * @return The number of shares the client owns (for each company) * each share price (per company).
     */
    public double getSharesTotal() {
        double total = 0;
        for (Share s : shares) {
            total += s.getSharePrice();
        }
        return total;
    }

}