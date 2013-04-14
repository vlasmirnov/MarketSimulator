import java.util.ArrayList;
import java.util.Random;


public class Market {

	private int numcommodities;
	public final Commodity[] commodities;
	public final Agent[] testagents;
	private ArrayList<ArrayList<Bid>> sellbids = new ArrayList<ArrayList<Bid>>();
	private ArrayList<ArrayList<Bid>> buybids = new ArrayList<ArrayList<Bid>>();
	private Bid[][] shuffledsellbids;
	private Bid[][] shuffledbuybids;
	
	public Market()
	{
		numcommodities = 3;
		Commodity c1 = new Commodity("Self-Aware Furniture", 0); 
		Commodity c2 = new Commodity("Fluvobericated Hypertronium", 1); 
		Commodity c3 = new Commodity("Child-Safe Neutron Bomb", 2); 
		
		commodities = new Commodity[]{c1, c2, c3};
		
		Agent testguy1 = new Agent(this, "Oliver", c1, 2);
		Agent testguy2 = new Agent(this, "Edward", c1, 2);
		Agent testguy3 = new Agent(this, "Stephen", c2, 3);
		Agent testguy4 = new Agent(this, "Richard", c2, 3);
		Agent testguy5 = new Agent(this, "Marcus", c3, 5);
		Agent testguy6 = new Agent(this, "Alexander", c3, 8);
		
		testagents = new Agent[]{testguy1, testguy2, testguy3, testguy4, testguy5, testguy6};
		shuffledsellbids = new Bid[numcommodities][];
		shuffledbuybids = new Bid[numcommodities][];
		for(int a = 0; a < numcommodities; a++)
		{
			sellbids.add(new ArrayList<Bid>());
			buybids.add(new ArrayList<Bid>());
		}
	}
	
	public void update()
	{
		for(int a = 0; a < numcommodities; a++)
		{
			sellbids.get(a).clear();
			buybids.get(a).clear();
		}
		for(int a = 0; a < testagents.length; a++)
		{
			testagents[a].update();
		}
		clearBids();
		System.out.println("Trading cycle complete");
		System.out.println("______________________");
		System.out.println("Current market prices:");
		for(int a = 0; a < numcommodities; a++)
		{
			System.out.println(commodities[a].name + ": " + commodities[a].marketprice);
		}
		System.out.println("______________________");


	}
	
	private Bid[] shuffleBids(ArrayList<Bid> bids)
	{
		if(bids.size() == 0)
			return new Bid[0];
		
		Bid[] shuffledbids = new Bid[bids.size()];
		Random rand = new Random();
		shuffledbids[0] = bids.get(0);
		for (int a = 1; a < bids.size(); a ++)
		{
			int b = rand.nextInt(a + 1);
			shuffledbids[a] = shuffledbids[b];
			shuffledbids[b] = bids.get(a);
		}
		
		return shuffledbids;
	}
	
	public void submitBid(Bid b)
	{
		if (b.type.equals("buy"))
		{
			buybids.get(b.commodity.commoditynumber).add(b);
		}
		if (b.type.equals("sell"))
		{
			sellbids.get(b.commodity.commoditynumber).add(b);
		}
	}
	
	private void clearBids()
	{
		for(int a = 0; a < numcommodities; a++)
		{
			shuffledbuybids[a] = shuffleBids(buybids.get(a));
		}
		for(int a = 0; a < numcommodities; a++)
		{
			shuffledsellbids[a] = shuffleBids(sellbids.get(a));
		}
		
		
		for(int a = 0; a < numcommodities; a++)
		{
			int newmarketprice = 0;
			int quantitysold = 0;
			int highestbuy = 0;
			Bid[] buylist = shuffledbuybids[a];
			Bid[] selllist = shuffledsellbids[a];
			for(int b = 0; b < buylist.length; b++)
			{
				Bid buybid = buylist[b];
				if (buybid.price > highestbuy)
				{
					highestbuy = buybid.price;
				}
				for(int s = 0; s < selllist.length; s++)
				{
					Bid sellbid = selllist[s];
					if(sellbid.quantity != 0 && buybid.price >= sellbid.price)
					{
						if(buybid.quantity >= sellbid.quantity)
							{
							transaction(buybid.agent, sellbid.agent, sellbid.commodity, sellbid.quantity, sellbid.quantity * sellbid.price);
							buybid.quantity = buybid.quantity - sellbid.quantity;
							sellbid.quantity = 0;
							quantitysold = quantitysold + sellbid.quantity;
							newmarketprice = newmarketprice + sellbid.quantity * sellbid.price;
							if(buybid.quantity == 0)
							{
								break;
							}
						}
						else
						{
							transaction(buybid.agent, sellbid.agent, sellbid.commodity, buybid.quantity, buybid.quantity * sellbid.price);
							sellbid.quantity = sellbid.quantity - buybid.quantity;
							buybid.quantity = 0;
							quantitysold = quantitysold + buybid.quantity;
							newmarketprice = newmarketprice + buybid.quantity * sellbid.price;
							break;
						}
					}
				}
			}
			if (quantitysold > 0)
			{
				newmarketprice = newmarketprice / quantitysold;
			}
			else
			{
				newmarketprice = highestbuy;
			}
			commodities[a].marketprice = newmarketprice;
		}
		
		
	}
	
	private void transaction(Agent buyer, Agent seller, Commodity c, int quantity, int money)
	{
		buyer.inventory[c.commoditynumber] = buyer.inventory[c.commoditynumber] + quantity;
		seller.inventory[c.commoditynumber] = seller.inventory[c.commoditynumber] - quantity;
		System.out.println(buyer.name + " bought " + quantity + " units of " + c.name + " from " + seller.name + " at " + money/quantity + " galactic intracredits each");

	}
	
	
}
