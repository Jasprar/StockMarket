package StockMarket;

import java.util.ArrayList;

public class Portfolio {
    private String clientName;
    private double cashHolding;
    private ArrayList<Share> shares;
    private boolean sellAll;

    public Portfolio(String clientName) {
        this.clientName = clientName;
        shares = new ArrayList<>();
        sellAll = false;
    }

    public String getClientName() {
        return clientName;
    }

    //ALSO FOR SETUP - HENCE += TOTALWORTH.
    public void addSharesInit(ArrayList<Share> shares) {
        this.shares.addAll(shares);
        for(Share share : shares) {
        }
    }

    public void addShares(ArrayList<Share> shares) {
        //System.out.println(shares.size() + " shares added to " + clientName + ".");
        this.shares.addAll(shares);
        for(Share s : shares) {
            //System.out.println(s.getCompanyName() + " has a share price of " + s.getSharePrice());
            addCashHolding(-s.getSharePrice());
        }
        //System.out.println(clientName + "'s cash now = " + cashHolding);
        if(cashHolding < 0) {
            System.err.println("Houston, We have a problem: " + clientName + ": " + cashHolding);
            System.exit(-1);
        }


    }

    public ArrayList<Share> getShares() {
        return shares;
    }

    public double getTotalWorth() {
        double totalWorth = cashHolding;
        for(Share s : shares) {
            totalWorth += s.getSharePrice();
        }
        return totalWorth;
    }

    // MUST ONLY BE USED DURING SETUP, AS MULTIPLIES PARAMETER BY 100 & ADDS TO TOTALWORTH.
    public void setCashHolding(int cashHolding) {
        cashHolding = cashHolding * 100; // cashHolding is in pounds, we wish to store it in pence.
        this.cashHolding = cashHolding;
    }

    public double getCashHolding() {
        return cashHolding;
    }

    public void setSellAll() {
        this.sellAll = true;
    }

    // Called when a company's share price reaches 0.
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

    @Override
    public String toString(){
        return shares.toString();
    }

    public void addCashHolding(double sharePrice) {
        cashHolding += sharePrice;
    }

    public double getSharesTotal() {
        double total = 0;
        for (Share s : shares) {
            total += s.getSharePrice();
        }
        return total;
    }



}