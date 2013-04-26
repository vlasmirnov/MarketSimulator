import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;


public class Market {

    public Commodity[] commodities;
    public Agent[] agents;
    private int marketCycle = 0;
    private double budgetSumProducers = Main.PRODUCER_STARTING_BUDGET, budgetSumMinMaxers = Main.MIN_MAX_STARTING_BUDGET, budgetSumSpeculators = Main.SPECULATOR_STARTING_BUDGET;
    public static double[] budgetArrayProducers, budgetArraySpeculators, budgetArrayMinMaxers;
    public BufferedWriter marketBufferedWriter;
    public BufferedWriter commoditiesBufferedWriter;
    private HashMap<Commodity, ArrayList<Bid>> buyBids;
    private HashMap<Commodity, ArrayList<Bid>> sellBids;
    private HashMap<Commodity, ArrayList<Bid>> shuffledSellBids;
    private HashMap<Commodity, ArrayList<Bid>> shuffledBuyBids;
    private List<RoundData> roundDataList = new ArrayList<RoundData>();

    public Market()
    {
    	budgetArrayProducers = new double[Main.NUMBER_OF_ROUNDS];
    	budgetArraySpeculators = new double[Main.NUMBER_OF_ROUNDS];
        budgetArrayMinMaxers = new double[Main.NUMBER_OF_ROUNDS];
    }

    /**
     * Goes through one round.
     * Clears the bids, updates the agents, and completes transactions.
     */
    public void update()
    {
        emptyBidLists();

        requestAgentBids();

        shuffleBids();

        matchAndExecuteTrades();

        printMarketInformation();
        
        budgetArrayProducers[marketCycle]= budgetSumProducers /(Main.NUMBER_OF_PRODUCERS-1);
        budgetArraySpeculators[marketCycle]= budgetSumSpeculators /(Main.NUMBER_OF_SPECULATORS-1);
        budgetArrayMinMaxers[marketCycle] = budgetSumMinMaxers / (Main.NUMBER_OF_MINMAX-1);
        budgetSumProducers = 0;
        budgetSumMinMaxers = 0;
        budgetSumSpeculators = 0;
        RoundData roundData = new RoundData(this);
        roundDataList.add(roundData);
        
        marketCycle++;
    }

    private void emptyBidLists(){
        buyBids = new HashMap<Commodity, ArrayList<Bid>>();
        sellBids = new HashMap<Commodity, ArrayList<Bid>>();
        shuffledBuyBids = new HashMap<Commodity, ArrayList<Bid>>();
        shuffledSellBids = new HashMap<Commodity, ArrayList<Bid>>();
        for (Commodity commodity : commodities){
            buyBids.put(commodity, new ArrayList<Bid>());
            sellBids.put(commodity, new ArrayList<Bid>());
            shuffledBuyBids.put(commodity, new ArrayList<Bid>());
            shuffledSellBids.put(commodity, new ArrayList<Bid>());
        }
    }

    /**
     * Tell the agents to update themselves and submit their bids
     */
    private void requestAgentBids(){
        for (Commodity commodity : commodities){
            commodity.consumption = 0;
        }
        for (Agent agent : agents) {
            agent.update();
        }
    }

