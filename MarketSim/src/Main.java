
public class Main {

	public static void main(String[] args) {
        Market market = new Market();
        Commodity c1 = new Commodity("Ware A", 0);
        Commodity c2 = new Commodity("Ware B", 0);
        Commodity c3 = new Commodity("Ware C", 0);
        Commodity c4 = new Commodity("Ware D", 20);
        market.commodities = new Commodity[]{c1,c2,c3, c4};

        market.agents = new Agent[100];

        for (int a = 0; a < market.agents.length / 4; a++)
        {
        	market.agents[a] = new Agent(market, new ProducerPattern(), "Clone " + a, c1, 5, 2, 1000d);
        	market.agents[a + 25] = new Agent(market, new ProducerPattern(), "Clone " + (a + 25), c2, 6, 2, 1000d);
        	market.agents[a + 50] = new Agent(market, new ProducerPattern(), "Clone " + (a + 50), c3, 7, 2, 1000d);
        	market.agents[a + 75] = new Agent(market, new ProducerPattern(), "Clone " + (a + 75), c4, 9, 2, 1000d);

        }




		for(int a = 0; a < 2000; a++)
		{
		    System.out.println("Trading cycle " + a);
		    System.out.println("____________________");
		    market.update();
		}
		
	}

}
