import javax.swing.JFrame;


public class GameGalaxian {

	public static void main(String[] args) {
		
		JFrame window = new JFrame("Galaxian");
		//.............................. x botun prekid
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		window.setContentPane(new GalaxianPanel());
		
		window.pack();
		window.setVisible(true);
	}

}