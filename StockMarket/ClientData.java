package StockMarket;

import javafx.beans.property.*;

/**
 * Created by shahs on 27/04/2017.
 */
public class ClientData {

    private final StringProperty PFClient;
    private final SimpleDoubleProperty PFWealth;
    private final SimpleDoubleProperty PFCashHolding;
    private final StringProperty PFShares;
    private final StringProperty PFManagedBy;


    public ClientData(String pfClient, int pfWealth, int pfCashHolding, String PFShares, String managedBy) {
        this.PFClient = new SimpleStringProperty(pfClient);
        this.PFWealth = new SimpleDoubleProperty(pfWealth);
        this.PFCashHolding = new SimpleDoubleProperty(pfCashHolding);
        this.PFShares = new SimpleStringProperty(PFShares);
        this.PFManagedBy = new SimpleStringProperty(managedBy);
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

    public double getPFWealth() {
        return PFWealth.get();
    }

    public SimpleDoubleProperty PFWealthProperty() {
        return PFWealth;
    }

    public void setPFWealth(Double PFWealth) {
        this.PFWealth.set(PFWealth);
    }

    public double getPFCashHolding() {
        return PFCashHolding.get();
    }

    public SimpleDoubleProperty PFCashHoldingProperty() {
        return PFCashHolding;
    }

    public void setPFCashHolding(Double PFCashHolding) {
        this.PFCashHolding.set(PFCashHolding);
    }

    public String getPFShares() {
        return PFShares.get();
    }

    public StringProperty PFSharesProperty() {
        return PFShares;
    }

    public void setPFShares(String PFShares) {
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
