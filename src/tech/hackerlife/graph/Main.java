package tech.hackerlife.graph;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.JPanel;
import tech.hackerlife.graph.Graph;
import tech.hackerlife.graph.helper.Vector2D;
import tech.hackerlife.graph.helper.Window;

public class Main extends JPanel {
	private static final long serialVersionUID = 1L;
	private final static int WIDTH = 640;
	private final static int HEIGHT = 480;
	
	// These make it so the width of the panel is really the width of the frame
	private final static int WIDTH_OFFSET = 17;
	
	private String[] labels = {"Position", "Velocity"};
	
	Graph graph = new Graph("Time", labels, WIDTH, HEIGHT);

	public static void main(String[] args) {
		Window frame = new Window("Graphing Sample", WIDTH, HEIGHT);
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
	
	// This makes the graph auto-resize with the frame
	class ResizeListener extends ComponentAdapter {
        public void componentResized(ComponentEvent e) {
            graph.resize(e.getComponent().getSize().width - WIDTH_OFFSET, e.getComponent().getSize().height);
        }
	}

}