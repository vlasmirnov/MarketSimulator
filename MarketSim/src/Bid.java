/**
 * 
 * This class is the base class for all the bids to be created from.
 * 
 */
public class Bid {

	public Agent agent;
	public BidType type;
	public Commodity commodity;
	public int quantity;
	public final int initialQuantity;
	public double price;
	public double spendingcap;
	public double marginalscalefactor = 1;

	/**
	 * This constructor initiates the properties of bids.
	 * 
	 * @param agent
	 *            : the agent making the bid.
	 * @param type
	 *            : the type of bid.
	 * @param commodity
	 *            : the commodity the agent is bidding on.
	 * @param quantity
	 *            : the amount of commodity being bidded.
	 * @param price
	 *            : the amount/value of credits being bidded for commodity.
	 */
	public Bid(Agent agent, BidType type, Commodity commodity, int quantity,
			double price) {
		this.agent = agent;
		this.type = type;
		this.commodity = commodity;
		this.quantity = quantity;
		initialQuantity = quantity;
		this.price = price;
		this.spendingcap = quantity * price;
	}

	public boolean isFilled() {
		return quantity == 0;
	}

	public boolean isPartiallyFilled() {
		return initialQuantity > quantity;
	}
}
