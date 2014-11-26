import javax.swing.JPanel;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.*;
import java.util.ArrayList;

public class GalaxianPanel extends JPanel implements Runnable, KeyListener  {

	// POLJA
	public static int WIDTH = 800;
	public static int HEIGHT = 600;
	
	private Graphics2D g;
	private boolean running;
	private boolean isPaused;
	private Thread thread;
	
	private BufferedImage image;
	
	private int FPS = 30;
	private double averageFPS;
	
	public static Igrac igrac;
	public static ArrayList<Star> stars; 
	public static ArrayList<Neprijatelj> neprijatelji;
	public static ArrayList<Metak> meci;
	
	private long waveStartTimer;
	private long waveStartTimerDiff;
	private int stageNumber;
	private boolean waveStart;
	private int waveDelay = 4000;
	
	private long triggerTimer;
	private long triggerTimerDiff;
	private int triggerLength = 6000;
	private Information info;
	
	
	// KONSTRUKTOR
	public GalaxianPanel() {
		super();
		setPreferredSize(new Dimension(WIDTH,HEIGHT));
		setFocusable(true);
		requestFocus();
		
		triggerTimer = System.nanoTime();
	}
	
	public void stopGame() {
		running = false;
	}
	
	public boolean getRunning() { return running; }
	
	// override kada je formiran JPannel
	public void addNotify() {
		super.addNotify();
		
		if (thread == null) {
			thread = new Thread(this);
			thread.start();
		}
		addKeyListener(this);
	}
		
	//FUNKCIJE
	public void run() {
		running = true;
		
		image = new BufferedImage(WIDTH,HEIGHT, BufferedImage.TYPE_INT_RGB);
		g = (Graphics2D) image.getGraphics();
		g.setRenderingHint(
				RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON); // AA grafika
		g.setRenderingHint(
				RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON); // AA text
	
		igrac = new Igrac();
		neprijatelji = new ArrayList<Neprijatelj>();
		stars = new ArrayList<Star>();
		meci = new ArrayList<Metak>();
		info = new Information();
		
		//for (int i = 0; i < 200; i++) {
		//	stars.add(new Star());
		//}
		
		waveStartTimer = 0;
		waveStartTimerDiff = 0;
		waveStart = true;
		stageNumber = 0;
		
		long startTime;
		long URDTimeMills;
		long waitTime;
		long totalTime = 0;
		
		int frameCount = 0;
		int maxFrameCount = 60;
		
		long targetTime = 1000 / FPS;
		
		// GAME LOOP
		while (running) {
			
			startTime = System.nanoTime();
			
			if (!isPaused) { 
			  gameUpdate();
			  gameRender();
			  gameDraw();
			}

			URDTimeMills = (System.nanoTime() - startTime) / 1000000; // ms
			waitTime = targetTime - URDTimeMills;
			
			try {
				Thread.sleep(waitTime);
			}
			catch (InterruptedException e) {
				
			}
			
			totalTime += System.nanoTime() - startTime;
			frameCount++;
			if (frameCount == maxFrameCount) {
				averageFPS = 1000.0 / ((totalTime / frameCount) / 1000000);
				frameCount = 0;
				totalTime  = 0;
			}
		}
		
		g.setColor(new Color(0, 100, 255));
		g.fillRect(0, 0, WIDTH, HEIGHT);
		g.setColor(Color.WHITE);
		g.setFont(new Font("Arial", Font.PLAIN, 24));
		String s = "- G a m e  O v e r - ";
		int length = (int) g.getFontMetrics().getStringBounds(s, g).getWidth();
		g.drawString(s, (WIDTH - length) / 2, HEIGHT / 2);
		//s = "Final score: " + player.getScore();
		//length = (int) g.getFontMetrics().getStringBounds(s, g).getWidth();
		//g.drawString(s, (WIDTH - length) / 2, HEIGHT / 2 + 30);
		gameDraw();
	}

