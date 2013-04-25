import java.util.ArrayList;
import java.util.HashMap;

/**
 * User: mark.mcdonald
 * Date: 4/21/13
 */
public class RoundData {
    private HashMap<Commodity, Integer> numberSellBids = new HashMap<Commodity, Integer>();
    private HashMap<Commodity, Integer> numberBuyBids = new HashMap<Commodity, Integer>();
    private HashMap<Commodity, Integer> numberCompletedBidsPerCommodity = new HashMap<Commodity, Integer>();
    private HashMap<Commodity, Integer> numberPartiallyCompletedBidsPerCommodity = new HashMap<Commodity, Integer>();
    private HashMap<Commodity, Integer> numberUncompletedBidsPerCommodity = new HashMap<Commodity, Integer>();

    private HashMap<Commodity, Double> marketPrices = new HashMap<Commodity, Double>();

    private HashMap<Agent, Integer> agentBudgets = new HashMap<Agent, Integer>();

    private Commodity[] commodities;

    public RoundData(Market market) {
        for (Agent agent : market.agents){
            agentBudgets.put(agent, (int) agent.budget);
        }

        commodities = market.commodities;
        for (Commodity commodity : market.commodities){
            ArrayList<Bid> sellBids = market.getSellBids().get(commodity);
            ArrayList<Bid> buyBids = market.getBuyBids().get(commodity);

            int numberCompletedBids = 0;
            int numberPartiallyCompletedBids = 0;
            int numberUncompletedBids = 0;
            for (Bid bid : sellBids){
                if (bid.isFilled()){
                    numberCompletedBids++;
                } else if (bid.isPartiallyFilled()) {
                    numberPartiallyCompletedBids++;
                } else {
                    numberUncompletedBids++;
                }
            }
            numberCompletedBidsPerCommodity.put(commodity,numberCompletedBids);
            numberPartiallyCompletedBidsPerCommodity.put(commodity,numberPartiallyCompletedBids);
            numberUncompletedBidsPerCommodity.put(commodity, numberUncompletedBids);

            numberSellBids.put(commodity, sellBids.size());
            numberBuyBids.put(commodity, buyBids.size());
            marketPrices.put(commodity, commodity.marketprice);
        }
    }

    public HashMap<Commodity, Integer> getNumberSellBids() {
        return numberSellBids;
    }

    public HashMap<Commodity, Integer> getNumberBuyBids() {
        return numberBuyBids;
    }

    public HashMap<Commodity, Double> getMarketPrices() {
        return marketPrices;
    }

    public HashMap<Commodity, Integer> getNumberCompletedBidsPerCommodity() {
        return numberCompletedBidsPerCommodity;
    }

    public HashMap<Commodity, Integer> getNumberPartiallyCompletedBidsPerCommodity() {
        return numberPartiallyCompletedBidsPerCommodity;
    }

    public HashMap<Commodity, Integer> getNumberUncompletedBidsPerCommodity() {
        return numberUncompletedBidsPerCommodity;
    }

    public Commodity[] getCommodities() {
        return commodities;
    }

    public HashMap<Agent, Integer> getAgentBudgets() {
        return agentBudgets;
    }
}
