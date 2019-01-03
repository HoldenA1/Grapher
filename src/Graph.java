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
	private int[] xLabels = {0,5,10,15,20,25,30};//new int[xNumOfLines];
	private int[] yLabels = {0,1,2,3,4,5};//new int[yNumOfLines];
	private boolean displayGrid = false;
	
	private String title;
	private Font normalFont = new Font(Font.MONOSPACED, Font.PLAIN, 25);
	private ArrayList<Vector2D> dataPoints = new ArrayList<Vector2D>();
	
	public Graph(String xAxisLabel, String yAxisLabel, int width, int height) {
		title = yAxisLabel + " vs. " + xAxisLabel;
		titleY = (int)(CHAR_HEIGHT * 1.5);
		titleX = width / 2 - (title.length() * CHAR_WIDTH) / 2;
		this.width = width - CHAR_WIDTH * 2; // with minus 2 chars is for asthetics
		this.height = height;
		xScale = (int) ((this.width - xBuffer) / (xNumOfLines-1));
		yScale = (int) ((this.height - yBuffer - titleY) / (yNumOfLines-1));
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
		
		
		if (xScale < CHAR_WIDTH * 5) {
			xScale = (int) ((this.width - xBuffer) / (xNumOfLines-1));
		}
		if (yScale < CHAR_HEIGHT * 2) {
			yScale = (int) ((this.height - yBuffer - titleY) / (yNumOfLines-1));
		}
	}
	
	public void draw(Graphics g) {
		// Draw Title
		g.setFont(normalFont);
		g.drawString(title, titleX, titleY);
		
		// Draw scale and grid
		drawScale(g);
		
		// Draw Axes
		g.drawLine(xBuffer, titleY, xBuffer, height - yBuffer); // Vertical
		g.drawLine(xBuffer, height - yBuffer, width, height - yBuffer); // Horizontal
		
		// Draw Line
	}
	
	public void drawScale(Graphics g) {
		for (int yLines = 0; yLines < yNumOfLines; yLines++) { // Vertical
			// Vertical is minus since up is positive
			
			if (displayGrid) {
				g.setColor(Color.GRAY);
				g.drawLine(xBuffer, height - yBuffer - yLines * yScale, width, height - yBuffer - yLines * yScale);
				g.setColor(Color.BLACK);
			}
			
			g.drawLine(xBuffer - CHAR_WIDTH / 2, height - yBuffer - yLines * yScale, xBuffer + CHAR_WIDTH / 2, height - yBuffer - yLines * yScale);
			
			// Labels
			String label = Integer.toString(yLabels[yLines]);
			g.drawString(label, CHAR_WIDTH / 2, height - yBuffer - yLines * yScale + CHAR_HEIGHT / 2);
		}
		for (int xLines = 0; xLines < xNumOfLines; xLines++) { // Horizontal
			if (displayGrid) {
				g.setColor(Color.GRAY);
				g.drawLine(xBuffer + xLines * xScale, titleY, xBuffer + xLines * xScale, height - yBuffer);
				g.setColor(Color.BLACK);
			}
			
			g.drawLine(xBuffer + xLines * xScale, height - yBuffer - CHAR_HEIGHT / 2, xBuffer + xLines * xScale, height - yBuffer + CHAR_HEIGHT / 2);
			
			// Labels
			String label = Integer.toString(xLabels[xLines]);
			g.drawString(label, xBuffer + xLines * xScale - (CHAR_WIDTH * label.length()) / 2, height - CHAR_HEIGHT / 2);
		}
	}

}