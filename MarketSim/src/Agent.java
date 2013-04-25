import java.util.HashMap;

public class Agent{
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

	public Agent(Market market, RoundCycleTradingPattern roundCycleTradingPattern, String name, Commodity commodityProduced, Integer productionRate, Integer consumptionRate, Double budget)
	{
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

        for (Commodity commodity : market.commodities){
            inventory.put(commodity, 0);
            shortages.put(commodity, 0);
            previousinventory.put(commodity, 0);
            previousroundpricebid.put(commodity, 0.0);
        }

        this.roundCycleTradingPattern = roundCycleTradingPattern;
        this.roundCycleTradingPattern.agent = this;
	}
	public void update()
	{
		roundCycleTradingPattern.produce();
		roundCycleTradingPattern.consume();
		roundCycleTradingPattern.placeBids();
		previousinventory = new HashMap<Commodity, Integer>();
        for (Commodity commodity : market.commodities){
            previousinventory.put(commodity, inventory.get(commodity));
        }
	}

	
}
