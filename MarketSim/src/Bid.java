
public class Bid {

	public Agent agent;
	public String type;
	public Commodity commodity;
	public int quantity;
	public double price;
	public double spendingcap;
	public double marginalscalefactor;
	
	public Bid(Agent a, String t, Commodity c, int q, double p)
	{
		agent = a;
		type = t;
		commodity = c;
		quantity = q;
		price = p;
	}

}
