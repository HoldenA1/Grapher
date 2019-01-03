import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;

public class Main extends JPanel {
	private static final long serialVersionUID = 1L;
	private final static String NAME = "Grapher";
	private final static int WIDTH = 640;
	private final static int HEIGHT = 480;
	
	// These make it so the width and height of the panel are really the width and height of the frame
	private final static int HEIGHT_OFFSET = 40;
	private final static int WIDTH_OFFSET = 17;
	
	Graph graph = new Graph("Time", "Position", WIDTH - WIDTH_OFFSET, HEIGHT - HEIGHT_OFFSET);

	public static void main(String[] args) {
		Window frame = new Window(NAME, WIDTH, HEIGHT);
		frame.add(new Main());
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		this.setBackground(Color.LIGHT_GRAY);
		
		graph.draw(g);
		
		repaint();
	}

}