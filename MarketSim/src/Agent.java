import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

public class Agent{
    private TradingPattern tradingPattern;
	public Market market;
	public String name;
	public Commodity commodityProduced;
	public int productionRate;
    public int consumptionRate;
    public HashMap<Commodity, Integer> inventory;
    public HashMap<Commodity, Integer> previousinventory;
    public HashMap<Commodity, Double> previousroundpricebid;
    public HashMap<Commodity, Integer> shortages;
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
		previousinventory = new HashMap<Commodity, Integer>();
		shortages = new HashMap<Commodity, Integer>();
		previousroundpricebid = new HashMap<Commodity, Double>();

        for (Commodity commodity : market.commodities){
            inventory.put(commodity, 0);
            shortages.put(commodity, 0);
            previousinventory.put(commodity, 0);
            previousroundpricebid.put(commodity, 0.0);
        }

        this.tradingPattern = tradingPattern;
        this.tradingPattern.agent = this;
	}
	public void update()
	{
		tradingPattern.produce();
		tradingPattern.consume();
		tradingPattern.placeBids();
		previousinventory = new HashMap<Commodity, Integer>();
        for (Commodity commodity : market.commodities){
            previousinventory.put(commodity, inventory.get(commodity));
        }
	}

	
}
