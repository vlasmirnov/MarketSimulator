import java.util.Random;


public class Agent{

	public Market market;
	public String name;
	public Commodity makes;
	public int productionrate;
	public double[] demands;
	public int[] inventory;

	
	public Agent(Market mkt, String n, Commodity m, int rate)
	{

		market = mkt;
		name = n;
		makes = m;
		productionrate = rate;
		int commoditynum = market.commodities.length;
		demands = new double[commoditynum];
		inventory = new int[commoditynum];
		Random r = new Random();
		for (int a = 0; a < commoditynum; a++)
		{
			inventory[a] = 5;
			demands[a] = (r.nextGaussian() / 5) + 1;
		}
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
			if (inventory[a] > 0)
			{
				inventory[a] = inventory[a] - 1;
			}
		}
	}
	private void placeBids()
	{
		for(int a = 0; a < inventory.length; a++)
		{
			int p = 0;
			Random r = new Random();
			if(inventory[a] > 5)
			{
				int surplus = inventory[a] - 5;
				if(market.commodities[a].marketprice == -1)
				{
					p = r.nextInt(100) + 1;
				}
				else
				{
					p = 1 + (int) (market.commodities[a].marketprice / Math.pow(1.2, surplus - 5));
				}
				
				Bid b = new Bid(this, "sell", market.commodities[a], surplus, p);
				System.out.println(b.agent.name + " wants to sell " + surplus + " units of " + market.commodities[a].name + " for " + p +  " galactic intracredits each.");
				market.submitBid(b);
			}
			if(inventory[a] < 5)
			{
				int needed = 5 - inventory[a];
				if(market.commodities[a].marketprice == -1)
				{
					p = r.nextInt(100) + 1;
				}
				else
				{
					p = 1 + (int) (market.commodities[a].marketprice / Math.pow(1.2, 2 - needed));
				}

					Bid b = new Bid(this, "buy", market.commodities[a], needed, p);
					System.out.println(b.agent.name + " wants to buy " + needed + " units of " + market.commodities[a].name + " for " + p +  " galactic intracredits each.");
					market.submitBid(b);	


			}
		}
	}
}
