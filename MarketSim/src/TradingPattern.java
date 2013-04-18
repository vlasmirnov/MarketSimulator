import java.util.Random;


public class TradingPattern {
	public String pattern;
	public Agent agent;
	private double marginalscalefactor = 1.2;
	public TradingPattern()
	{
		
	}
	
	public TradingPattern(Agent a, String pattern)
	{
		agent = a;
		this.pattern = pattern;
			
	}
	
	public void consume() {
		if (this.pattern.equals("consumer")) {
			for (int a = 0; a < agent.inventory.length; a++) {
				if (agent.inventory[a] > 1) {
					agent.inventory[a] = agent.inventory[a] - 4;
				}
			}
		}
	}
	
	public void produce()
	{
		if(this.pattern.equals("consumer"))
			agent.budget = agent.budget + agent.sallary;
	}
	
	public void placeBids()
	{

		int totalneeds = 0;
		for (int a = 0; a < agent.inventory.length; a++)
		{
			if(agent.inventory[a] < 5)
			{
				totalneeds = totalneeds + 5 - agent.inventory[a];
			}
		}
		
		for(int a = 0; a < agent.inventory.length; a++)
		{
			double p = 0;
			Random r = new Random();
			if(agent.inventory[a] > 5)
			{
				int surplus = agent.inventory[a] - 5;
				if(agent.market.commodities[a].marketprice == -1)
				{
					p = r.nextDouble() * 100;
				}
				else
				{
					if(surplus > 5)
					{
						p = agent.market.commodities[a].marketprice;
					}
					else
					{
						p = agent.market.commodities[a].marketprice * Math.pow(marginalscalefactor, 5 - surplus);
					}
				}
				if (p>0)
				{
				Bid b = new Bid(agent, "sell", agent.market.commodities[a], surplus, p);
				System.out.println(b.agent.name + " wants to sell " + surplus + " units of " + agent.market.commodities[a].name + " for " + (int)p +  " galactic intracredits each.");
				agent.market.submitBid(b);
				}
			}
			if(agent.inventory[a] < 5)
			{
				int needed = 5 - agent.inventory[a];
				if(agent.market.commodities[a].marketprice == -1)
				{
					p = r.nextDouble() * 100;
				}
				else
				{
					p = agent.market.commodities[a].marketprice * Math.pow(marginalscalefactor, needed - 1);
				}
				double spendcap = agent.budget * needed / totalneeds;
				if (p > spendcap)
				{
					p = spendcap;
				}
				if (p > 0)
				{
					Bid b = new Bid(agent, "buy", agent.market.commodities[a], needed, p);
					b.spendingcap = spendcap;
					b.marginalscalefactor = marginalscalefactor;
					System.out.println(b.agent.name + " wants to buy " + needed + " units of " + agent.market.commodities[a].name + ", and is willing to spend " + (int)spendcap + " galactic intracredits.");
					agent.market.submitBid(b);	
				}
			}
		}
	}
	}

