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

    public final static Integer GRAPH_WIDTH = 1500, GRAPH_HEIGHT = 1000;

    public final static Integer NUMBER_OF_COMMODITIES = 4;

    public final static Integer COMMODITY_PRICE_FLOOR = 1;

    public static Integer NUMBER_OF_ROUNDS = 2000;

    public static Integer NUMBER_OF_PRODUCERS = 100, NUMBER_OF_SPECULATORS = 0, NUMBER_OF_MINMAX = 0;

    public final static Double PRODUCER_STARTING_BUDGET = 1000d;

    public final static Double SPECULATOR_STARTING_BUDGET = 1000d;

    public final static Double MIN_MAX_STARTING_BUDGET = 1000d;

    public final static Integer PRODUCER_CONSUMPTION_RATE = 2;

    // The maximum amount a producer may create in one round
    private final static Integer PRODUCER_PRODUCTION_STARTING_RATE = 3, PRODUCER_PRODUCTION_INCREASE = 1;

	public static void main(String[] args) throws IOException {
		if (args.length > 3) {
            NUMBER_OF_PRODUCERS = Integer.parseInt(args[0]);
            NUMBER_OF_SPECULATORS = Integer.parseInt(args[1]);
            NUMBER_OF_MINMAX = Integer.parseInt(args[2]);
            NUMBER_OF_ROUNDS = Integer.parseInt(args[3]);
            System.out.println("Simulation started with " + NUMBER_OF_PRODUCERS + " producers, " + NUMBER_OF_SPECULATORS + " trend traders (speculators), and " + NUMBER_OF_MINMAX + " min-max traders.\nIt will run for " + NUMBER_OF_ROUNDS + " rounds.");
        }
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

        /**
         * Set up market commodities based off configurations
         */
        List<Commodity> commodities = new ArrayList<Commodity>();
        for (int a = 0; a < NUMBER_OF_COMMODITIES; a ++) {
            Commodity commodity = new Commodity("Ware " + a, COMMODITY_PRICE_FLOOR);
            commodities.add(commodity);
        }
        market.commodities = new Commodity[commodities.size()];
        commodities.toArray(market.commodities);

        Random rand = new Random();

        ArrayList<Agent> newagents = new ArrayList<Agent>();

        /*
        Create copies of agents - each agent will only produce one kind of commodity
         */

        for (int i = 0; i < NUMBER_OF_COMMODITIES; i++) {
            for(int a = 0; a < (NUMBER_OF_PRODUCERS / NUMBER_OF_COMMODITIES); a++)
            {
                newagents.add(new Agent(market, new ProducerTradingPattern(), "Citizen " + i, commodities.get(i), PRODUCER_PRODUCTION_STARTING_RATE + PRODUCER_PRODUCTION_INCREASE * i, PRODUCER_CONSUMPTION_RATE, PRODUCER_STARTING_BUDGET));
            }
        }
        for (int a = 0; a < NUMBER_OF_MINMAX; a++){
            int days = rand.nextInt(4) + 3;
            newagents.add(new Agent(market, new MinMaxTradingPattern(market,days),"MinMax " + a, null,0,0,MIN_MAX_STARTING_BUDGET));
        }
        for (int a = 0; a < NUMBER_OF_SPECULATORS; a++) {
            int days = rand.nextInt(4) + 3;
            newagents.add(new Agent(market, new TrendTradingPattern(market,days),"Speculator " + a,null,0,0,SPECULATOR_STARTING_BUDGET));
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
        String xLabel = "Number of Rounds";
		String yLabel = "Galactic Intracredits";
		String gTitle = " ";

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
            marketPriceFrame.setSize(GRAPH_WIDTH, GRAPH_HEIGHT);
            marketPriceFrame.setLocation(20, 20);
            marketPriceFrame.setVisible(true);
        }

    	JFrame avgProdBudgetFrame = new JFrame();
        avgProdBudgetFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        avgProdBudgetFrame.setTitle("Average Budgets of Producers");
        gTitle = ("Average Budgets of Producers per Round");
        avgProdBudgetFrame.add(new GraphingData(Market.budgetArrayProducers, xLabel, yLabel, gTitle));
        avgProdBudgetFrame.setSize(GRAPH_WIDTH, GRAPH_HEIGHT);
        avgProdBudgetFrame.setLocation(20, 20);
        avgProdBudgetFrame.setVisible(true);
        
        JFrame avgSpecBudgetFrame = new JFrame();
        avgSpecBudgetFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        avgSpecBudgetFrame.setTitle("Average Budgets of Speculators");
        gTitle = ("Average Budgets of Speculators per Round");
        avgSpecBudgetFrame.add(new GraphingData(Market.budgetArraySpeculators, xLabel, yLabel, gTitle));
        avgSpecBudgetFrame.setSize(GRAPH_WIDTH, GRAPH_HEIGHT);
        avgSpecBudgetFrame.setLocation(20, 20);
        avgSpecBudgetFrame.setVisible(true);

        JFrame avgMinMaxBudgetFrame = new JFrame();
        avgMinMaxBudgetFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        avgMinMaxBudgetFrame.setTitle("Average Budgets of Min Max Traders");
        gTitle = ("Average budgets of Min Max Traders per Round");
        avgMinMaxBudgetFrame.add(new GraphingData(Market.budgetArrayMinMaxers, xLabel, yLabel, gTitle));
        avgMinMaxBudgetFrame.setSize(GRAPH_WIDTH, GRAPH_HEIGHT);
        avgMinMaxBudgetFrame.setLocation(20,20);
        avgMinMaxBudgetFrame.setVisible(true);

    }

}
