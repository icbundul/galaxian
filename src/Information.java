import java.awt.*;

public class Information {

	private int x;
	private int y;
	
	private Color color1;
	
	private double averageFPS;
	private int bullets;
	private boolean onscreen;
	private String head;
	public char op;
	
	// CONSTRUKTOR
	public Information() {
		
		this.x = 20;
		this.y = 20;

		color1 = Color.WHITE;
		onscreen = false;
	}
	
	// FUNCTIONS
	public void setinfo() {
		if (onscreen == true)
			onscreen = false;
		else
			onscreen = true;	
		}
	
	public void update(int bullets, double fps) {
	
		this.averageFPS = fps;
		this.bullets = bullets;
	}
	
	public void draw(Graphics2D g) {
		
		if (onscreen == false)
			return;
		 
		g.setFont(new Font("Arial", Font.ITALIC, 12));
		g.setColor(color1);
		g.drawString(" --- INFO GALAXIAN ver 0.2 ---", x, y);
		g.drawString("FPS: " + averageFPS, x, y + 25);
		g.drawString("Num bullets: " + bullets, x, y + 12);
	}
}

