package StockMarket;

import java.util.ArrayList;

public class Portfolio {
    private String clientName;
    private double totalWorth;
    private double cashHolding;
    private ArrayList<Share> shares;
    private boolean sellAll;

    public Portfolio(String clientName) {
        this.clientName = clientName;
        totalWorth = 0; // cashHolding is added later (due to processing the spreadsheet row-by-row).
        shares = new ArrayList<>();
        sellAll = false;
    }

    public String getClientName() {
        return clientName;
    }

    public void addShares(ArrayList<Share> shares) {
        this.shares.addAll(shares);
        for(Share share : shares) {
            totalWorth -= share.getSharePrice();
        }
    }

    public void addOneShare(Share shares) {
        this.shares.add(shares);
        totalWorth -= shares.getSharePrice();
    }

    public ArrayList<Share> getShares() {
        return shares;
    }

    public double getTotalWorth() {
        return totalWorth;
    }

    // MUST ONLY BE USED DURING SETUP, AS MULTIPLIES PARAMETER BY 100.
    public void setCashHolding(int cashHolding) {
        cashHolding = cashHolding * 100; // cashHolding is in pounds, we wish to store it in pence.
        this.cashHolding = cashHolding;
        totalWorth += cashHolding;
    }

    public double getCashHolding() {
        return cashHolding;
    }

    public void setSellAll() {
        this.sellAll = true;
    }

    // Called when a company's share price reaches 0.
    public void removeAllShares(String companyName) {
        for(int i = 0; i < shares.size(); i++) {
            if(shares.get(i).getCompanyName().equals(companyName)) {
                shares.remove(i);
            }
        }
    }
    @Override
    public String toString(){
        return shares.toString();
    }

    public void addCashHolding(double sharePrice) {
        cashHolding += sharePrice;
        totalWorth += sharePrice;
    }
}

