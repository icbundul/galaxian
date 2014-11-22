import java.awt.*;

public class Star {

	private int x;
	private int y;
	private int speed;
	private int r;
	private boolean screen;
	
	public Star() {
		
		screen = true;
		x = (int) (Math.random() * GalaxianPanel.WIDTH + 1);
		y = (int) (Math.random() * GalaxianPanel.HEIGHT + 1);
		speed = (int) (Math.random() * 4  + 1); // brzina
		r = (int) (Math.random() * 2 + 1);
	}
	
	public void update() {
		y +=  speed;	
	}
	
	public boolean onScreen() {
		
		if (y > GalaxianPanel.HEIGHT) {
			screen = false;
		}
		else {
			screen = true;
		}
		
		return screen;
	}
	
	public void draw(Graphics2D g) {
		g.setColor(Color.WHITE);
		g.fillOval(x, y, r, r);
	}
}
