import java.awt.*;
import java.awt.font.*;
import java.awt.geom.*;
import javax.swing.*;

/**
 * 
 * 
 */
public class GraphingData extends JPanel {
	double[] commodities;
	double[] data;
	String xLabel;
	String yLabel;
	String gTitle;

	/**
	 * 
	 * @param commodity
	 * @param xAxisTitle
	 * @param yAxisTitle
	 * @param graphTitle
	 */
	public GraphingData(double[] commodity, String xAxisTitle, String yAxisTitle, String graphTitle) {
		this.commodities = commodity;
		this.data = new double[(commodities.length/3)+1];
		this.xLabel = xAxisTitle;
		this.yLabel = yAxisTitle;
		this.gTitle = graphTitle;
		//System.out.println(xAxisTitle + " " + yAxisTitle);
		int index = 0;
		for (int i = 0; i < commodities.length; i += 3) {
			if (i + 1 >= commodities.length)
				data[index] = (commodities[i] + 0 + 0) / 1;
			else if (i + 2 >= commodities.length)
				data[index] = (commodities[i] + commodities[i + 1] + 0) / 2;
			else
				data[index] = (commodities[i] + commodities[i + 1] + commodities[i + 2]) / 3;
			//System.out.println(Market.roundToDecimals(data[index],2));
			index++;
			
		}
	}

	final int PAD = 50;

	/**
	 * 
	 */
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		int w = getWidth();
		int h = getHeight();
		// Draw ordinate.
		g2.draw(new Line2D.Double(PAD, PAD, PAD, h - PAD));
		// Draw abscissa.
		g2.draw(new Line2D.Double(PAD, h - PAD, w - PAD, h - PAD));
		// Draw labels.
		Font font = g2.getFont();
		FontRenderContext frc = g2.getFontRenderContext();
		LineMetrics lm = font.getLineMetrics("0", frc);
		float sh = lm.getAscent() + lm.getDescent();
		// Ordinate label.
		String s = yLabel;
		float sy = PAD + ((h - 2 * PAD) - s.length() * sh) / 2 + lm.getAscent();
		for (int i = 0; i < s.length(); i++) {
			String letter = String.valueOf(s.charAt(i));
			float sw = (float) font.getStringBounds(letter, frc).getWidth();
			float sx = (PAD - sw) / 2;
			g2.drawString(letter, sx, sy);
			sy += sh;
		}
		// Abscissa label.
		s = xLabel;
		sy = h - PAD + (PAD - sh) / 2 + lm.getAscent();
		float sw = (float) font.getStringBounds(s, frc).getWidth();
		float sx = (w - sw) / 2;
		g2.drawString(s, sx, sy);
		// Draw lines.
		double xInc = (double) (w - 2 * PAD) / (data.length - 1);
		double scale = (double) (h - 2 * PAD) / getMax();
		g2.setPaint(Color.green.darker());
		for (int i = 0; i < data.length - 1; i++) {
			double x1 = PAD + i * xInc;
			double y1 = h - PAD - scale * data[i];
			double x2 = PAD + (i + 1) * xInc;
			double y2 = h - PAD - scale * data[i + 1];
			g2.draw(new Line2D.Double(x1, y1, x2, y2));
		}
		// Mark data points.
		g2.setPaint(Color.red);
		for (int i = 0; i < data.length; i++) {
			double x = PAD + i * xInc;
			double y = h - PAD - scale * data[i];
			g2.fill(new Ellipse2D.Double(x - 2, y - 2, 4, 4));
		}

		xInc = (double) (w - 2 * PAD) / (data.length - 1);
		scale = (double) (h - 2 * PAD) / getMax();

		double x = PAD + 1 * xInc;
		sy = h - PAD + (PAD - sh) / 2 + lm.getAscent();

		final int SPAD = 2;
		
		s = String.valueOf(GraphingData.roundToDecimals(getMax(),1));
		sw = (float) font.getStringBounds(s, frc).getWidth();
		sx = PAD - sw - SPAD;
		sy = (float) (h - PAD - scale * getMax() + lm.getAscent() / 2);
		g.drawString(s, (int) sx, (int) sy);

		s = String.valueOf(commodities.length);
		sw = (float) font.getStringBounds(s, frc).getWidth();
		sx = getWidth() - PAD - sw;
		sy = (float) h - PAD + (PAD - sh) / 4 + lm.getAscent();
		g.drawString(s, (int) sx, (int) sy);
		
		s = gTitle;
		sy = (float) (h - PAD - scale * getMax() + lm.getAscent() / 2);
		sw = (float) font.getStringBounds(s, frc).getWidth();
		sx = (w - sw) / 2;
		g2.drawString(s, sx, sy);

	}

	/**
	 * 
	 * @return
	 */
	private double getMax() {
		double max = -Integer.MAX_VALUE;
		for (int i = 0; i < data.length; i++) {
			if (data[i] > max)
				max = data[i];
		}
		return max;
	}
	
	public static double roundToDecimals(double d, int c) {
		int temp=(int)((d*Math.pow(10,c)));
		return (((double)temp)/Math.pow(10,c));
		}
}
