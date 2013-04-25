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

    public final static Integer NUMBER_OF_PRODUCERS = 100;

    public final static Integer NUMBER_OF_SPECULATORS = 20;

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
        for(int a = 0; a < (NUMBER_OF_PRODUCERS / 4); a++)
        {
        	newagents.add(new Agent(market, new ProducerTradingPattern(), "Citizen " + 4*a, c1, 3, 2, 1000d));
        	newagents.add(new Agent(market, new ProducerTradingPattern(), "Citizen " + (4*a+1), c2, 4, 2, 1000d));
        	newagents.add(new Agent(market, new ProducerTradingPattern(), "Citizen " + (4*a+2), c3, 5, 2, 1000d));
        	newagents.add(new Agent(market, new ProducerTradingPattern(), "Citizen " + (4*a+3), c4, 6, 2, 1000d));
        }
//        for(int a = 0; a < NUMBER_OF_SPECULATORS; a++)
//        {
//        	int days = rand.nextInt(4) + 3;
//        	newagents.add(new Agent(market, new TrendTradingPattern(market,days), "Speculator " + a, null, 0, 0, 1000d));
//        }
        for (int a = 0; a < 20; a++){
            int days = rand.nextInt(4) + 3;
            newagents.add(new Agent(market, new MinMaxTradingPattern(market,days),"MinMax " + a, null,0,0,1000d));
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
        RoundData firstRoundData = roundDataList.get(0);
        /**
         * Create a list of three speculators and three producers we'll track
         */
        List<Agent> threeSpeculators = new ArrayList<Agent>();
        List<Agent> threeProducers = new ArrayList<Agent>();
        for (Agent agent : firstRoundData.getAgentBudgets().keySet()){
            if (threeProducers.size() < 3 || threeSpeculators.size() < 3){
                if (agent.name.contains("MinMax") && threeSpeculators.size() < 3) {
                    threeSpeculators.add(agent);
                } else if (!agent.name.contains("MinMax") && threeProducers.size() < 3) {
                    threeProducers.add(agent);
                }
            }
        }
        
        String xLabel = "Number of Rounds";
		String yLabel = "Galactic Intracredits";
		String gTitle = " ";

            for (Agent agent : threeSpeculators){
                double[] agentBudget = new double[NUMBER_OF_ROUNDS];
                int i = 0;
                for (RoundData roundData : roundDataList) {
                    agentBudget[i] = roundData.getAgentBudgets().get(agent);
                    i++;
                }
                JFrame speculatorBudgetFrame = new JFrame();
                speculatorBudgetFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                speculatorBudgetFrame.setTitle("Budget for " + agent.name);
                gTitle = ("Three Speculator's Summed Budgets");
                speculatorBudgetFrame.add(new GraphingData(agentBudget, xLabel, yLabel, gTitle));
                speculatorBudgetFrame.setSize(1000, 1000);
                speculatorBudgetFrame.setLocation(20, 20);
                speculatorBudgetFrame.setVisible(true);
            }
            for (Agent agent : threeProducers){
                double[] agentBudget = new double[NUMBER_OF_ROUNDS];
                int i = 0;
                for (RoundData roundData : roundDataList) {
                    agentBudget[i] = roundData.getAgentBudgets().get(agent);
                    i++;
                }
                JFrame speculatorBudgetFrame = new JFrame();
                speculatorBudgetFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                speculatorBudgetFrame.setTitle("Budget for " + agent.name);
                gTitle = ("Three Producer's Summed Budgets");
                speculatorBudgetFrame.add(new GraphingData(agentBudget, xLabel, yLabel, gTitle));
                speculatorBudgetFrame.setSize(1000, 1000);
                speculatorBudgetFrame.setLocation(20, 20);
                speculatorBudgetFrame.setVisible(true);
            }

        Commodity[] commodities = roundDataList.get(0).getCommodities();
    	for (Commodity commodity : commodities) {
    		double[] marketPrices = new double[NUMBER_OF_ROUNDS];
    		int i = 0;
    		for (RoundData roundData : roundDataList) {
    			marketPrices[i] = roundData.getMarketPrices().get(commodity);
    			i++;
            }
    		JFrame marketPriceFrame = new JFrame();
            marketPriceFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            marketPriceFrame.setTitle("Market Price for " + commodity.name);
            gTitle = ("Market Value of Commodity: " + commodity.name);
            marketPriceFrame.add(new GraphingData(marketPrices, xLabel, yLabel, gTitle));
            marketPriceFrame.setSize(1000, 1000);
            marketPriceFrame.setLocation(20, 20);
            marketPriceFrame.setVisible(true);
        }
    }

}
