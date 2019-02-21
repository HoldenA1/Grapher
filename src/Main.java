import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JPanel;

import helper.Vector2D;
import helper.Window;

public class Main extends JPanel {
	private static final long serialVersionUID = 1L;
	private final static String NAME = "Grapher";
	private final static int WIDTH = 640;
	private final static int HEIGHT = 480;
	
	// These make it so the width and height of the panel are really the width and height of the frame
	private final static int HEIGHT_OFFSET = 40;
	private final static int WIDTH_OFFSET = 17;
	
	private String[] labels = {"Position", "Velocity"};
	
	Graph graph = new Graph("Time", labels, WIDTH - WIDTH_OFFSET, HEIGHT - HEIGHT_OFFSET);

	public static void main(String[] args) {
		Window frame = new Window(NAME, WIDTH, HEIGHT);
		Main panel = new Main();
		frame.add(panel);
	}
	
	public Main() {
		this.addComponentListener(new ResizeListener());
		
		graph.displayGrid(false);
		
		for (float i = 0; i < 7; i++) {
			graph.addData(1, new Vector2D(i, i*i));
		}
		for (float i = 0; i < Math.PI * 2; i+= Math.PI / 12) {
			graph.addData(new Vector2D(i, (float)(20*Math.sin(i))));
		}
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		this.setBackground(Color.LIGHT_GRAY);
		
		graph.draw(g);
		
		repaint();
	}
	
	class ResizeListener extends ComponentAdapter {
        public void componentResized(ComponentEvent e) {
            graph.resize(e.getComponent().getSize().width - WIDTH_OFFSET, e.getComponent().getSize().height);
        }
	}

}