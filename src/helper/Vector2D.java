package helper;
import java.awt.Color;
import java.awt.Graphics;

public class Vector2D {
	// Down is positive!!
	float x, y;
	final float VECTOR_ARROW_SIZE = 10;
	
	public Vector2D(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * @param angle in radians measured from the x-axis
	 * @param mag is magnitude of vector
	 */
	public Vector2D(double theta, float mag) {
		this.x = (float) (mag * Math.cos(theta));
		this.y = (float) (mag * Math.sin(theta));
	}
	
	public Vector2D add(Vector2D vec) {
		float x = this.x + vec.X();
		float y = this.y + vec.Y();
		return new Vector2D(x, y);
	}
	
	public Vector2D add(float scalar) {
		float x = this.x + scalar;
		float y = this.y + scalar;
		return new Vector2D(x, y);
	}
	
	public Vector2D divideVec(Vector2D vec) {
		float x = this.x / vec.X();
		float y = this.y / vec.Y();
		return new Vector2D(x, y);
	}
	
	public Vector2D divideScalar(float scalar) {
		float x = this.x / scalar;
		float y = this.y / scalar;
		return new Vector2D(x, y);
	}
	
	public Vector2D mult(float scalar) {
		float x = this.x * scalar;
		float y = this.y * scalar;
		return new Vector2D(x, y);
	}
	
	public float mag() {
		return (float) Math.sqrt(x*x + y*y);
	}
	
	public Vector2D normalize() {
		float magnitude = this.mag();
		if (magnitude > 0) {
			return this.divideScalar(magnitude);
		}
		return this;
	}
	
	/**
	 * @return angle in radians
	 */
	public double dir() {
		return Math.atan2(this.y, this.x);
	}
	
	public boolean equals(Vector2D vec) {
		return x == vec.X() && y == vec.Y();
	}
	
	/**
	 * @param scale Put 1 for normal use
	 */
	public void drawVector(Graphics g, Vector2D pos, float scale, Color vectorColor) {
		g.setColor(vectorColor);
		
		Vector2D scaledInitial = pos.mult(scale);
		Vector2D scaledFinal = this.mult(scale);
		scaledFinal = scaledFinal.add(scaledInitial);
	    int dx = (int) (scaledFinal.X() - scaledInitial.X());
	    int dy = (int) (scaledFinal.Y() - scaledInitial.Y());
	    double D = Math.sqrt(dx*dx + dy*dy);
	    double xm = D - VECTOR_ARROW_SIZE, xn = xm, ym = VECTOR_ARROW_SIZE, yn = -VECTOR_ARROW_SIZE, x;
	    double sin = dy / D, cos = dx / D;

	    x = xm*cos - ym*sin + scaledInitial.X();
	    ym = xm*sin + ym*cos + scaledInitial.Y();
	    xm = x;

	    x = xn*cos - yn*sin + scaledInitial.X();
	    yn = xn*sin + yn*cos + scaledInitial.Y();
	    xn = x;

	    int[] xpoints = {(int) scaledFinal.X(), (int) xm, (int) xn};
	    int[] ypoints = {(int) scaledFinal.Y(), (int) ym, (int) yn};

	    g.drawLine((int) scaledInitial.X(), (int) scaledInitial.Y(), (int) scaledFinal.X(), (int) scaledFinal.Y());
	    g.fillPolygon(xpoints, ypoints, 3);
	}
	
	public float X() {
		return x;
	}
	
	public float Y() {
		return y;
	}
	
	public void setX(float x) {
		this.x = x;
	}
	
	public void setY(float y) {
		this.y = y;
	}
	
	public String toString() {
		return "(" + x + ", " + y + ")";
	}

}
