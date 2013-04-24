import java.util.ArrayList;
import java.util.HashMap;

/**
 * User: mark.mcdonald Date: 4/21/13
 */
public class RoundData {
	private HashMap<Commodity, Integer> numberSellBids = new HashMap<Commodity, Integer>();
	private HashMap<Commodity, Integer> numberBuyBids = new HashMap<Commodity, Integer>();
	private HashMap<Commodity, Integer> numberCompletedBidsPerCommodity = new HashMap<Commodity, Integer>();
	private HashMap<Commodity, Integer> numberPartiallyCompletedBidsPerCommodity = new HashMap<Commodity, Integer>();
	private HashMap<Commodity, Integer> numberUncompletedBidsPerCommodity = new HashMap<Commodity, Integer>();

	private HashMap<Commodity, Double> marketPrices = new HashMap<Commodity, Double>();

	private Commodity[] commodities;

	/**
	 * 
	 * @param market
	 */
	public RoundData(Market market) {
		commodities = market.commodities;
		for (Commodity commodity : market.commodities) {
			ArrayList<Bid> sellBids = market.getSellBids().get(commodity);
			ArrayList<Bid> buyBids = market.getBuyBids().get(commodity);

			int numberCompletedBids = 0;
			int numberPartiallyCompletedBids = 0;
			int numberUncompletedBids = 0;
			for (Bid bid : sellBids) {
				if (bid.isFilled()) {
					numberCompletedBids++;
				} else if (bid.isPartiallyFilled()) {
					numberPartiallyCompletedBids++;
				} else {
					numberUncompletedBids++;
				}
			}
			numberCompletedBidsPerCommodity.put(commodity, numberCompletedBids);
			numberPartiallyCompletedBidsPerCommodity.put(commodity,
					numberPartiallyCompletedBids);
			numberUncompletedBidsPerCommodity.put(commodity,
					numberUncompletedBids);

			numberSellBids.put(commodity, sellBids.size());
			numberBuyBids.put(commodity, buyBids.size());
			marketPrices.put(commodity, commodity.marketprice);
		}
	}

	/**
	 * 
	 * @return
	 */
	public HashMap<Commodity, Integer> getNumberSellBids() {
		return numberSellBids;
	}

	/**
	 * 
	 * @return
	 */
	public HashMap<Commodity, Integer> getNumberBuyBids() {
		return numberBuyBids;
	}

	/**
	 * 
	 * @return
	 */
	public HashMap<Commodity, Double> getMarketPrices() {
		return marketPrices;
	}

	/**
	 * 
	 * @return
	 */
	public HashMap<Commodity, Integer> getNumberCompletedBidsPerCommodity() {
		return numberCompletedBidsPerCommodity;
	}

	/**
	 * 
	 * @return
	 */
	public HashMap<Commodity, Integer> getNumberPartiallyCompletedBidsPerCommodity() {
		return numberPartiallyCompletedBidsPerCommodity;
	}

	/**
	 * 
	 * @return
	 */
	public HashMap<Commodity, Integer> getNumberUncompletedBidsPerCommodity() {
		return numberUncompletedBidsPerCommodity;
	}

	/**
	 * 
	 * @return
	 */
	public Commodity[] getCommodities() {
		return commodities;
	}
}
