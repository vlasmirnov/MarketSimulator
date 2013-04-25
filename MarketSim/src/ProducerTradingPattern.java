import java.util.Random;


public class ProducerTradingPattern extends RoundCycleTradingPattern {

    private double marginalScaleFactor = 1.2;
    private double sellerScaleFactor = 1.1;
    private double marketPriceWeight = 0.25;

    public ProducerTradingPattern() {
    }

    public ProducerTradingPattern(double marginalScaleFactor, double marketPriceWeight) {
        this.marginalScaleFactor = marginalScaleFactor;
        this.marketPriceWeight = marketPriceWeight;
    }

    public void placeBids()
    {
        double totalneeds = 0;
        for (Commodity commodity : agent.inventory.keySet())
        {
            if(agent.inventory.get(commodity) < 15)
            {
                totalneeds = totalneeds + (15 - agent.inventory.get(commodity));
            }
        }
        Commodity production = agent.commodityProduced;
        int productioninventorychange = agent.inventory.get(production) - agent.shortages.get(production) - agent.previousinventory.get(production);
        if (productioninventorychange > 0 && agent.previousinventory.get(production) > 40 && agent.toProduce > 0)
        {
            agent.toProduce = agent.toProduce - 1;
        }
        if (productioninventorychange <= 0 && agent.toProduce < agent.productionRate)
        {
            agent.toProduce = agent.toProduce + 1;
        }


        for (Commodity commodity : agent.inventory.keySet())
        {
            double p = 0;
            Random r = new Random();
            int inventorychange = agent.inventory.get(commodity) -agent.shortages.get(commodity) - agent.previousinventory.get(commodity);
            double previousprice = agent.previousroundpricebid.get(commodity);
            if(agent.inventory.get(commodity) > 20)
            {
                int surplus = agent.inventory.get(commodity) - 20;
                if(commodity.marketprice == -1)
                {
                    p = r.nextDouble() * 100;
                }
                else
                {
                    p = marketPriceWeight * commodity.marketprice + (1- marketPriceWeight) * previousprice * Math.pow(marginalScaleFactor, 0.1 - inventorychange);
                }
                if (p < commodity.pricefloor)
                {
                    p = commodity.pricefloor;
                }
                if (p>0)
                {
                    Bid b = new Bid(agent, BidType.SELL, commodity, surplus, p);
                    agent.previousroundpricebid.put(commodity, p);
                    agent.market.acceptBid(b);
                }
            }
            if(agent.inventory.get(commodity) < 15)
            {
                int needed = 15 - agent.inventory.get(commodity);
                double neededspending = needed;
                if(commodity.marketprice == -1)
                {
                    p = r.nextDouble() * 100;
                }
                else
                {
                    p = marketPriceWeight * commodity.marketprice + (1- marketPriceWeight) * previousprice * Math.pow(marginalScaleFactor, -0.1 - inventorychange);
                }
                double spendcap = agent.budget * neededspending / totalneeds;
                if (p > spendcap)
                {
                    p = spendcap;
                }
                if (p > 0)
                {
                    Bid b = new Bid(agent, BidType.BUY, commodity, needed, p);
                    b.spendingcap = spendcap;
                    b.marginalscalefactor = marginalScaleFactor;
                    agent.previousroundpricebid.put(commodity, p);
                    agent.market.acceptBid(b);
                }
            }
        }
    }
}
