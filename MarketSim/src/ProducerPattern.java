import java.util.Random;


public class ProducerPattern extends TradingPattern{

	private double marginalScaleFactor = 1.2;

	
	public ProducerPattern() {

	}

	public void placeBids()
	{
		int totalneeds = 0;
		for (Commodity commodity : agent.inventory.keySet())
		{
			if(agent.inventory.get(commodity) < 5)
			{
				totalneeds = totalneeds + 5 - agent.inventory.get(commodity);
			}
		}


        for (Commodity commodity : agent.inventory.keySet())
		{
			double p = 0;
			Random r = new Random();
			if(agent.inventory.get(commodity) > 5)
			{
				int surplus = agent.inventory.get(commodity) - 5;
				if(commodity.marketprice == -1)
				{
					p = r.nextDouble() * 100;
				}
				else
				{
					if(surplus > 5)
					{
						p = commodity.marketprice;
					}
					else
					{
						p = commodity.marketprice * Math.pow(marginalScaleFactor, 5 - surplus);
					}
				}
				if (p>0)
				{
				Bid b = new Bid(agent, BidType.SELL, commodity, surplus, p);
				System.out.println(b.agent.name + " wants to sell " + surplus + " units of " + commodity.name + " for " + p +  " galactic intracredits each.");
				agent.market.acceptBid(b);
				}
			}
			if(agent.inventory.get(commodity) < 5)
			{
				int needed = 5 - agent.inventory.get(commodity);
				if(commodity.marketprice == -1)
				{
					p = r.nextDouble() * 100;
				}
				else
				{
					p = commodity.marketprice * Math.pow(marginalScaleFactor, needed - 1);
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
					System.out.println(b.agent.name + " wants to buy " + needed + " units of " + commodity.name + ", and is willing to spend " + spendcap + " galactic intracredits.");
					agent.market.acceptBid(b);
				}
			}
		}
	}
}
