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

	/**
	 * 
	 * @param commodity
	 */
	public GraphingData(double[] commodity) {
		this.commodities = commodity;
		this.data = new double[commodities.length / 3];
		for (int i = 0; i < commodities.length / 3; i++) {
			data[i] = (commodities[i] + commodities[i + 1] + commodities[i + 2]) / 3;
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
		String s = "data";
		float sy = PAD + ((h - 2 * PAD) - s.length() * sh) / 2 + lm.getAscent();
		for (int i = 0; i < s.length(); i++) {
			String letter = String.valueOf(s.charAt(i));
			float sw = (float) font.getStringBounds(letter, frc).getWidth();
			float sx = (PAD - sw) / 2;
			g2.drawString(letter, sx, sy);
			sy += sh;
		}
		// Abscissa label.
		s = "x axis";
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
		s = String.valueOf(String.format("%.3g%n", getMax()));
		sw = (float) font.getStringBounds(s, frc).getWidth();
		sx = PAD - sw - SPAD;
		sy = (float) (h - PAD - scale * getMax() + lm.getAscent() / 2);
		g.drawString(s, (int) sx, (int) sy);

		s = String.valueOf(commodities.length);

		sw = (float) font.getStringBounds(s, frc).getWidth();
		sx = getWidth() - PAD - sw;
		sy = (float) (h - PAD + scale * 3 + lm.getAscent() / 2);

		g.drawString(s, (int) sx, (int) sy);

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
}
