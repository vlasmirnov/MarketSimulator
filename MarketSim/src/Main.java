import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Main {

	public static void main(String[] args) throws IOException {
		
        Market market = new Market();
        
		File resultsMarketFile = new File("resultsMarket.csv");
		File resultsEntitiesFile = new File("resultsEntities.csv");
		try {
			if (!resultsMarketFile.exists()) {
                resultsMarketFile.createNewFile();
            }
			if (!resultsEntitiesFile.exists()) {
                resultsEntitiesFile.createNewFile();
            }
			FileWriter resultsMarketFileWriter = new FileWriter(resultsMarketFile.getAbsoluteFile());
			FileWriter resultsEntitiesFileWriter = new FileWriter(resultsEntitiesFile.getAbsoluteFile());
			market.marketBufferedWriter = new BufferedWriter(resultsMarketFileWriter);
			market.commoditiesBufferedWriter = new BufferedWriter(resultsEntitiesFileWriter);
			market.marketBufferedWriter.write("marketpriceA,marketpriceB,marketpriceC,marketpriceD");
			market.commoditiesBufferedWriter.write("consumptionA,consumptionB,consumptionC,consumptionD,budgetvariance");
			market.marketBufferedWriter.newLine();
			market.commoditiesBufferedWriter.newLine();
			market.commoditiesBufferedWriter.flush();
			market.marketBufferedWriter.flush();

		} catch (IOException e) {
			e.printStackTrace();
		}
        
        Commodity c1 = new Commodity("Ware A", 1);
        Commodity c2 = new Commodity("Ware B", 1);
        Commodity c3 = new Commodity("Ware C", 1);
        Commodity c4 = new Commodity("Ware D", 1);
        market.commodities = new Commodity[]{c1,c2,c3, c4};

        market.agents = new Agent[100];

        /*
        Create 25 copies of four different agents
         */
        for (int a = 0; a < market.agents.length / 4; a++)
        {
        	market.agents[a] = new Agent(market, new ProducerPattern(), "Clone " + a, c1, 3, 2, 1000d);
        	market.agents[a + 25] = new Agent(market, new ProducerPattern(), "Clone " + (a + 25), c2, 4, 2, 1000d);
        	market.agents[a + 50] = new Agent(market, new ProducerPattern(), "Clone " + (a + 50), c3, 5, 2, 1000d);
        	market.agents[a + 75] = new Agent(market, new ProducerPattern(), "Clone " + (a + 75), c4, 6, 2, 1000d);
        }


        List<RoundData> roundDataList = new ArrayList<RoundData>();
		for(int a = 0; a < 100; a++)
		{
		    System.out.println("Trading cycle " + a);
		    System.out.println("____________________");
		    market.update();
            RoundData roundData = new RoundData(market);
            roundDataList.add(roundData);
		}
		printRoundData(roundDataList);
        System.out.println("Finished");
	}

    public static void printRoundData(List<RoundData> roundDataList) throws IOException {

    }

}
