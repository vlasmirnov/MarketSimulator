import java.util.HashMap;

public class Agent{
    private TradingPattern tradingPattern;
	public Market market;
	public String name;
	public Commodity commodityProduced;
	public int productionRate;
    public int consumptionRate;
    public HashMap<Commodity, Integer> inventory;
	public double budget;

	public Agent(Market market, TradingPattern tradingPattern, String name, Commodity commodityProduced, Integer productionRate, Integer consumptionRate, Double budget)
	{
		this.market = market;
		this.name = name;
        this.productionRate = productionRate;
        this.consumptionRate = consumptionRate;
        this.commodityProduced = commodityProduced;
        this.budget = budget;

		inventory = new HashMap<Commodity, Integer>();

        for (Commodity commodity : market.commodities){
            inventory.put(commodity, 0);
        }

        this.tradingPattern = tradingPattern;
        this.tradingPattern.agent = this;
	}
	public void update()
	{
		tradingPattern.consume();
		tradingPattern.produce();
		tradingPattern.placeBids();
	}

	
}
