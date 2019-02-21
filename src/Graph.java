import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Random;
import helper.Vector2D;

public class Graph {
	private final int CHAR_WIDTH = 15, CHAR_HEIGHT = 18;
	private int width, height, titleX, titleY, graphHeight;
	private int xBuffer = CHAR_WIDTH * 2;
	private int yBuffer = CHAR_HEIGHT * 2; // 0.5 * CHAR_HEIGHT for the line and 1.5 * CHAR_HEIGHT for the char
	
	private float minX, maxX, maxY, minY;
	private int xScale, yScale; // Distance between lines
	private int xNumOfLines, yNumOfLines;
	private float[] xLabels;
	private float[] yLabels;
	private boolean displayGrid = false;
	
	private int radius = 10;
	
	private String title, xAxisLabel;
	private String[] labels;
	private Font normalFont = new Font(Font.MONOSPACED, Font.PLAIN, 25);
	private ArrayList<ArrayList<Vector2D>> dataPoints;
	private ArrayList<Color> lineColors;
	
	public Graph(String xAxisLabel, String yAxisLabel, int width, int height) {
		this(xAxisLabel, stringToArray(yAxisLabel), width, height);
	}
	
	public Graph(String xAxisLabel, String[] labels, int width, int height) {
		title = "";
		for (String label: labels) {
			title += label + " vs. ";
		}
		title += xAxisLabel;
		this.xAxisLabel = xAxisLabel;
		this.labels = labels;
		dataPoints = new ArrayList<ArrayList<Vector2D>>();
		lineColors = new ArrayList<Color>();
		for (int i = 0; i < labels.length; i++) {
			dataPoints.add(new ArrayList<Vector2D>());
			lineColors.add(randomColor());
		}
		
		resize(width, height);
	}
	
	private static String[] stringToArray(String s) {
		String[] array = {s};
		return array;
	}
	
	private Color randomColor() {
        Random random = new Random();
        int r = random.nextInt(155);
	    int g = random.nextInt(155);
        int b = random.nextInt(155);
        return new Color(r, g, b);
    }
	
	/**
	 * False by default
	 */
	public Graph displayGrid(boolean displayGrid) {
		this.displayGrid = displayGrid;
		return this;
	}
	
	public void resize(int width, int height) {
		titleY = (int)(-height + CHAR_HEIGHT * 1.5);
		graphHeight = (int)(titleY + CHAR_HEIGHT * 0.5);
		titleX = width / 2 - (title.length() * CHAR_WIDTH) / 2;
		this.width = width - CHAR_WIDTH * 1;
		this.height = height;
		xNumOfLines = width / 100;
		yNumOfLines = height / 70;
		if (xNumOfLines < 2) xNumOfLines = 2;
		if (yNumOfLines < 2) yNumOfLines = 2;
		xLabels = new float[xNumOfLines];
		yLabels = new float[yNumOfLines];
		xScale = (int) ((this.width - xBuffer) / (xNumOfLines-1));
		yScale = (int) ((-graphHeight - yBuffer) / (yNumOfLines-1));
		
		createLabels();
	}
	
	public void addData(Vector2D dataPoint) {
		addData(0, dataPoint);
	}
	
	/**
	 * use ONLY if your graph has multiple lines
	 */
	public void addData(int line, Vector2D dataPoint) {
		dataPoints.get(line).add(dataPoint);
		
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
		
		createLabels();
	}
	
	private void createLabels() {
		float stepX = (maxX - minX) / (xNumOfLines - 1);
		float stepY = (maxY - minY) / (yNumOfLines - 1);
		
		for (int i = 0; i < xNumOfLines; i++) {
			xLabels[i] = minX + stepX * i;
		}
		for (int i = 0; i < yNumOfLines; i++) {
			yLabels[i] = minY + stepY * i;
		}
		
		int longestLabelLength = 0;
		for (float number: yLabels) {
			String label = trimLabel(Float.toString(number));
			
			if (label.length() > longestLabelLength) {
				longestLabelLength = label.length();
			}
		}
		
		xBuffer = (longestLabelLength+1) * CHAR_WIDTH;
		
		xScale = (int) ((this.width - xBuffer) / (xNumOfLines-1));
	}
	
	private String trimLabel(String label) {
		int number = 2;
		if (label.startsWith("-")) {
			number++;
		}
		if (label.substring(number).contains(".")) {
			label = label.substring(0, label.indexOf("."));
		} else {
			label = label.substring(0, number+1);
			if (label.endsWith("0")) {
				label = label.substring(0, number);
			}
			if (label.endsWith(".")) {
				label = label.substring(0, label.length() - 1);
			}
		}
		return label;
	}
	