    /**
     *  Prints out the current simulation information TODO: turn datapoints into csv / excell file to analyze
     */
    public void printMarketInformation(){
        if (Main.DEBUGGING) {
            System.out.println("Trading cycle: " + marketCycle);
            System.out.println("______________________");
            System.out.println("Current market prices:");
        }
        String marketcsvline = "";
        String entitiescsvline = "";
        for (Commodity commodity : commodities){
            if(Main.DEBUGGING) 
            	System.out.println(commodity.name + ": " + commodity.marketprice);
            marketcsvline = marketcsvline + commodity.marketprice + ",";
            entitiescsvline = entitiescsvline + commodity.consumption + ",";
        }
        double budgetvar = budgetVariance();
        entitiescsvline = entitiescsvline + budgetvar;

        if(Main.DEBUGGING)
        	System.out.println("Current budgets:");
		for (Agent agent : agents) {
			if(Main.DEBUGGING)
				System.out.println("Agent: " + agent.name + " with Budget: " + GraphingData.roundToDecimals(agent.budget,2));
			if (agent.name.contains("Citizen"))
				budgetSumProducers += agent.budget;
			else if (agent.name.contains("culat")) {
                budgetSumSpeculators += agent.budget;
            } else {
                budgetSumMinMaxers += agent.budget;
            }

		}

        try {
            marketBufferedWriter.write(marketcsvline.substring(0, marketcsvline.length() - 1));
            commoditiesBufferedWriter.write(entitiescsvline);
            marketBufferedWriter.newLine();
            marketBufferedWriter.flush();
            commoditiesBufferedWriter.newLine();
            commoditiesBufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public double budgetVariance()
    {
        double avg = 0;
        for(Agent agent: agents)
        {
            avg = avg + agent.budget;
        }
        avg = avg / agents.length;
        double variance = 0;
        for(Agent agent: agents)
        {
            variance = variance + Math.pow(avg - agent.budget, 2);
        }
        variance = variance / agents.length;
        return variance;
    }

    /**
     * Accept a bid submitted by a user
     * @param bid
     */
    public void acceptBid(Bid bid)
    {
        if (bid.quantity > 0) { // only accept bids that have positive quantities
            Commodity bidCommodity = bid.commodity;
            switch(bid.type){
                case BUY:
                    ArrayList<Bid> buyBidList = buyBids.get(bidCommodity);
                    buyBidList.add(bid);
                    buyBids.put(bidCommodity, buyBidList);
                    if(Main.DEBUGGING)
                    	System.out.println(bid.agent.name + " wants to buy " + bid.quantity + " units of " + bid.commodity.name + " for " + GraphingData.roundToDecimals(bid.price,2) +  " Galactic Intracredits each.");
                    break;
                case SELL:
                    ArrayList<Bid> sellBidList = sellBids.get(bidCommodity);
                    sellBidList.add(bid);
                    sellBids.put(bidCommodity, sellBidList);
                    if(Main.DEBUGGING)
                    	System.out.println(bid.agent.name + " wants to sell " + bid.quantity + " units of " + bid.commodity.name + " for " + GraphingData.roundToDecimals(bid.price,2) +  " Galactic Intracredits each.");
                    break;
            }
        }

    }

    /**
     * Shuffles the submitted bids and processes transactions for matches.
     */
    private void shuffleBids()
    {
		/*
		Shuffle sell bids and buy bids
		 */
        for (Commodity commodity : commodities){
            ArrayList<Bid> listOfBuyBidsToShuffle = buyBids.get(commodity);
            Collections.shuffle(listOfBuyBidsToShuffle);
            shuffledBuyBids.put(commodity,listOfBuyBidsToShuffle);

            ArrayList<Bid> listOfSellBidsToShuffle = sellBids.get(commodity);
            Collections.shuffle(listOfSellBidsToShuffle);
            shuffledSellBids.put(commodity, listOfSellBidsToShuffle);
        }

    }

    /**
     * Calculates the current market price. TODO: Configurable
     */
    private void matchAndExecuteTrades(){
        for (Commodity commodity : commodities){
            {
                double newMarketPrice = 0;
                int quantitySold = 0;
                ArrayList<Bid> buyList = shuffledBuyBids.get(commodity);
                ArrayList<Bid> sellList = shuffledSellBids.get(commodity);
                for (Bid buyBid : buyList) {
                    for (Bid sellBid : sellList) {
                        if (sellBid.quantity != 0 && buyBid.price >= sellBid.price) {
                            int maxBuyAmount;
                            if (buyBid.marginalscalefactor != 1){
                                maxBuyAmount = (int) Math.ceil((Math.log(sellBid.price * 1.0 / buyBid.price) / Math.log(1 / buyBid.marginalscalefactor))) + 1;
                            } else {
                                maxBuyAmount = buyBid.quantity;
                            }
                            int maxBuyAmount2 = (int) (buyBid.spendingcap / sellBid.price);
                            int buyAmount = Math.min(Math.min(maxBuyAmount, maxBuyAmount2), sellBid.quantity);
                            if (buyAmount > 0) {
                                if (buyAmount >= sellBid.quantity) {
                                    transaction(buyBid.agent, sellBid.agent, sellBid.commodity, sellBid.quantity, sellBid.quantity * sellBid.price);
                                    buyBid.spendingcap = buyBid.spendingcap - sellBid.quantity * sellBid.price;
                                    buyBid.price = buyBid.price * Math.pow((1 / buyBid.marginalscalefactor), sellBid.quantity);
                                    quantitySold = quantitySold + sellBid.quantity;
                                    newMarketPrice = newMarketPrice + sellBid.quantity * sellBid.price;
                                    buyBid.quantity = buyBid.quantity - sellBid.quantity;
                                    sellBid.quantity = 0;
                                    if (buyBid.quantity <= 0) {
                                        break;
                                    }
                                } else {
                                    transaction(buyBid.agent, sellBid.agent, sellBid.commodity, buyAmount, buyAmount * sellBid.price);
                                    buyBid.spendingcap = buyBid.spendingcap - buyAmount * sellBid.price;
                                    buyBid.price = buyBid.price * Math.pow((1 / buyBid.marginalscalefactor), buyAmount);
                                    quantitySold = quantitySold + buyAmount;
                                    newMarketPrice = newMarketPrice + buyAmount * sellBid.price;
                                    sellBid.quantity = sellBid.quantity - buyAmount;
                                    buyBid.quantity = buyBid.quantity - buyAmount;
                                    if (buyBid.quantity <= 0) {
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
                if (quantitySold > 0)
                {
                    newMarketPrice = newMarketPrice / quantitySold;
                }
                else
                {
                    double highestbuy = 0;
                    double lowestsell = Double.MAX_VALUE;
                    for (Bid buybid : buyList) {
                        if (buybid.price > highestbuy) {
                            highestbuy = buybid.price;
                        }
                    }
                    for (Bid sellBid : sellList) {
                        if (sellBid.price < lowestsell) {
                            lowestsell = sellBid.price;
                        }
                    }
                    if (highestbuy > 0)
                    {
                        if(lowestsell < Double.MAX_VALUE)
                            newMarketPrice = (highestbuy + lowestsell) / 2;
                        else
                            newMarketPrice = highestbuy;
                    }
                    else
                    {
                        if(lowestsell < Double.MAX_VALUE)
                            newMarketPrice = lowestsell;
                        else
                            newMarketPrice = -1;
                    }

                }
                commodity.marketprice = newMarketPrice;
            }
        }
    }

    /**
     * Services a transaction between a buyer and a seller
     * @param buyer
     * @param seller
     * @param commodity
     * @param quantity
     * @param price
     */
    private void transaction(Agent buyer, Agent seller, Commodity commodity, int quantity, double price)
    {
        buyer.budget = buyer.budget - price;
        seller.budget = seller.budget + price;
        buyer.inventory.put(commodity, buyer.inventory.get(commodity) + quantity);
        seller.inventory.put(commodity, seller.inventory.get(commodity) - quantity);
        //System.out.println(buyer.name + " bought " + quantity + " units of " + commodity.name + " from " + seller.name + " at " + GraphingData.roundToDecimals(price/quantity,2) + " Galactic Intracredits each");
    }


    public HashMap<Commodity, ArrayList<Bid>> getBuyBids() {
        return buyBids;
    }

    public HashMap<Commodity, ArrayList<Bid>> getSellBids() {
        return sellBids;
    }

    public List<RoundData> getRoundDataList() {
        return roundDataList;
    }
}
