
public class Main {

	public static void main(String[] args) {

		Market market = new Market();
		for(int a = 0; a < 500; a++)
		{
		System.out.println("Trading cycle " + a);
		System.out.println("____________________");
		market.update();
		}
		
	}

}
