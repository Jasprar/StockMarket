package StockMarket;

import javafx.beans.property.*;

/**
 * Created by shahs on 27/04/2017.
 */
public class ClientData {

    private final StringProperty client;
    private final StringProperty wealth;
    private final StringProperty cashHolding;
    private final StringProperty shares;



    /**
     * Constructor. Initializes the ClientData class.
     * @param client
     * @param wealth
     * @param cashHolding
     * @param shares
     */
    public ClientData(String client, String wealth, String cashHolding, String shares) {
        this.client = new SimpleStringProperty(client);
        this.wealth = new SimpleStringProperty(wealth);
        this.cashHolding = new SimpleStringProperty(cashHolding);
        this.shares = new SimpleStringProperty(shares);
    }

    /**
     * This method gets the client name
     * @return String client
     */
    public String getClient() {
        return client.get();
    }

    /**
     * This method sets the client name to be used in the GUI
     * @param PFClient
     */
    public void setClient(String PFClient) {
        this.client.set(PFClient);
    }

    /**
     * This method gets the wealth of a client
     * @return String wealth
     */
    public String getWealth() {
        return wealth.get();
    }

    /**
     * This method sets the wealth of the client into a variable to be used
     * in the GUI
     * @param wealth
     */
    public void setWealth(String wealth) {
        this.wealth.set(wealth);
    }

    /**
     * This method gets the cash the client is currently holding
     * @return String cashHolding
     */
    public String getCashHolding() {
        return cashHolding.get();
    }

    /**
     * This method sets the cash holding to be used in the GUI
     * @param PFCashHolding
     */
    public void setCashHolding(String PFCashHolding) {
        this.cashHolding.set(PFCashHolding);
    }

    /**
     * This method gets the shares of the client
     * @return String shares
     */
    public String getShares() {
        return shares.get();
    }

    /**
     * This method sets the shares of the client to be used in the GUI
     * @param shares
     */
    public void setShares(String shares) {
        this.shares.set(shares);
    }



    public StringProperty clientProperty() {
        return client;
    }

    public StringProperty wealthProperty() {
        return wealth;
    }

    public StringProperty cashHoldingProperty() {
        return cashHolding;
    }

    public StringProperty sharesProperty() {
        return shares;
    }

    
}