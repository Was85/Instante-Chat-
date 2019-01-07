import javax.swing.JFrame;
public class ClientStart {

	public static void main(String[] args) {
		
		Client client;
		client = new Client ("172.0.0.1");
		client.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		client.startClient();
	}

}
