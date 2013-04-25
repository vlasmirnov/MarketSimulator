import java.util.List;

/**
 * User: mark.mcdonald
 * Date: 4/23/13
 */
public class MinMaxTradingPattern extends RoundCycleTradingPattern {

    private Market market;
    private int numberRoundsToTrack;


    public MinMaxTradingPattern(Market market, int numberRoundsToTrack) {
        this.market = market;
        this.numberRoundsToTrack = numberRoundsToTrack;
    }

    @Override
    public void placeBids() {
        List<RoundData> marketRoundData = market.getRoundDataList();
        int numberRoundsPassed = marketRoundData.size();
        final double spendingCapPerCommodity = Math.floor(agent.budget / market.commodities.length);
        if (numberRoundsToTrack < numberRoundsPassed) {
            for (Commodity commodity : market.commodities) {
                double minMarketPrice = Double.MAX_VALUE;
                double maxMarketPrice = 0;
                for (int i = numberRoundsToTrack; i > 0; i--) {
                    double marketPriceForIthRound = marketRoundData.get(numberRoundsPassed - i).getMarketPrices().get(commodity);
                    if (marketPriceForIthRound > maxMarketPrice) {
                        maxMarketPrice = marketPriceForIthRound;
                    } else if (marketPriceForIthRound < minMarketPrice) {
                        minMarketPrice = marketPriceForIthRound;
                    }
                }
                // Place a buy bid at the market minimum and sell bid at market maximum
                int quantityToBuy = (int) Math.floor(spendingCapPerCommodity / minMarketPrice);
                Bid buyBid = new Bid(agent, BidType.BUY, commodity, quantityToBuy, minMarketPrice);
                Bid sellBid = new Bid(agent, BidType.SELL, commodity, agent.inventory.get(commodity), maxMarketPrice);
                market.acceptBid(buyBid);
                market.acceptBid(sellBid);
            }
        }
    }
}
