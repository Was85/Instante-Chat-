  

import javax.swing.JFrame;


public class ServerLuncher {

	public static void main(String[] args) {

		
		Server start = new Server();
		start.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		start.startRunning();
		
		
		
	}

}
