import java.awt.*;
import java.awt.font.*;
import java.awt.geom.*;
import javax.swing.*;

/**
 * 
 * This class takes in the graphing properties and draws them.
 * 
 */
public class GraphingData extends JPanel {
	double[] list;
	double[] data;
	String xLabel;
	String yLabel;
	String gTitle;

	/**
	 * The constructor that initializes the graph with inputs.
	 * 
	 * @param array
	 *            : the array of doubles to be taken in for graph input data.
	 * @param xAxisTitle
	 *            : the label to be set in the graph for x-axis.
	 * @param yAxisTitle
	 *            : the label to be set in the graph for y-axis.
	 * @param graphTitle
	 *            : the title to be set in the graph.
	 */
	public GraphingData(double[] array, String xAxisTitle, String yAxisTitle,
			String graphTitle) {
		this.list = array;
		this.data = new double[(list.length / 3) + 1];
		this.xLabel = xAxisTitle;
		this.yLabel = yAxisTitle;
		this.gTitle = graphTitle;
		int index = 0;
		for (int i = 0; i < list.length; i += 3) {
			if (i + 1 >= list.length)
				data[index] = (list[i] + 0 + 0) / 1;
			else if (i + 2 >= list.length)
				data[index] = (list[i] + list[i + 1] + 0) / 2;
			else
				data[index] = (list[i] + list[i + 1] + list[i + 2]) / 3;
			index++;

		}
	}

	final int PAD = 50;

	/**
	 * Draws the graph.
	 */
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		int w = getWidth();
		int h = getHeight();
		// Draws the ordinate.
		g2.draw(new Line2D.Double(PAD, PAD, PAD, h - PAD));
		// Draws the abscissa.
		g2.draw(new Line2D.Double(PAD, h - PAD, w - PAD, h - PAD));
		// Draws the labels.
		Font font = g2.getFont();
		FontRenderContext frc = g2.getFontRenderContext();
		LineMetrics lm = font.getLineMetrics("0", frc);
		float sh = lm.getAscent() + lm.getDescent();
		// y Axis label.
		String s = yLabel;
		float sy = PAD + ((h - 2 * PAD) - s.length() * sh) / 2 + lm.getAscent();
		for (int i = 0; i < s.length(); i++) {
			String letter = String.valueOf(s.charAt(i));
			float sw = (float) font.getStringBounds(letter, frc).getWidth();
			float sx = (PAD - sw) / 2;
			g2.drawString(letter, sx, sy);
			sy += sh;
		}
		// x Axis label.
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
		// Mark Data points.
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
		// Draw Max Y-axis Value.
		s = String.valueOf(GraphingData.roundToDecimals(getMax(), 1));
		sw = (float) font.getStringBounds(s, frc).getWidth();
		sx = PAD - sw - SPAD;
		sy = (float) (h - PAD - scale * getMax() + lm.getAscent() / 2);
		g.drawString(s, (int) sx, (int) sy);
		// Draw Max X-axis Value.
		s = String.valueOf(list.length);
		sw = (float) font.getStringBounds(s, frc).getWidth();
		sx = getWidth() - PAD - sw;
		sy = (float) h - PAD + (PAD - sh) / 4 + lm.getAscent();
		g.drawString(s, (int) sx, (int) sy);
		// Draw Graph Title.
		s = gTitle;
		sy = (float) (h - PAD - scale * getMax() - 20 + lm.getAscent() / 2);
		sw = (float) font.getStringBounds(s, frc).getWidth();
		sx = (w - sw) / 2;
		g2.drawString(s, sx, sy);

	}

	/**
	 * Gets the max value index from double array, data.
	 * 
	 * @return : the index of the max value inside the input data array.
	 */
	private double getMax() {
		double max = -Integer.MAX_VALUE;
		for (int i = 0; i < data.length; i++) {
			if (data[i] > max)
				max = data[i];
		}
		return max;
	}

	/**
	 * Algorithm for Decimal Redux.
	 * 
	 * @param d
	 *            the number to be reduced in decimal place.
	 * @param c
	 *            the number that determines by how many decimal places to be
	 *            reduced.
	 * @return the reduced in decimal places number.
	 */
	public static double roundToDecimals(double d, int c) {
		int temp = (int) ((d * Math.pow(10, c)));
		return (((double) temp) / Math.pow(10, c));
	}
}
