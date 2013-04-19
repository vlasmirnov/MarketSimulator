
abstract public class TradingPattern {

    protected Agent agent;
    private int amountNeededToConsumeButCouldNot = 0;

	public TradingPattern()
	{
		
	}

	public void consume()
	{
        for (Commodity commodity : agent.inventory.keySet()){
            int amountCommodityCurrentlyOwned = agent.inventory.get(commodity);
            int amountRemaining = amountCommodityCurrentlyOwned - agent.consumptionRate;
            agent.inventory.put(commodity, amountRemaining);
 
        }
	}
	
	public void produce()
	{
        int amountCommodityCurrentlyOwned = agent.inventory.get(agent.commodityProduced);
        agent.inventory.put(agent.commodityProduced, amountCommodityCurrentlyOwned + agent.productionRate);
	}
	
	abstract public void placeBids();

}
