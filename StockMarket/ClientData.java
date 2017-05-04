package StockMarket;

import javafx.beans.property.*;

/**
 * Created by shahs on 27/04/2017.
 */
public class ClientData {

    private final StringProperty client;
    private final SimpleDoubleProperty wealth;
    private final SimpleDoubleProperty cashHolding;
    private final StringProperty shares;
    private final StringProperty managedBy;

    public ClientData(String client, int wealth, int cashHolding, String shares, String managedBy) {
        this.client = new SimpleStringProperty(client);
        this.wealth = new SimpleDoubleProperty(wealth);
        this.cashHolding = new SimpleDoubleProperty(cashHolding);
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

    public double getWealth() {
        return wealth.get();
    }

    public SimpleDoubleProperty PFWealthProperty() {
        return wealth;
    }

    public void setWealth(Double wealth) {
        this.wealth.set(wealth);
    }

    public double getCashHolding() {
        return cashHolding.get();
    }

    public SimpleDoubleProperty PFCashHoldingProperty() {
        return cashHolding;
    }

    public void setCashHolding(Double PFCashHolding) {
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
