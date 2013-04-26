import java.util.HashMap;

/**
 * 
 * This class is the base class for all the agents to be created by the Main
 * class.
 * 
 */
public class Agent {
	private RoundCycleTradingPattern roundCycleTradingPattern;
	public Market market;
	public String name;
	public Commodity commodityProduced;
	public int productionRate;
	public int toProduce;
	public int consumptionRate;
	public HashMap<Commodity, Integer> inventory;
	public HashMap<Commodity, Integer> previousinventory;
	public HashMap<Commodity, Double> previousroundpricebid;
	public HashMap<Commodity, Integer> shortages;
	public double budget;

	/**
	 * This constructor aids in initializing different agents with following
	 * details:
	 * 
	 * @param market
	 *            : the default global Market to be passed in.
	 * @param roundCycleTradingPattern
	 *            : the abstract class that is the parent of all trading
	 *            patterns.
	 * @param name
	 *            : the name/identifier of agents.
	 * @param commodityProduced
	 *            : the type of commodity to be produced on each round by
	 *            agents.
	 * @param productionRate
	 *            : the amount of commodity to be produced on each round by
	 *            agents.
	 * @param consumptionRate
	 *            : the amount of commodity to be consumed on each round by
	 *            agents.
	 * @param budget
	 *            : the amount of credit the agent has for spending/storing
	 *            purposes.
	 */
	public Agent(Market market,
			RoundCycleTradingPattern roundCycleTradingPattern, String name,
			Commodity commodityProduced, Integer productionRate,
			Integer consumptionRate, Double budget) {
		this.market = market;
		this.name = name;
		this.productionRate = productionRate;
		this.toProduce = productionRate;
		this.consumptionRate = consumptionRate;
		this.commodityProduced = commodityProduced;
		this.budget = budget;

		inventory = new HashMap<Commodity, Integer>();
		previousinventory = new HashMap<Commodity, Integer>();
		shortages = new HashMap<Commodity, Integer>();
		previousroundpricebid = new HashMap<Commodity, Double>();

		for (Commodity commodity : market.commodities) {
			inventory.put(commodity, 0);
			shortages.put(commodity, 0);
			previousinventory.put(commodity, 0);
			previousroundpricebid.put(commodity, 0.0);
		}

		this.roundCycleTradingPattern = roundCycleTradingPattern;
		this.roundCycleTradingPattern.agent = this;
	}

	/**
	 * This method updates the agent's parametric values.
	 */
	public void update() {
		roundCycleTradingPattern.produce();
		roundCycleTradingPattern.consume();
		roundCycleTradingPattern.placeBids();
		previousinventory = new HashMap<Commodity, Integer>();
		for (Commodity commodity : market.commodities) {
			previousinventory.put(commodity, inventory.get(commodity));
		}
	}

}
