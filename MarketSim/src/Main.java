import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class Main {

	public static void main(String[] args) {
		
        Market market = new Market();
        
		File file = new File("resultsMarket.csv");
		File file2 = new File("resultsEntities.csv");
		try {
			if (!file.exists())
				file.createNewFile();
			if (!file2.exists())
				file2.createNewFile();			
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			FileWriter fw2 = new FileWriter(file2.getAbsoluteFile());
			market.bwMarket = new BufferedWriter(fw);
			market.bwCommodities = new BufferedWriter(fw2);
			market.bwMarket.write("marketpriceA,marketpriceB,marketpriceC,marketpriceD");
			market.bwCommodities.write("consumptionA,consumptionB,consumptionC,consumptionD,budgetvariance");
			market.bwMarket.newLine();
			market.bwCommodities.newLine();
			market.bwCommodities.flush();
			market.bwMarket.flush();

		} catch (IOException e) {
			e.printStackTrace();
		}
        
        Commodity c1 = new Commodity("Ware A", 1);
        Commodity c2 = new Commodity("Ware B", 1);
        Commodity c3 = new Commodity("Ware C", 1);
        Commodity c4 = new Commodity("Ware D", 1);
        market.commodities = new Commodity[]{c1,c2,c3, c4};

        market.agents = new Agent[100];

        for (int a = 0; a < market.agents.length / 4; a++)
        {
        	market.agents[a] = new Agent(market, new ProducerPattern(), "Clone " + a, c1, 3, 2, 1000d);
        	market.agents[a + 25] = new Agent(market, new ProducerPattern(), "Clone " + (a + 25), c2, 4, 2, 1000d);
        	market.agents[a + 50] = new Agent(market, new ProducerPattern(), "Clone " + (a + 50), c3, 5, 2, 1000d);
        	market.agents[a + 75] = new Agent(market, new ProducerPattern(), "Clone " + (a + 75), c4, 6, 2, 1000d);

        }




		for(int a = 0; a < 2000; a++)
		{
		    System.out.println("Trading cycle " + a);
		    System.out.println("____________________");
		    market.update();
		}
		
	}

}
