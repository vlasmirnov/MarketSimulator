import java.util.Random;


public class Agent{

	public Market market;
	public String name;
	public Commodity makes;
	public int productionrate;
	public double[] demands; 
	public int[] inventory;
	public double budget = 500;

	
	public Agent(Market mkt, String n, Commodity m, int rate)
	{

		market = mkt;
		name = n;
		makes = m;
		productionrate = rate;
		int commoditynum = market.commodities.length;
		inventory = new int[commoditynum];
		Random r = new Random();
		demands = new double[]{0.4, 0.2, 0.3};
//		double total = 1;
//		demands = new double[commoditynum];
//		for (int a = 0; a < commoditynum; a++)
//		{
//			inventory[a] = 5;
//			demands[a] = r.nextDouble() * total;
//			total = total - demands[a];
//		}
	}
	public void update()
	{
		consume();
		inventory[makes.commoditynumber] = inventory[makes.commoditynumber] + productionrate;
		placeBids();
	}
	private void consume()
	{
		for(int a = 0; a < inventory.length; a++)
		{
			if (inventory[a] > 1)
			{
				inventory[a] = inventory[a] - 2;
			}
		}
	}
	private void placeBids()
	{
		int totalneeds = 0;
		for (int a = 0; a < inventory.length; a++)
		{
			if(inventory[a] < 5)
			{
				totalneeds = totalneeds + 5 - inventory[a];
			}
		}
		
		for(int a = 0; a < inventory.length; a++)
		{
			double p = 0;
			Random r = new Random();
			if(inventory[a] > 5)
			{
				int surplus = inventory[a] - 5;
				if(market.commodities[a].marketprice == -1)
				{
					p = r.nextDouble() * 100;
				}
				else
				{
					if(surplus > 5)
					{
						p = market.commodities[a].marketprice * Math.pow(1.2, 5 - 5);
					}
					else
					{
						p = market.commodities[a].marketprice * Math.pow(1.2, 5 - surplus);
					}
				}
				if (p>0)
				{
				Bid b = new Bid(this, "sell", market.commodities[a], surplus, p);
				System.out.println(b.agent.name + " wants to sell " + surplus + " units of " + market.commodities[a].name + " for " + p +  " galactic intracredits each.");
				market.submitBid(b);
				}
			}
			if(inventory[a] < 5)
			{
				int needed = 5 - inventory[a];
				if(market.commodities[a].marketprice == -1)
				{
					p = r.nextDouble() * 100;
				}
				else
				{
					p = market.commodities[a].marketprice * Math.pow(1.2, needed - 1);
				}
				double spendcap = budget * needed / totalneeds;
				if (p > spendcap)
				{
					p = spendcap;
				}
				if (p > 0)
				{
					Bid b = new Bid(this, "buy", market.commodities[a], needed, p);
					b.spendingcap = spendcap;
					System.out.println(b.agent.name + " wants to buy " + needed + " units of " + market.commodities[a].name + ", and is willing to spend " + spendcap + " galactic intracredits.");
					market.submitBid(b);	
				}
			}
		}
	}
}
