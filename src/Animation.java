import java.awt.image.*;

public class Animation {
	
	private BufferedImage[] frames;
	private int currentFrame;
	
	private long startTime;
	private long delay;
	
	public Animation() { }
	
	public void setFrames(BufferedImage[] images) {
		frames = images;
		
		if (currentFrame >= frames.length) currentFrame = 0;
	}
	
	public void setDelay(long d) {
		delay = d;
	}

	public void update() { // provjeri je li trebamo sljedeci frame
		
		if (delay == -1) return; // nema animacije
		
		// ms koliko je vremena proslo od zadnje animacije
		long elapsed = (System.nanoTime() - startTime) / 1000000;
		if (elapsed > delay) {
			currentFrame++; // next frame
			startTime = System.nanoTime();
		}
		if (currentFrame == frames.length) { // out of bounds
			currentFrame = 0; // vrati na prvi frame
		}
		
	}
	
	public BufferedImage getImage() {
		return frames[currentFrame];
	}
}
