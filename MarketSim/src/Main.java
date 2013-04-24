import javax.swing.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Main {

    public final static Boolean DEBUGGING = false;

    private final static Integer NUMBER_OF_ROUNDS = 2000;

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

        ArrayList<Agent> newagents = new ArrayList<Agent>();
        Random rand = new Random();

        /*
        Create 20 copies of four different agents
         */
        for(int a = 0; a < 25; a++)
        {
        	newagents.add(new Agent(market, new ProducerPattern(), "Citizen " + 4*a, c1, 3, 2, 1000d));
        	newagents.add(new Agent(market, new ProducerPattern(), "Citizen " + (4*a+1), c2, 4, 2, 1000d));
        	newagents.add(new Agent(market, new ProducerPattern(), "Citizen " + (4*a+2), c3, 5, 2, 1000d));
        	newagents.add(new Agent(market, new ProducerPattern(), "Citizen " + (4*a+3), c4, 6, 2, 1000d));
        }
        for(int a = 0; a < 20; a++)
        {
        	int days = rand.nextInt(4) + 3;
        	newagents.add(new Agent(market, new TrendPattern(market,days), "Speculator " + a, c1, 0, 0, 1000d));
        }
        
        market.agents = new Agent[newagents.size()];
        newagents.toArray(market.agents);


		for(int a = 0; a < NUMBER_OF_ROUNDS; a++)
		{
		    if (DEBUGGING) {
                System.out.println("Trading cycle " + a);
                System.out.println("____________________");
            }
		    market.update();
		}
		displayRoundData(market.getRoundDataList());
        if (DEBUGGING) {
            System.out.println("Finished");
        }
	}

	public static void displayRoundData(List<RoundData> roundDataList) throws IOException {
    	Commodity[] commodities = roundDataList.get(0).getCommodities();
    	for (Commodity commodity : commodities) {

    		double[] marketPrices = new double[NUMBER_OF_ROUNDS];
    		int i = 0;
    		for (RoundData roundData : roundDataList) {
    			marketPrices[i] = roundData.getMarketPrices().get(commodity);
    			i++;
            }
    		// Filled in information on commodity prices, let's make a graph based on it now:
    		JFrame f = new JFrame();
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.add(new GraphingData(marketPrices));
            f.setSize(1500,1000);
            f.setLocation(20,20);
            f.setVisible(true);
        }
    	
    }

}
