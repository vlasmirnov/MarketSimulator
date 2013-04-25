
abstract public class RoundCycleTradingPattern {

    protected Agent agent;
    private int amountNeededToConsumeButCouldNot = 0;

    public RoundCycleTradingPattern()
    {

    }

    public void consume()
    {
        if (agent.consumptionRate > 0) {

            for (Commodity commodity : agent.inventory.keySet()){
                int amountCommodityCurrentlyOwned = agent.inventory.get(commodity);
                int amountRemaining = amountCommodityCurrentlyOwned - agent.consumptionRate;
                if (amountRemaining < 0)
                {
                    amountNeededToConsumeButCouldNot = 0 - amountRemaining;
                    agent.inventory.put(commodity, 0);
                    commodity.totalconsumption = commodity.totalconsumption + agent.consumptionRate + amountRemaining;
                    commodity.consumption = commodity.consumption + agent.consumptionRate + amountRemaining;
                }
                else
                {
                    agent.inventory.put(commodity, amountRemaining);
                    amountNeededToConsumeButCouldNot = 0;
                    commodity.totalconsumption = commodity.totalconsumption + agent.consumptionRate;
                    commodity.consumption = commodity.consumption + agent.consumptionRate;
                }
                agent.shortages.put(commodity, amountNeededToConsumeButCouldNot);
            }
        }
    }

    public void produce()
    {
        if (agent.toProduce > 0) {
            int amountCommodityCurrentlyOwned = agent.inventory.get(agent.commodityProduced);
            agent.inventory.put(agent.commodityProduced, amountCommodityCurrentlyOwned + agent.toProduce);
        }
    }

    abstract public void placeBids();

    public int getAmountNeededToConsumeButCouldNot() {
        return amountNeededToConsumeButCouldNot;
    }
}
