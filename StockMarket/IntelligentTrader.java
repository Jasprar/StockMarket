package StockMarket;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

public class IntelligentTrader extends Trader {
    private ArrayList<ClientTracker> tracker = new ArrayList<>();
    public IntelligentTrader(ArrayList<Portfolio> portfolios) {
        super(portfolios);
    }

    // I KNOW! IT LOOKS LIKE SHIT. I'LL CLEAN UP WHEN I GET IT TO WORK!

/*    @Override
    public HashMap<String, Integer> buy(ArrayList<String> availableCompanies) {
        HashMap<String, Integer> sharesBuying = new HashMap<>();
        ArrayList clientPortfolios = this.getPortfolios();
        if(!clientPortfolios.isEmpty()) {
            for (int i = 0; i <= clientPortfolios.size(); i++) {
                for (int j = 0; j <= this.getPortfolios().get(i).getShares().size(); j++) {
                    /*
                    if(!availableCompanies.contains(this.getPortfolios().get(i).getShares().get(j).getCompanyName())) {
                        tracker.add(new ClientTracker(this.getPortfolios().get(i).getClientName(), this.getPortfolios().get(i).getShares().get(j).getCompanyName(), 1));
                    }
                    else {
                        int k = 0;
                        while(true) {
                            if(tracker.get(k).getCompanyName().equals(this.getPortfolios().get(i).getShares().get(j).getCompanyName())) {
                                tracker.get(k).setAmount(tracker.get(k).getAmount() + 1);
                                break;
                            }
                            else {
                                k++;
                            }
                        }
                    }
                    if(!availableCompanies.contains(this.getPortfolios().get(i).getShares().get(j).getCompanyName())) {
                        if(this.getPortfolios().get(i).getShares().get(j).getCompanyName()
                        tracker.add(new ClientTracker(this.getPortfolios().get(i).getClientName(), this.getPortfolios().get(i).getShares().get(j).getCompanyName(), 1, ));
                    }
                }

            }
        }
        return sharesBuying;
    }
*/
    // USED RANDOM TRADER buy() FOR COMPILING FOR NOW.
    @Override
    public HashMap<String, Integer> buy(ArrayList<String> availableCompanies) {

        HashMap<String, Integer> sharesBuying = new HashMap<>();

        for(int i=0; i <= 1; i++) {
            int randomCompany = new Random().nextInt(availableCompanies.size());
            String randomlyChosenCompany = availableCompanies.get(randomCompany);
            if(sharesBuying.containsKey(randomlyChosenCompany)) {
                sharesBuying.put(randomlyChosenCompany,sharesBuying.get(randomlyChosenCompany) + 1);
            }
            else {
                sharesBuying.put(randomlyChosenCompany,1);
            }
        }
        return sharesBuying;
    }

    @Override
    public ArrayList<Share> sell() {
        ArrayList<Share> sharesSelling = new ArrayList<>();
        if(!this.getPortfolios().isEmpty()) {
            for(int i = 0; i <= this.getPortfolios().size(); i++) {
                for(int k = 0; k <= tracker.size(); k++) {
                    if(tracker.get(k).getClientName().equals(this.getPortfolios().get(i).getClientName())) {
                        for(int j = 0; j <= this.getPortfolios().get(i).getShares().size(); j++) {
                            String portCompName = this.getPortfolios().get(i).getShares().get(j).getCompanyName();
                            Share share = getPortfolios().get(i).getShares().get(j);
                            if(portCompName.equals(tracker.get(k).getCompanyName()) && tracker.get(k).getBuyPrice() < share.getSharePrice()) {
                                this.getPortfolios().get(i).setCashHolding(this.getPortfolios().get(i).getCashHolding() + share.getSharePrice());
                                sharesSelling.add(new Share(share.getCompanyName(), share.getCommodity(), share.getSharePrice()));
                            }
                        }
                    }
                }
            }
        }
        return sharesSelling;
    }
    @Override
    public void addNewShares(ArrayList<Share> sharesBought) {
        // TODO: Add these new shares into the portfolios (fairly sure you can just evenly divide these up) and decrement total worths.

//        this.getPortfolios().get(0).getShares().addAll(sharesBought);


        /*for(int i = 0; i <= sharesBought.size(); i++){
            for(int j = 0; j <= getPortfolios().size(); j++) {
                if(this.getPortfolios().size())
                this.getPortfolios().get(0).setCashHolding(this.getPortfolios().get(0).getCashHolding() - sharesBought.get(i).getSharePrice());
            }

        }*/
        HashMap<String,ArrayList<Share>> inputShare = new HashMap<>();
        inputShare.put(sharesBought.get(0).getCompanyName(), new ArrayList<Share>(sharesBought));
        Iterator iterateShare = inputShare.entrySet().iterator();
        while(iterateShare.hasNext()) {
            for(int i = 0; i <= sharesBought.size(); i++) {
                if(inputShare.containsKey(sharesBought.get(i).getCompanyName())) {
                    inputShare.get(sharesBought.get(i).getCompanyName()).add(sharesBought.get(i));
                }
                else {
                    inputShare.put(sharesBought.get(i).getCompanyName(), new ArrayList<Share>());
                    inputShare.get(sharesBought.get(i).getCompanyName()).add(sharesBought.get(i));
                }
            }
        }
    }
}