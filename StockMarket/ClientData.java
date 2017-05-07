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
    private final StringProperty managedBy;

    public ClientData(String client, String wealth, String cashHolding, String shares, String managedBy) {
        this.client = new SimpleStringProperty(client);
        this.wealth = new SimpleStringProperty(wealth);
        this.cashHolding = new SimpleStringProperty(cashHolding);
        this.shares = new SimpleStringProperty(shares);
        this.managedBy = new SimpleStringProperty(managedBy);
    }

    public String getClient() {
        return client.get();
    }

    public StringProperty clientProperty() {
        return client;
    }

    public void setClient(String PFClient) {
        this.client.set(PFClient);
    }

    public String getWealth() {
        return wealth.get();
    }

    public StringProperty PFWealthProperty() {
        return wealth;
    }

    public void setWealth(String wealth) {
        this.wealth.set(wealth);
    }

    public String getCashHolding() {
        return cashHolding.get();
    }

    public StringProperty PFCashHoldingProperty() {
        return cashHolding;
    }

    public void setCashHolding(String PFCashHolding) {
        this.cashHolding.set(PFCashHolding);
    }

    public String getShares() {
        return shares.get();
    }

    public StringProperty sharesProperty() {
        return shares;
    }

    public void setShares(String shares) {
        this.shares.set(shares);
    }
    public String getManagedBy() {
        return managedBy.get();
    }

    public StringProperty managedByProperty() {
        return managedBy;
    }

    public void setManagedBy(String managedBy) {this.managedBy.set(managedBy);}
}