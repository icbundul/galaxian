import java.awt.*;
import java.awt.image.*;

import javax.imageio.ImageIO;

import java.io.File;

public class Neprijatelj {

	// VARIJABLE
	private double x;
	private double y;
	private int r;
	
	private double dx;
	private double dy;
	
	private double oldx;
	private double oldy;
	
	private double rad;
	private double speed;
	private double startSpeed;
	
	private int type;
	private int rank;
	
	private int health;
	
	private Color color1;
	
	private double angle;
	private int rand;
	private long haltTimer;
	
	private boolean hit;
	private boolean dead;
	private volatile boolean onMove;
	private volatile boolean fristLap;
	private volatile boolean isLeft;
		
	private boolean firing;
	//private boolean hitting;
	private double hitTimer;
	
	public Neprijatelj(int type, int rank) {
		
		this.type = type;
		this.rank = rank;
		
		x = 10;
		y = 10;
		
		if (type == 1) {
			color1 = Color.BLUE;
			if (rank == 1)
			  {
				r = 10;
				speed = 5;
				health = 1;
			  }
			if (rank == 2) {
				r = 10;
				speed = 5;
				health = 2;
			}
			if (rank == 3) {
				r = 15;
				speed = 7;
				health = 3;
			}
			if (rank == 4) {
				r = 20;
				speed = 7;
				health = 4;
			}
		}
		else if (type == 2) {
			color1 = Color.GREEN;
			if (rank == 1)
			  {
				r = 10;
				speed = 7;
				health = 4;
			  }
			if (rank == 2) {
				r = 8;
				speed = 5;
				health = 4;
			}
			if (rank == 3) {
				r = 8;
				speed = 8;
				health = 4;
			}
			if (rank == 4) {
				r = 20;
				speed = 5;
				health = 6;
			}
		}
		else if (type == 3) {
			color1 = Color.RED;
			if (rank == 1)
			  {
				r = 10;
				speed = 5;
				health = 6;
			  }
			if (rank == 2) {
				r = 10;
				speed = 10;
				health = 6;
			}
			if (rank == 3) {
				r = 8;
				speed = 10;
				health = 4;
			}
			if (rank == 4) {
				r = 30;
				speed = 5;
				health = 12;
			}
		}
		else {
			color1 = Color.ORANGE;			
			r = 10;
			speed = type;
			health = rank;
		}
				
		dx = 6;
		dy = 0;
		angle = 0;
		
		startSpeed = 6;
		haltTimer = 0;
				
		hit = false;
		dead = false;
		onMove = false;
		fristLap = false;
		firing = false;
		hitTimer = 0;		
	}
	
	public double getx() { return x; }
	public double gety() { return y; }
	public double getr() { return r; }
	
	public void setX(double x) { this.x = x; }
	public void setY(double y) { this.y = y; }
	public void setIsLeft(boolean b) { this.isLeft = b; }
	public void setFiring(boolean b) { this.firing = b; }
	
	public boolean isDead() { return dead; }
	public boolean isMoving() { return onMove; }
	
	public int getRank() { return rank; }
	public int getType() { return type; }
	
	public synchronized  void startAnim() {
		if (onMove == false) {	
			dx = -dx; // mjenjaj livo desno
		}
	}

	public void setOnMove(boolean b) { 
		
		oldx = x; // zapamtimo poziciju
		oldy = y;
		fristLap = false;
		
		this.onMove = b;		
		rand = (int) (Math.random() * 2); // dva razlicita kuta
		
		if (rand == 1) {
		  this.angle = 45;
		}
		else {
		 this.angle = 120;
		}
	}
	
	public void onMovement() {
		
		oldx += dx;
		//oldy += dy;
				
		if (!onMove) {
			x += dx;
			y += dy;
		}
		
		if (onMove) {
		 		
			if (x < - r) {
				x =  GalaxianPanel.WIDTH + r;
			}
		
			if (x > GalaxianPanel.WIDTH + r) {
				x = GalaxianPanel.WIDTH - x;
			}
		
			if (y > GalaxianPanel.HEIGHT + r) {
				y = GalaxianPanel.HEIGHT - y;
			}		 
			
			if (!fristLap) {
				rad = Math.toRadians(angle);
			
				dx = Math.cos(rad) * speed;
				dy = Math.sin(rad) * speed;
			}	 
			
			if (y > GalaxianPanel.HEIGHT / 2) {
				
				if (rand == 1) {
					angle = angle + 2;
				}
				else {
					angle = angle - 2;
				}
			} 
			
			if (y < oldy) {
				//System.out.println("-------------------- Prosao kroz trecinu ---------------");
				dx = oldx - x;
				dy = oldy - y;
				fristLap = true;
			}
			
			x += dx;
			y += dy;

			if (x == oldx && y == oldy) {
				dy = 0;
				x = oldx;
				y = oldy;
				
				onMove = false;
			}
		}
	}
	
	public void hit() {
		
		health--;
		System.out.println(health);
		
		if (health <= 0)
			dead = true;
		
		hit = true;
	}
	
	///////////////////////////////////////////////////////
	public void update() {		
		
		this.onMovement(); // kretanje neprijatelja
		
		if (onMove) {
			if (this.x >= GalaxianPanel.igrac.getx() - GalaxianPanel.igrac.getr() &&
					this.x <= GalaxianPanel.igrac.getx() + GalaxianPanel.igrac.getr() &&
					this.y < GalaxianPanel.igrac.gety()) {
				GalaxianPanel.meci.add(new Metak(90, (int)x, (int)(y + r)));
				
			}
		}
	}
	
	public void draw(Graphics2D g) {
			
		g.setColor(color1);
		g.fillOval((int)(x - r), (int)(y - r), 2 * r, 2 * r);
	
		g.setStroke(new BasicStroke(3));
		g.setColor(color1.darker());
		g.drawOval((int)(x - r), (int)(y - r), 2 * r, 2 * r);
		g.setStroke(new BasicStroke(1));
	}
}
