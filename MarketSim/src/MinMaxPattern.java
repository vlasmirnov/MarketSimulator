/**
 * User: mark.mcdonald
 * Date: 4/23/13
 */
public class MinMaxPattern extends TradingPattern {

    private Market market;
    private int numberRoundsToTrack;


    public MinMaxPattern(Market market, int numberRoundsToTrack) {
        this.market = market;
        this.numberRoundsToTrack = numberRoundsToTrack;
    }

    @Override
    public void placeBids() {

    }
}
