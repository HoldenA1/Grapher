package tech.hackerlife.graph;

public class ExampleCode {
	private final static int WIDTH = 640;
	private final static int HEIGHT = 480;
	
	private static String[] labels = {"Position", "Velocity"};

	public static void main(String[] args) {
		Graph g = new Graph("Time", labels, WIDTH, HEIGHT);
		GraphWindow createGraph = new GraphWindow(g);
		g.displayGrid(true);
		
		
		for (float i = 0; i < Math.PI * 2; i+= Math.PI / 12) {
			float[] y = {i*i, (float)(20*Math.sin(i))};
			g.addData(i, y);
		}
		
		g.saveData();
	}

}