
public class Bid {

	public Agent agent;
	public BidType type;
	public Commodity commodity;
	public int quantity;
    public final int initialQuantity;
	public double price;
	public double spendingcap;
	public double marginalscalefactor = 1;
	
	public Bid(Agent agent, BidType type, Commodity commodity, int quantity, double price)
	{
		this.agent = agent;
		this.type = type;
		this.commodity = commodity;
		this.quantity = quantity;
        initialQuantity = quantity;
		this.price = price;
		this.spendingcap = quantity * price;
	}

    public boolean isFilled(){
        return quantity == 0;
    }

    public boolean isPartiallyFilled(){
        return initialQuantity > quantity;
    }
}
