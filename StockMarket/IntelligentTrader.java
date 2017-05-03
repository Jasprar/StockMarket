package StockMarket;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class IntelligentTrader extends Trader {
    private ArrayList<ClientTracker> tracker = new ArrayList<>();
    public IntelligentTrader(ArrayList<Portfolio> portfolios) {
        super(portfolios);
    }

    // I KNOW! IT LOOKS LIKE SHIT. I'LL CLEAN UP WHEN I GET IT TO WORK!

    @Override
    public HashMap<String, Integer> buy(ArrayList<String> availableCompanies) {
        HashMap<String, Integer> sharesBuying = new HashMap<>();
        for (Portfolio p : portfolios) {
            for (Share s : p.getShares()) {
                        for (ClientTracker ct : clientTrackers) {

                            if (ct.getClientName().equals(p.getClientName()) && p.getShares().equals(s) && s.getSharePrice() > ct.getBuyPrice()) {
                                int fluctScale = ct.getFluctuation();
                                int randomNoToBuy = 0;
                                randomNoToBuy = ThreadLocalRandom.current().nextInt(0, (int) Math.round((p.getCashHolding() + 1) * (fluctScale * 0.1)));
                                if (sharesBuying.containsKey(s.getCompanyName())) {
                                    sharesBuying.put(s.getCompanyName(), sharesBuying.get(s.getCompanyName()) + randomNoToBuy);
                                } else {
                                    sharesBuying.put(s.getCompanyName(), randomNoToBuy);
                                }

                            }
                        }
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