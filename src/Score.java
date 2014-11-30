import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class Score {
	
	private int highScore;
	private int score;
	private String filePath;
	private boolean isRecord;
	
	private File file;
	
	public Score() {
		
		isRecord = false;
		filePath = "score/data.txt";
		
		try {
			
			file = new File(filePath);
			
			// ako file ne postoji kreiraj ga
			if (!file.exists()) {
				file.createNewFile();
			}
			
			BufferedReader br = new BufferedReader(new FileReader(filePath));
			highScore = Integer.parseInt(br.readLine());
			br.close();
		}
		catch(IOException e) { 
			e.printStackTrace();
		}
		
	}
	
	public int getHighScore() { return highScore; }
	public boolean isRecord() { return isRecord; }
	
	public void setHighScore(int score) {
		
		isRecord = false;
		
		if (score > highScore) {
			// zapisi rekord ako je oboren
			isRecord = true;
			highScore = score;
			try {
				FileWriter fw = new FileWriter(file.getAbsoluteFile());
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write(new Integer(highScore).toString());
				bw.close();
				
			} 
			catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
}
