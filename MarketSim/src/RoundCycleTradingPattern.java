/**
 * 
 * This class is the parent class for all trading pattern classes with default
 * methods that apply to all children.
 * 
 */
abstract public class RoundCycleTradingPattern {

	protected Agent agent;
	private int amountNeededToConsumeButCouldNot = 0;

	public RoundCycleTradingPattern() {
	}

	/**
	 * This method applies the logic of consumption by an agent using the
	 * properties specified in the class of agent & commodity.
	 */
	public void consume() {
		if (agent.consumptionRate > 0) {

			for (Commodity commodity : agent.inventory.keySet()) {
				int amountCommodityCurrentlyOwned = agent.inventory
						.get(commodity);
				int amountRemaining = amountCommodityCurrentlyOwned
						- agent.consumptionRate;
				if (amountRemaining < 0) {
					amountNeededToConsumeButCouldNot = 0 - amountRemaining;
					agent.inventory.put(commodity, 0);
					commodity.totalconsumption = commodity.totalconsumption
							+ agent.consumptionRate + amountRemaining;
					commodity.consumption = commodity.consumption
							+ agent.consumptionRate + amountRemaining;
				} else {
					agent.inventory.put(commodity, amountRemaining);
					amountNeededToConsumeButCouldNot = 0;
					commodity.totalconsumption = commodity.totalconsumption
							+ agent.consumptionRate;
					commodity.consumption = commodity.consumption
							+ agent.consumptionRate;
				}
				agent.shortages
						.put(commodity, amountNeededToConsumeButCouldNot);
			}
		}
	}

	/**
	 * This method adds the produced commodity of an agent into their inventory
	 * of commodities.
	 */
	public void produce() {
		if (agent.toProduce > 0) {
			int amountCommodityCurrentlyOwned = agent.inventory
					.get(agent.commodityProduced);
			agent.inventory.put(agent.commodityProduced,
					amountCommodityCurrentlyOwned + agent.toProduce);
		}
	}

	/**
	 * Applies the relevant logic to placing bids and then places those bids.
	 */
	abstract public void placeBids();

	/**
	 * Getter method for a private field.
	 * 
	 * @return : amoundNeededToConsumeButCouldNot, an int value.
	 */
	public int getAmountNeededToConsumeButCouldNot() {
		return amountNeededToConsumeButCouldNot;
	}
}
