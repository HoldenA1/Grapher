package tech.hackerlife.graph;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import tech.hackerlife.gui.Window;

public class GraphWindow extends JPanel {
	private static final long serialVersionUID = 1L;
	private final static int WIDTH_OFFSET = 17; // These make it so the width of the panel is really the width of the frame
	
	private Graph graph;
	
	public GraphWindow(String filename, int width, int height) {
		graph = new Graph(filename, width, height);
		createFrame(graph);
	}
	
	public GraphWindow(Graph graph) {
		this.graph = graph;
		createFrame(graph);
	}
	
	public void createFrame(Graph graph) {
		this.addComponentListener(new ResizeListener());
		
		Window frame = new Window(graph.getTitle(), graph.getWidth(), graph.getHeight());
		frame.setResizable(true);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.add(this);
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		this.setBackground(Color.LIGHT_GRAY);
		
		graph.draw(g, this);
	}
	
	// This makes the graph auto-resize with the frame
	class ResizeListener extends ComponentAdapter {
        public void componentResized(ComponentEvent e) {
            graph.resize(e.getComponent().getSize().width - WIDTH_OFFSET, e.getComponent().getSize().height);
        }
	}
	
	public Graph getGraph() {
		return graph;
	}

}