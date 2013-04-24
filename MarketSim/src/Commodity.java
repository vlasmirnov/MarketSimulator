/**
 * 
 * 
 */
public class Commodity {

	public int commoditynumber;
	public String name;
	public double marketprice = -1;
	public double pricefloor = 0;
	public int totalconsumption = 0;
	public int consumption = 0;

	/**
	 * 
	 * @param n
	 * @param pf
	 */
	public Commodity(String n, int pf) {
		name = n;
		pricefloor = pf;
	}
}
