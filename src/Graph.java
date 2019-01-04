import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;

public class Graph {
	// Text is draw from bottom left corner
	
	private final int CHAR_WIDTH = 15, CHAR_HEIGHT = 18;
	private int width, height, titleX, titleY;
	private int xBuffer = CHAR_WIDTH * 2; // will change depending on number of chars displayed
	private int yBuffer = CHAR_HEIGHT * 2; // 0.5 * CHAR_HEIGHT for the line and 1.5 * CHAR_HEIGHT for the char
	
	private float minX, maxX, maxY, minY;
	private int xScale, yScale; // Distance between lines
	private int xNumOfLines = 7, yNumOfLines = 6;
	private float[] xLabels = new float[xNumOfLines];
	private float[] yLabels = new float[yNumOfLines];
	private boolean displayGrid = false;
	
	private int radius = 10;
	
	private String title;
	private Font normalFont = new Font(Font.MONOSPACED, Font.PLAIN, 25);
	private ArrayList<Vector2D> dataPoints = new ArrayList<Vector2D>();
	
	public Graph(String xAxisLabel, String yAxisLabel, int width, int height) {
		title = yAxisLabel + " vs. " + xAxisLabel;
		titleY = (int)(-height + CHAR_HEIGHT * 1.5);
		titleX = width / 2 - (title.length() * CHAR_WIDTH) / 2;
		this.width = width - CHAR_WIDTH * 1; // with minus 2 chars is for asthetics
		this.height = height;
		xScale = (int) ((this.width - xBuffer) / (xNumOfLines-1));
		yScale = (int) ((-titleY - yBuffer) / (yNumOfLines-1));
		
		// Temp array data
		for (float i = 0; i < 2 * Math.PI; i += Math.PI / 6) {
			addData(new Vector2D(i, (float)(Math.sin(i))));
		}
	}
	
	public void addData(Vector2D dataPoint) {
		dataPoints.add(dataPoint);
		
		if (dataPoint.X() > maxX) {
			maxX = dataPoint.X();
		} else if (dataPoint.X() < minX) {
			minX = dataPoint.X();
		}
		
		if (dataPoint.Y() > maxY) {
			maxY = dataPoint.Y();
		} else if (dataPoint.Y() < minY) {
			minY = dataPoint.Y();
		}
		
		updateScale();
	}
	
	public void updateScale() {
		float stepX = (maxX - minX) / (xNumOfLines - 1);
		float stepY = (maxY - minY) / (yNumOfLines - 1);
		
		for (int i = 0; i < xNumOfLines; i++) {
			xLabels[i] = minX + stepX * i;
		}
		for (int i = 0; i < yNumOfLines; i++) {
			yLabels[i] = minY + stepY * i;
		}
		
//		if (xScale < CHAR_WIDTH * 5) {
//			xScale = (int) ((this.width - xBuffer) / (xNumOfLines-1));
//		}
//		if (yScale < CHAR_HEIGHT * 2) {
//			yScale = (int) ((this.height - yBuffer - titleY) / (yNumOfLines-1));
//		}
	}
	
	public void draw(Graphics g) {
		// Moves origin to bottom right
		g.translate(0, height);
		
		// Draw Title
		g.setFont(normalFont);
		g.drawString(title, titleX, titleY);
		
		// Draw scale and grid
		drawScale(g);
		
		// Draw Axes
		g.drawLine(xBuffer, titleY, xBuffer, -yBuffer); // Vertical
		g.drawLine(xBuffer, -yBuffer, width, -yBuffer); // Horizontal
		
		// Draw Line
		drawLine(g);
	}
	
	public void drawLine(Graphics g) {
		g.setColor(Color.BLUE);
		
		int prevX = 0, prevY = 0;
		for (int i = 0; i < dataPoints.size(); i++) {
			Vector2D pt = dataPoints.get(i);
			
			float barSpreadX = maxX - minX;
			float barWidth = width - xBuffer;
			int x = (int) ((pt.X() / barSpreadX) * barWidth) + xBuffer;
			
			float barSpreadY = maxY - minY;
			float barHeight = -titleY - yBuffer;
			int y = (int) -((pt.Y() / barSpreadY) * barHeight) - yBuffer;
			
			g.fillOval(x - radius / 2, y - radius / 2, radius, radius);
			
			if (i > 0) {
				g.drawLine(x, y, prevX, prevY);
			}
			
			prevX = x;
			prevY = y;
		}
	}
	
	public void drawScale(Graphics g) {
		g.setColor(Color.BLACK);
		for (int yLines = 0; yLines < yNumOfLines; yLines++) { // Vertical
			if (displayGrid) {
				g.setColor(Color.GRAY);
				g.drawLine(xBuffer, -yBuffer - yLines * yScale, width, -yBuffer - yLines * yScale);
				g.setColor(Color.BLACK);
			}
			
			g.drawLine(xBuffer - CHAR_WIDTH / 2, -yBuffer - yLines * yScale, xBuffer + CHAR_WIDTH / 2, -yBuffer - yLines * yScale);
			
			// Labels
			String label = Float.toString(yLabels[yLines]).substring(0, 3);
			if (label.endsWith("0")) {
				label = label.substring(0, 2);
			}
			if (label.endsWith(".")) {
				label = label.substring(0, label.length() - 1);
			}
			g.drawString(label, CHAR_WIDTH / 2, -yBuffer - yLines * yScale + CHAR_HEIGHT / 2);
		}
		for (int xLines = 0; xLines < xNumOfLines; xLines++) { // Horizontal
			if (displayGrid) {
				g.setColor(Color.GRAY);
				g.drawLine(xBuffer + xLines * xScale, titleY, xBuffer + xLines * xScale, height - yBuffer);
				g.setColor(Color.BLACK);
			}
			
			g.drawLine(xBuffer + xLines * xScale, -yBuffer - CHAR_HEIGHT / 2, xBuffer + xLines * xScale, -yBuffer + CHAR_HEIGHT / 2);
			
			// Labels
			String label = Float.toString(xLabels[xLines]).substring(0, 3);
			if (label.endsWith("0")) {
				label = label.substring(0, 2);
			}
			if (label.endsWith(".")) {
				label = label.substring(0, label.length() - 1);
			}
			g.drawString(label, xBuffer + xLines * xScale - (CHAR_WIDTH * label.length()) / 2, -CHAR_HEIGHT / 2);
		}
	}

}