	public void draw(Graphics g) {
		// Moves origin to bottom right
		g.translate(0, height);
		
		// Calculates the proper origin
		float barHeight = -graphHeight - yBuffer;
		float barSpreadY = maxY - minY;
		int originY = (int) (minY / barSpreadY * barHeight) - yBuffer;

		float barWidth = width - xBuffer;
		float barSpreadX = maxX - minX;
		int originX = (int) ((barSpreadX - maxX) / barSpreadX * barWidth) + xBuffer;
		
		// Draw Title
		drawTitle(g);
		
		// Draw scale and grid
		drawScale(g, originX, originY);
		
		// Draw Axes
		g.drawLine(originX, graphHeight, originX, -yBuffer); // Vertical
		g.drawLine(xBuffer, originY, width, originY); // Horizontal
		
		// Draw Line
		for (int i = 0; i < dataPoints.size(); i++) {
			drawLine(g, i, originX, originY);
		}
		
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void drawTitle(Graphics g) {
		g.setFont(normalFont);
		String pastTitle = "";
		for (int i = 0; i < lineColors.size(); i++) {
			g.setColor(lineColors.get(i));
			g.drawString(labels[i], titleX + pastTitle.length() * CHAR_WIDTH, titleY);
			pastTitle += labels[i];
			g.setColor(Color.BLACK);
			g.drawString(" vs. ", titleX + pastTitle.length() * CHAR_WIDTH, titleY); 
			pastTitle += " vs. ";
		}
		g.drawString(xAxisLabel, titleX + pastTitle.length() * CHAR_WIDTH, titleY);
	}
	
	private void drawLine(Graphics g, int lineNumber, int originX, int originY) {
		g.setColor(lineColors.get(lineNumber));
		
		int prevX = 0, prevY = 0;
		for (int i = 0; i < dataPoints.get(lineNumber).size(); i++) {
			Vector2D pt = dataPoints.get(lineNumber).get(i);
			
			float barSpreadX = maxX - minX;
			float barWidth = width - xBuffer;
			int x = (int) ((pt.X() / barSpreadX) * barWidth) + originX;
			
			float barSpreadY = maxY - minY;
			float barHeight = -graphHeight - yBuffer;
			int y = (int) -((pt.Y() / barSpreadY) * barHeight) + originY;
			
			g.fillOval(x - radius / 2, y - radius / 2, radius, radius);
			
			if (i > 0) {
				g.drawLine(x, y, prevX, prevY);
			}
			
			prevX = x;
			prevY = y;
		}
	}
	
	private void drawScale(Graphics g, int originX, int originY) {
		g.setColor(Color.BLACK);
		
		for (int yLines = 0; yLines < yNumOfLines; yLines++) { // Vertical
			if (displayGrid) {
				g.setColor(Color.GRAY);
				g.drawLine(xBuffer, -yBuffer - yLines * yScale, width, -yBuffer - yLines * yScale);
				g.setColor(Color.BLACK);
			}
			
			g.drawLine(originX - CHAR_WIDTH / 2, -yBuffer - yLines * yScale, originX + CHAR_WIDTH / 2, -yBuffer - yLines * yScale);
			
			// Labels
			String label = trimLabel(Float.toString(yLabels[yLines]));

			g.drawString(label, xBuffer - CHAR_WIDTH / 2 - label.length() * CHAR_WIDTH, -yBuffer - yLines * yScale + CHAR_HEIGHT / 2);
		}
		for (int xLines = 0; xLines < xNumOfLines; xLines++) { // Horizontal
			if (displayGrid) {
				g.setColor(Color.GRAY);
				g.drawLine(xBuffer + xLines * xScale, graphHeight, xBuffer + xLines * xScale, -yBuffer);
				g.setColor(Color.BLACK);
			}
			
			g.drawLine(xBuffer + xLines * xScale, originY - CHAR_HEIGHT / 2, xBuffer + xLines * xScale, originY + CHAR_HEIGHT / 2);
			
			// Labels
			String label = trimLabel(Float.toString(xLabels[xLines]));
			g.drawString(label, xBuffer + xLines * xScale - (CHAR_WIDTH * label.length()) / 2, -CHAR_HEIGHT / 2);
		}
	}
	
}