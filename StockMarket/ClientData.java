package StockMarket;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by shahs on 27/04/2017.
 */
public class ClientData {

    private final StringProperty PFClient;
    private final IntegerProperty PFWealth;
    private final IntegerProperty PFCashHolding;
    private final IntegerProperty Shares;


    public ClientData(String pfClient, int pfWealth, int pfCashHolding, int shares) {
        this.PFClient = new SimpleStringProperty(pfClient);
        this.PFWealth = new SimpleIntegerProperty(pfWealth);
        this.PFCashHolding = new SimpleIntegerProperty(pfCashHolding);
        this.Shares = new SimpleIntegerProperty(shares);
    }

    public String getPFClient() {
        return PFClient.get();
    }

    public StringProperty PFClientProperty() {
        return PFClient;
    }

    public void setPFClient(String PFClient) {
        this.PFClient.set(PFClient);
    }

    public int getPFWealth() {
        return PFWealth.get();
    }

    public IntegerProperty PFWealthProperty() {
        return PFWealth;
    }

    public void setPFWealth(int PFWealth) {
        this.PFWealth.set(PFWealth);
    }

    public int getPFCashHolding() {
        return PFCashHolding.get();
    }

    public IntegerProperty PFCashHoldingProperty() {
        return PFCashHolding;
    }

    public void setPFCashHolding(int PFCashHolding) {
        this.PFCashHolding.set(PFCashHolding);
    }

    public int getShares() {
        return Shares.get();
    }

    public IntegerProperty sharesProperty() {
        return Shares;
    }

    public void setShares(int shares) {
        this.Shares.set(shares);
    }
}