	private void gameUpdate() {	
				
		// novi level
		if (waveStartTimer == 0 && neprijatelji.size() == 0) {
			stageNumber++;
			waveStart = false;
			waveStartTimer = System.nanoTime();
		}
		else {
			waveStartTimerDiff = (System.nanoTime() - waveStartTimer) / 1000000;
									
				if (waveStartTimerDiff > waveDelay) {
					waveStart = true;
					waveStartTimer = 0;
					waveStartTimerDiff = 0;
				}
		}
		
		if (neprijatelji.size() == 0) {
			dodajNeprijatelje();
		}
		
		refreshStars(); // update zvjezde
		igrac.update(); // update igrac
		
		// enemy update
		for (int i = 0; i < neprijatelji.size(); i++) {
			neprijatelji.get(i).update();
		}
		
		
		// meci update
		for(int i = 0; i < meci.size(); i++) {
			boolean remove = meci.get(i).update();
			if (remove) {
				meci.remove(i);
				i--;
			}
		}
		
		// igrac neprijatelj kolizija
		if (!igrac.isRecovering()) {
			int px = igrac.getx();
			int py = igrac.gety();
			int pr = igrac.getr();
			
			for (int i = 0; i < neprijatelji.size(); i++) {
				Neprijatelj np = neprijatelji.get(i);
				if (np.isMoving()) {
					double npx = np.getx();
					double npy = np.gety();
					double npr = np.getr();
					
					double dx = px - npx;
					double dy = py - npy;
					double dist = Math.sqrt(dx * dx + dy * dy);
					
					if (dist < pr + npr) {
						igrac.loseLife(); // Pogodak
					}
				}	
			}
		}
		
		if (igrac.isDead()) {
			running = false;
		}
		
		//meci-neprijatelj kolizija
		for (int i = 0; i < meci.size(); i++) {
			
			Metak b = meci.get(i);
			double bx = b.getx();
			double by = b.gety();
			double br = b.getr();
			
			for (int j = 0; j < neprijatelji.size(); j++) {
				
				// Neprijatelj
				Neprijatelj e = neprijatelji.get(j);
				double ex = e.getx();
				double ey = e.gety();
				double er = e.getr();
				
				double dx = bx - ex;
				double dy = by - ey;
				double dist = Math.sqrt(dx * dx + dy * dy);
				
				if (dist < br + er) { // (udaljenost < radius) kolizija
					e.hit();
					meci.remove(i);
					i--;
					break;
				}
				
			  // igrac - metak kolizija
			  double px = igrac.getx();
			  double py = igrac.gety();
			  double pr = igrac.getr();
			  
			  double dpx = bx - px;
			  double dpy = by - py;
			  double distp = Math.sqrt(dpx * dpx + dpy * dpy);
			  
			  if (distp < br + pr && !igrac.isRecovering()) {
				  
				  igrac.loseLife();
				  meci.remove(i);
				  i--;
				  break;
			  }	
			}
		}
		
		// provjera mrtvih neprijatelja
		for (int i = 0; i < neprijatelji.size(); i++) {
			if (neprijatelji.get(i).isDead()) {
				neprijatelji.remove(i);
				i--;
			}	
		}
		
		// enemy move update
		for (int i = 0; i < neprijatelji.size(); i++) {
			if (neprijatelji.get(i).getx() > WIDTH - neprijatelji.get(i).getr() && !neprijatelji.get(i).isMoving()) {
				for (int j = 0; j < neprijatelji.size(); j++) {
					neprijatelji.get(j).startAnim();
					neprijatelji.get(j).setIsLeft(true);
					}
				 break;
				}
			if (neprijatelji.get(i).getx() < neprijatelji.get(i).getr() && !neprijatelji.get(i).isMoving()) {
				for (int j = 0; j < neprijatelji.size(); j++) {
					neprijatelji.get(j).startAnim();
					neprijatelji.get(j).setIsLeft(false);
					}
				 break;
				}
			}
		
		// kretanje update
		triggerTimerDiff = (System.nanoTime() - triggerTimer) / 1000000;
		if (triggerTimerDiff > triggerLength) {
			triggerTimer = System.nanoTime();
						
			int rand = (int) (Math.random() * neprijatelji.size());
			neprijatelji.get(rand).setOnMove(true);
		}
		
		info.update(meci.size(),averageFPS);
	}
	
