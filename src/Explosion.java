import java.awt.*;
import java.awt.image.*;
import javax.imageio.ImageIO;
import java.io.File;


public class Explosion {

	// FIELDS
	private double x;
	private double y;
	private double r;
	private double maxRadius;
	private int alpha;
	
	private ExplosionSprite boom;
	
	// CONSTRUCTOR
	public Explosion (double x, double y, double d, double e) {
		
		this.x = x;
		this.y = y;
		this.r = d;
		
		maxRadius = e;
		alpha = 128;
		
		boom = new ExplosionSprite(x, y);
	}
	
	public boolean update() {
		
		r += 4;
		
		if (r >= maxRadius) {
			return true;
		}
		return false;
	}
	
	public void draw(Graphics2D g) {
		
		alpha = alpha - 2;
		
		if (alpha <= 0)
			alpha = 1;
		
		if (maxRadius != 10) {
			boom.update();
			boom.draw(g);
		}
		
		g.setColor(new Color(255,255,255,alpha));
		g.setStroke(new BasicStroke(6));
		g.drawOval((int) (x - r), (int) (y - r), (int)(2 * r), (int)(2 * r));
		g.setStroke(new BasicStroke(1));
	}
}
