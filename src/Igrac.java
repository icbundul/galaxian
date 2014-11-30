import java.awt.*;
import java.awt.image.*;
import javax.imageio.ImageIO;
import java.io.File;

public class Igrac {
	
	// VARIJABLE
	private int x;
	private int y;
	private int r;
	
	private int dx;
	private int dy;
	private int speed;
	
	private int width;
	private int height;
	
	private int lives;
	private int score;
	
	private Color color1;
	private Color color2;
	
	private boolean left;
	private boolean right;
	
	private boolean recovering;
	private long recoveryTimer;
	
	private boolean firing;
	private long firingTimer;
	private long firingDelay;
	
	private Animation animation;
	private BufferedImage[] idleSprites;
	private BufferedImage[] leftSprites;
	private BufferedImage[] rightSprites;
	private BufferedImage[] hitSprites;
	
	public Igrac() {
		
		x = GalaxianPanel.WIDTH / 2;
		y = GalaxianPanel.HEIGHT - 100;
		r = 10;
		
		dx = 0;
		dy = 0;
		speed = 10;
		
		lives = 5;
		
		width = 40;
		height = 42;
		
		color1 = Color.WHITE;
		color2 = Color.RED;
		
		firing = false;
		firingTimer = System.nanoTime();
		firingDelay = 500;
		
		recovering = false;
		recoveryTimer = 0;
		
		score = 0;
		
		// igrac
		try {
			// ucitajmo sprites
			idleSprites = new BufferedImage[3];
			leftSprites = new BufferedImage[3];
			rightSprites = new BufferedImage[3];
			hitSprites = new BufferedImage[2];
			
			BufferedImage image = ImageIO.read(new File("graphics/spaceshipsprites.gif"));
			idleSprites[0] = image.getSubimage(38, 0,  40, 40);
			idleSprites[1] = image.getSubimage(38, 40, 40, 41);
			idleSprites[2] = image.getSubimage(38, 86, 40, 41);
			
			leftSprites[0] = image.getSubimage(0, 0,  29, 40);
			leftSprites[1] = image.getSubimage(0, 40, 29, 41);
			leftSprites[2] = image.getSubimage(0, 86, 29, 41);
			
			rightSprites[0] = image.getSubimage(84, 0,  29, 40);
			rightSprites[1] = image.getSubimage(84, 40, 29, 41);
			rightSprites[2] = image.getSubimage(84, 86, 29, 41);
			
			hitSprites[0] = null;
			hitSprites[1] = idleSprites[1];			
		}
		catch(Exception e) {
			e.printStackTrace();
		}		
		
		animation = new Animation();
	}
	
	public int getx() { return x; }
	public int gety() { return y; }
	public int getr() { return r; }
	public int getScore() { return score; }
	
	public void setLeft(boolean b) { left = b; }
	public void setRight(boolean b) { right = b; }
	
	public int getLives() { return lives; }
	
	public void setFiring(boolean b) { firing = b; }
	
	public void addScore(int i) { score += i; }; 
	
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
		
		if (!left && !right) {
		animation.setFrames(idleSprites);
		animation.setDelay(500);
		}
		
		if (left) {
			animation.setFrames(leftSprites);
			animation.setDelay(500);
		}
		
		if (right) {
			animation.setFrames(rightSprites);
			animation.setDelay(500);
		}
		
		// god mode oko 2sec
		if (recovering) {
			
			animation.setFrames(hitSprites);
			animation.setDelay(100);
			// koliko je vremena proslo nakon sto smo pogodeni			
			long elapsed = (System.nanoTime() - recoveryTimer) / 1000000;
			if (elapsed > 2000) {
				recovering = false;
				recoveryTimer = 0;
			}
		}
		
		animation.update();
	}
	
	public void draw(Graphics2D g) {
		
			g.drawImage(
					animation.getImage(), 
					x - (2 * r), 
					y - (2 * r), 
					width, 
					height, 
					null);
			
	}
	
}
