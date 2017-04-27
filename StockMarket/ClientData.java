package StockMarket;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.ArrayList;

/**
 * Created by shahs on 27/04/2017.
 */
public class ClientData {

    private final StringProperty PFClient;
    private final IntegerProperty PFWealth;
    private final IntegerProperty PFCashHolding;
    private final IntegerProperty PFShares;
    private final StringProperty PFManagedBy;

    private ArrayList<String> clientNames;

    public ClientData(String ClientName, int Wealth, int CashHolding, int Shares, String ManagedBy) {
        this.PFClient = new SimpleStringProperty(ClientName);
        this.PFWealth = new SimpleIntegerProperty(Wealth);
        this.PFCashHolding = new SimpleIntegerProperty(CashHolding);
        this.PFShares = new SimpleIntegerProperty(Shares);
        this.PFManagedBy = new SimpleStringProperty(ManagedBy);
    }

    public String getPFClient() {
        return PFClient.get();
    }

    public StringProperty PFClientProperty() {
        return PFClient;
    }

    public ArrayList<String> getClientNames() {
        return clientNames;
    }

    public void setClientNames(ArrayList<String> clientNames) {
        this.clientNames = clientNames;
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

    public int getPFShares() {
        return PFShares.get();
    }

    public IntegerProperty PFSharesProperty() {
        return PFShares;
    }

    public void setPFShares(int PFShares) {
        this.PFShares.set(PFShares);
    }
    public String getPFManagedBy() {
        return PFManagedBy.get();
    }

    public StringProperty PFManagedByProperty() {
        return PFManagedBy;
    }

    public void setPFManagedBy(String PFManagedBy) {
        this.PFManagedBy.set(PFManagedBy);
    }
}