	private void gameRender() {

		// background
		g.setColor(new Color(0, 0, 0));
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		// draw stars
		for (int i = 0; i < stars.size(); i++) {
			stars.get(i).draw(g);
		}

		//draw stage number
		if (waveStartTimer != 0) {
			g.setFont(new Font("Arial", Font.ITALIC, 24));
			g.setColor(Color.WHITE);	
			String s = "- S T A G E  " + stageNumber + " -";
			int length = (int) g.getFontMetrics().getStringBounds(s, g).getWidth(); //Sirina duzine sa piksela fonta
			int alpha = (int) (255 * Math.sin(3.14 * waveStartTimerDiff / waveDelay));
			if (alpha > 255) alpha = 255;
			g.setColor(new Color(255, 255, 255, alpha));
			g.drawString(s, WIDTH / 2 - length / 2, HEIGHT / 2);
		}
		
		// no of lives
		for(int i = 0; i < igrac.getLives(); i++) {
			g.setColor(Color.WHITE);
			g.fillOval(20 + (20 * i), HEIGHT - 30, igrac.getr(), igrac.getr());
			g.setStroke(new BasicStroke(3));
			g.setColor(Color.WHITE.darker());
			g.drawOval(20 + (20 * i), HEIGHT - 30, igrac.getr(), igrac.getr());
			g.setStroke(new BasicStroke(1));
			}
		
		// igrac draw
		igrac.draw(g);
				
		// neprijatelji draw
		for (int i = 0; i < neprijatelji.size(); i++) {
			neprijatelji.get(i).draw(g);
		}
		
		//meci draw
		for (int i = 0; i < meci.size(); i++) {
			meci.get(i).draw(g); 
		}
		
		// info draw
		info.draw(g);
	}
	
	private void gameDraw() {
		Graphics g2 = this.getGraphics();
		g2.drawImage(image, 0, 0, null);
		g2.dispose();
	}
	
	private void refreshStars() {
		
		int numStars = 150;
		
		if (stars.size() == 0) {
		  for (int i = 0; i < numStars; i++)
		    stars.add(new Star());
		}
		
		
		for (int i = 0; i < numStars; i++) {
		   stars.get(i).update();
			
		   if (stars.get(i).onScreen() == false) {  
			   stars.remove(i);
			   i--;
			   stars.add(new Star());
		    }
		}
	}
	
	private void dodajNeprijatelje() {
		int pozx = WIDTH / 5;
		int pozy = HEIGHT / 8;
		
		neprijatelji.clear();
		
		for(int i = 0; i < 22; i++) {
			neprijatelji.add(new Neprijatelj());
			neprijatelji.get(i).setX(pozx);
			neprijatelji.get(i).setY(pozy);
			pozx += 50;
			
			if (i == 10) {
				pozy += 50;
				pozx = WIDTH / 5;
			}
		}
	}
	
	@Override
	public void keyPressed(KeyEvent key) {
		int keyCode = key.getKeyCode();
		
		if (keyCode == KeyEvent.VK_LEFT) {
			igrac.setLeft(true);
		}
		if (keyCode == KeyEvent.VK_RIGHT) {
			igrac.setRight(true);
		}
		if (keyCode == KeyEvent.VK_Y) {
			igrac.setFiring(true);
		}
		if (keyCode == KeyEvent.VK_ESCAPE || keyCode == KeyEvent.VK_Q) {
			running = false;
		}
		if (keyCode == KeyEvent.VK_P) {
			if (isPaused)
			  isPaused = false;
			else
			  isPaused = true;
		}
		// INFO
		if (keyCode == KeyEvent.VK_I) {
			info.setinfo(); 
		}
	}

	@Override
	public void keyReleased(KeyEvent key) {
		int keyCode = key.getKeyCode();
		
		if (keyCode == KeyEvent.VK_LEFT) {
			igrac.setLeft(false);
		}
		if (keyCode == KeyEvent.VK_RIGHT) {
			igrac.setRight(false);
		}
		if (keyCode == KeyEvent.VK_Y) {
			igrac.setFiring(false);
		}
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
}
