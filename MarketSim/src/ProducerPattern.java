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
			if(agent.inventory.get(commodity) < 0)
			{
				totalneeds = totalneeds - agent.inventory.get(commodity);
			}
		}


        for (Commodity commodity : agent.inventory.keySet())
		{
			double p = 0;
			Random r = new Random();
			int inventorychange = agent.inventory.get(commodity) - agent.previousinventory.get(commodity);
			double previousprice = agent.previousroundpricebid.get(commodity);
			if(agent.inventory.get(commodity) > 0)
			{
				int surplus = agent.inventory.get(commodity);
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
			if(agent.inventory.get(commodity) < 0)
			{
				int needed = 0 - agent.inventory.get(commodity);
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
