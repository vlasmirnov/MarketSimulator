
public class Main {

	public static void main(String[] args) {
        Market market = new Market();

        Commodity c1 = new Commodity("Self-Aware Furniture", 0);
        Commodity c2 = new Commodity("Fluvobericated Hypertronium", 1);
        Commodity c3 = new Commodity("Child-Safe Neutron Bomb", 2);

        market.commodities = new Commodity[]{c1,c2,c3};

        Agent testGuy1 = new Agent(market, new ProducerPattern(), "Oliver", c1, 3, 2, 500d);
        Agent testGuy2 = new Agent(market, new ProducerPattern(), "Edward", c1, 3, 2, 500d);
        Agent testGuy3 = new Agent(market, new ProducerPattern(), "Stephen", c2, 4, 2, 500d);
        Agent testGuy4 = new Agent(market, new ProducerPattern(), "Richard", c2, 5, 2, 500d);
        Agent testGuy5 = new Agent(market, new ProducerPattern(), "Marcus", c3, 4, 2, 500d);
        Agent testGuy6 = new Agent(market, new ProducerPattern(), "Alexander", c3, 3, 2, 500d);

        market.agents = new Agent[]{testGuy1,testGuy2,testGuy3,testGuy4,testGuy5,testGuy6};


		for(int a = 0; a < 1000; a++)
		{
		    System.out.println("Trading cycle " + a);
		    System.out.println("____________________");
		    market.update();
		}
		
	}

}
