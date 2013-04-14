
public class Bid {

	public Agent agent;
	public String type;
	public Commodity commodity;
	public int quantity;
	public int price;
	
	public Bid(Agent a, String t, Commodity c, int q, int p)
	{
		agent = a;
		type = t;
		commodity = c;
		quantity = q;
		price = p;
	}

}
