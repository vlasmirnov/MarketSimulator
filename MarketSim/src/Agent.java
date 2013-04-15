import java.util.Random;


public class Agent{

	private TradingPattern tradingpattern;
	public Market market;
	public String name;
	public Commodity makes;
	public int productionrate;
	public int[] inventory;
	public double budget = 500;

	
	public Agent(Market mkt, String pattern, String n, Commodity m, int rate)
	{

		market = mkt;
		name = n;
		makes = m;
		productionrate = rate;
		inventory = new int[market.commodities.length];
		
		if(pattern.equals("producer"))
		{
			tradingpattern = new ProducerPattern(this);
		}

	}
	public void update()
	{
		tradingpattern.consume();
		tradingpattern.produce();
		tradingpattern.placeBids();
	}

	
}
