import java.util.Random;


public class ProducerPattern extends TradingPattern{

	private double marginalScaleFactor = 1.2;
	private double sellerScaleFactor = 1.1;
	private double marketpriceweight = 0.25;

	
	public ProducerPattern() {

	}

	public void placeBids()
	{
		int totalneeds = 0;
		for (Commodity commodity : agent.inventory.keySet())
		{
			if(agent.inventory.get(commodity) < 10)
			{
				totalneeds = totalneeds + 10 - agent.inventory.get(commodity);
			}
		}
		Commodity production = agent.commodityProduced;
		int productioninventorychange = agent.inventory.get(production) -agent.shortages.get(production) - agent.previousinventory.get(production);
		if (productioninventorychange > 0 && agent.previousinventory.get(production) > 20 && agent.toProduce > 0)
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
			if(agent.inventory.get(commodity) > 10)
			{
				int surplus = agent.inventory.get(commodity) - 10;
				if(commodity.marketprice == -1)
				{
					p = r.nextDouble() * 100;
				}
				else
				{
					p = marketpriceweight * commodity.marketprice + (1-marketpriceweight) * previousprice * Math.pow(marginalScaleFactor, 0.1 - inventorychange);
				}
				if (p>0)
				{
				Bid b = new Bid(agent, BidType.SELL, commodity, surplus, p);
				agent.previousroundpricebid.put(commodity, p);
				System.out.println(b.agent.name + " wants to sell " + surplus + " units of " + commodity.name + " for " + p +  " galactic intracredits each.");
				agent.market.acceptBid(b);
				}
			}
			if(agent.inventory.get(commodity) < 10)
			{
				int needed = 10 - agent.inventory.get(commodity);
				if(commodity.marketprice == -1)
				{
					p = r.nextDouble() * 100;
				}
				else
				{
					p = marketpriceweight * commodity.marketprice + (1-marketpriceweight) * previousprice * Math.pow(marginalScaleFactor, -0.1 - inventorychange);
				}
				double spendcap = agent.budget * needed / totalneeds;
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
					System.out.println(b.agent.name + " wants to buy " + needed + " units of " + commodity.name + ", and is willing to spend " + p + " galactic intracredits.");
					agent.market.acceptBid(b);
				}
			}
		}
	}
}
