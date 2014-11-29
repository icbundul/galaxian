import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;


public class ExplosionSprite extends Animation {
	
	private double x;
	private double y;
	private double r;
	
	private int width;
	private int height;
	
	private BufferedImage[] boomSprite;
	
	public ExplosionSprite(double x, double y) {
		
		super();		
		
		boomSprite = new BufferedImage[9];
		
		try {
			BufferedImage image = ImageIO.read(new File("graphics/burst.png"));
			
			/*int j = 0;
			for(int i = 0; i < boomSprite.length; i++) {
				boomSprite[i] = image.getSubimage(
						i * image.getWidth()/3, 
						j, 
						image.getWidth()/3, 
						image.getWidth()/3
						);
				
				if (i == 3 || i == 6) 
				  j = j + 100;
			}*/
			
			boomSprite[0] = image.getSubimage(0,     0, 100, 100);
			boomSprite[1] = image.getSubimage(100,   0, 100, 100);
			boomSprite[2] = image.getSubimage(200,   0, 100, 100);
			boomSprite[3] = image.getSubimage(0,	100, 100, 100);
			boomSprite[4] = image.getSubimage(100, 100, 100, 100);
			boomSprite[5] = image.getSubimage(200, 100, 100, 100);
			boomSprite[6] = image.getSubimage(0,   200, 100, 100);
			boomSprite[7] = image.getSubimage(100, 200, 100, 100);
			boomSprite[8] = image.getSubimage(200, 200, 100, 100);
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		
		this.x = x - (boomSprite[0].getWidth() / 2);
		this.y = y - (boomSprite[0].getHeight() / 2);
	}

	@Override
	public void update() {
		
		super.setFrames(boomSprite);
		super.setDelay(500);

		// TODO Auto-generated method stub
		super.update();
	}
	
	public void draw(Graphics2D g) {
		g.drawImage(getImage(), (int) x, (int) y, null);
	}

	@Override
	public BufferedImage getImage() {
		// TODO Auto-generated method stub
		System.out.println("---------------> " + super.getCurrentFrame());
		return super.getImage();
	}
}
