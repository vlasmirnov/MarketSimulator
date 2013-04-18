
public class Bid {

	public Agent agent;
	public BidType type;
	public Commodity commodity;
	public int quantity;
	public double price;
	public double spendingcap;
	public double marginalscalefactor;
	
	public Bid(Agent agent, BidType type, Commodity c, int q, double p)
	{
		this.agent = agent;
		this.type = type;
		commodity = c;
		quantity = q;
		price = p;
	}

}
