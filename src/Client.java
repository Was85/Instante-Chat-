import java.awt.BorderLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;


public class Client extends JFrame {
	
	private JTextField userText;
	private JTextArea chatWindow ;
	private ObjectOutputStream output ;
	private ObjectInputStream input ;
	private String message= "";
	private Socket connection ;
	private String serverIp ;

	
	
	public Client(String host)  {
		super("Client Chat Window.");
		
		serverIp = host ;
		userText = new JTextField();
		chatWindow = new JTextArea();
		
		userText.setEditable(false);
		
		userText.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				sendClientMessage(e.getActionCommand());
				userText.setText(""); 	
			}
		});
		
		add(new JScrollPane(chatWindow) ,BorderLayout.CENTER);
		add(userText, BorderLayout.NORTH);
		setSize(400,200);
		setVisible(true);		
		
	}
	
	// connect to server 
	public void startClient(){
		try{
			connectToTheServer();
			startStream();
			conversation();			
		}catch (EOFException eofException){
			showMessage ("\n Client Side Terminated  !");
		}catch (IOException ioException){
			ioException.printStackTrace();
		} finally {
			clientCleanerCollector();
		}		
	}
	
	
	// establish connection with server ...
	private void connectToTheServer () throws IOException   {
	showMessage(" Connecting To The Server  ....");
	connection = new Socket("127.0.0.1",6789);
	showMessage(" You Are Connected "+connection.getInetAddress().getHostName());	
		
	}
	
	// setting up stream input and output 
	private void startStream() throws IOException {
		output = new ObjectOutputStream(connection.getOutputStream());
		output.flush();
		input = new ObjectInputStream(connection.getInputStream());
		showMessage("Straems Are Ready !");
	}
	
	// chatting 
	private void conversation() throws IOException {
		letUserType(true);
		do {
			try{
				message = (String)input.readObject();
				showMessage("\n "+message);
			}catch(ClassNotFoundException classNotFoundException){
				showMessage("\n No idea what the hell was That !!! ");
			}
		}while (!message.equals("SERVER - END)"));
	}
	
	// close all 
	private void clientCleanerCollector() {
		showMessage("\n Closing all Sockets and Streams");
		letUserType(false);
		
		try {
			output.close();
			input.close();
			connection.close();
		}catch (IOException ioException) {
			ioException.printStackTrace();
		}	
	}
	
	

	// send message to server 
	private void sendClientMessage(String message) {
		try {
			output.writeObject("CLIENT - "+ message);
			output.flush();
			showMessage("\nCLIENT - "+ message);
		} catch (IOException e) {
			showMessage("\n Error While sending message !!!");
		}
		
	}
	
	private void showMessage(final String message) {
		
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {	
				chatWindow.append(message);
			}
		});
		
		
	}
	
	// enable the user to print in text field 
	private void letUserType(final boolean status) {		
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				userText.setEditable(status);
			}
		});
	}
	
	
	
	
}
