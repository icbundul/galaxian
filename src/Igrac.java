import java.awt.*;


public class Igrac {
	
	// VARIJABLE
	private int x;
	private int y;
	private int r;
	
	private int dx;
	private int dy;
	private int speed;
	
	private Color color1;
	
	private boolean left;
	private boolean right;
	
	private boolean firing;
	private long firingTimer;
	private long firingDelay;
	
	public Igrac() {
		
		x = GalaxianPanel.WIDTH / 2;
		y = GalaxianPanel.HEIGHT - 100;
		r = 10;
		
		dx = 0;
		dy = 0;
		speed = 10;
		
		color1 = Color.WHITE;
		
		firing = false;
		firingTimer = System.nanoTime();
		firingDelay = 500;
	}
	
	public int getx() { return x; }
	public int gety() { return y; }
	public int getr() { return r; }
	
	public void setLeft(boolean b) { left = b; }
	public void setRight(boolean b) { right = b; }
	
	public void setFiring(boolean b) { firing = b; }
	
	public void update() {
		
		if (left) {
			dx = - speed;
		}
		if (right) {
			dx = speed;
		}
		
		x += dx;
		y += dy;
		
		if (x < r) x = r;
		if (y < r) y = r;
		if (x > GalaxianPanel.WIDTH - r) x = GalaxianPanel.WIDTH - r; 	// GRANICE EKRANA
		if (y > GalaxianPanel.HEIGHT - r) y = GalaxianPanel.HEIGHT - r;
	
		dx = 0;
		dy = 0;
		
		// pucanje, firing
		if (firing) {
			long elapsed = (System.nanoTime() - firingTimer) / 1000000; // zadnje pucanje
		
			if (elapsed > firingDelay) {
				
				firingTimer = System.nanoTime();
				GalaxianPanel.meci.add(new Metak(270, x, y));
			}
		}
	}
	
	public void draw(Graphics2D g) {
		g.setColor(color1);
		g.fillOval(x - r, y - r, 2 * r, 2 * r);
		
		g.setStroke(new BasicStroke(3));
		g.setColor(color1.darker());
		g.drawOval(x - r, y - r, 2 * r, 2 * r);
		g.setStroke(new BasicStroke(1));
	}
	
}
