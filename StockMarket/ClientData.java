package StockMarket;

import javafx.beans.property.*;

/**
 * Used by the GUI and Controller to keep track of one Client (for the table in the 'back end' of the GUI).
 * @Author 132224
 * @Version 04/05/2017
 */
public class ClientData {

    private final StringProperty client;
    private final StringProperty wealth;
    private final StringProperty cashHolding;
    private final StringProperty shares;



    /**
     * Constructor. Initializes the ClientData class.
     * @param client clientName.
     * @param wealth totalWealth.
     * @param cashHolding cashHolding.
     * @param shares number of shares.
     */public ClientData(String client, String wealth, String cashHolding, String shares) {
        this.client = new SimpleStringProperty(client);
        this.wealth = new SimpleStringProperty(wealth);
        this.cashHolding = new SimpleStringProperty(cashHolding);
        this.shares = new SimpleStringProperty(shares);
    }

    /**
     * This method gets the client name.
     * @return String representation of client's name.
     */
    public String getClient() {
        return client.get();
    }

    /**
     * This method sets the client name to be used in the GUI.
     * @param PFClient The string to set the client StringProperty to.
     */
    public void setClient(String PFClient) {
        this.client.set(PFClient);
    }

    /**
     * This method gets the wealth of a client.
     * @return String representing the wealth of the client.
     */
    public String getWealth() {
        return wealth.get();
    }

    /**
     * This method sets the wealth of the client into a variable to be used
     * in the GUI.
     * @param wealth The string representation of the wealth of the client to set the wealth to.
     */
    public void setWealth(String wealth) {
        this.wealth.set(wealth);
    }

    /**
     * This method gets the cash the client is currently holding.
     * @return The string representation of the client's cash holding.
     */
    public String getCashHolding() {
        return cashHolding.get();
    }

    /**
     * This method sets the cash holding to be used in the GUI.
     * @param PFCashHolding The cashHolding (as a String) to set the cashHolding StringProperty to.
     */
    public void setCashHolding(String PFCashHolding) {
        this.cashHolding.set(PFCashHolding);
    }

    /**
     * This method gets the shares of the client.
     * @return String representation of the shares.
     */
    public String getShares() {
        return shares.get();
    }

    /**
     * This method sets the shares of the client to be used in the GUI.
     * @param shares The String representation to set shares to.
     */
    public void setShares(String shares) {
        this.shares.set(shares);
    }


    /**
     * Returns the client StringProperty.
     * @return The StringProperty representing the client name.
     */
    public StringProperty clientProperty() {
        return client;
    }

    /**
     * Returns the wealth StringProperty.
     * @return The StringProperty representing the wealth.
     */
    public StringProperty wealthProperty() {
        return wealth;
    }

    /**
     * Returns the cash holding StringProperty.
     * @return The StringProperty representing the amount of cash the client has.
     */
    public StringProperty cashHoldingProperty() {
        return cashHolding;
    }

    /**
     * Returns the shares StringProperty.
     * @return The StringProperty representing the shares the client has.
     */
    public StringProperty sharesProperty() {
        return shares;
    }

    
}