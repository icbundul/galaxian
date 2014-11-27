import java.awt.*;


public class Igrac {
	
	// VARIJABLE
	private int x;
	private int y;
	private int r;
	
	private int dx;
	private int dy;
	private int speed;
	
	private int lives;
	
	private Color color1;
	private Color color2;
	
	private boolean left;
	private boolean right;
	
	private boolean recovering;
	private long recoveryTimer;
	
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
		
		lives = 5;
		
		color1 = Color.WHITE;
		color2 = Color.RED;
		
		firing = false;
		firingTimer = System.nanoTime();
		firingDelay = 500;
		
		recovering = false;
		recoveryTimer = 0;
	}
	
	public int getx() { return x; }
	public int gety() { return y; }
	public int getr() { return r; }
	
	public void setLeft(boolean b) { left = b; }
	public void setRight(boolean b) { right = b; }
	
	public int getLives() { return lives; }
	
	public void setFiring(boolean b) { firing = b; }
	
	public boolean isRecovering() { return recovering; }
	
	public void loseLife() {
		GalaxianPanel.explosions.add(new Explosion(20 + (20 * lives), 
										GalaxianPanel.HEIGHT - 30, 
										getr(), 
										(getr() + 30))
									);
		lives--;
		recovering = true;
		recoveryTimer = System.nanoTime();
	}
	
	public void revive() {
		lives = 3;
		recovering = false;
	}
	
	public boolean isDead() { return (lives <= 0); }
	
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
				GalaxianPanel.meci.add(new Metak(270, x, y - r));
			}
		}
		
		// god mode oko 2sec
		if (recovering) {
			// koliko je vremena proslo nakon sto smo pogodeni
			long elapsed = (System.nanoTime() - recoveryTimer) / 1000000;
			if (elapsed > 2000) {
				recovering = false;
				recoveryTimer = 0;
			}
		}
	}
	
	public void draw(Graphics2D g) {
		
		if (recovering) {
			g.setColor(color2);
			g.fillOval(x - r, y - r, 2 * r, 2 * r);
			
			g.setStroke(new BasicStroke(3));
			g.setColor(color2.darker());
			g.drawOval(x - r, y - r, 2 * r, 2 * r);
			g.setStroke(new BasicStroke(1));
		}
		else {
			g.setColor(color1);
			g.fillOval(x - r, y - r, 2 * r, 2 * r);
		
			g.setStroke(new BasicStroke(3));
			g.setColor(color1.darker());
			g.drawOval(x - r, y - r, 2 * r, 2 * r);
			g.setStroke(new BasicStroke(1));
		}
	}
	
}
