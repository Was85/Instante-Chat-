
import java.awt.BorderLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;




public class Server extends JFrame {
	
	private JTextField userText;
	private JTextArea chatWindow ;
	private ObjectOutputStream output ;
	private ObjectInputStream input ;
	private ServerSocket server ;
	private Socket connection ;
	


	
	public Server()  {
		super("Instant Message App...");
		userText = new JTextField();
		chatWindow = new JTextArea();		
		userText.setEditable(false);
	
		userText.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				sendMessage(e.getActionCommand());
				userText.setText("");
			}
		});
		
		add(new JScrollPane(chatWindow));
		add(userText , BorderLayout.NORTH);
		setSize(400,200);
		setVisible(true);
		
	}

// setting and run 
	public void startRunning(){
		
		try {
			server = new ServerSocket(6789, 100);
			while(true){
				try {
					// establish connection 
				waitForConnection();
				setUpStream();
				messaging();
				}catch (EOFException eofException){
					showMessage("\n Connection Ended By Server ! ");
				} finally {
					flushing();
				}			
			}			
		} catch (IOException ioException) {			
			ioException.printStackTrace();
		}
	}
	// waiting Connection 
	private void waitForConnection() throws IOException {
		showMessage("\n Waiting For Client Side ");
		connection = server.accept();
		showMessage(" Now Connected To "+ connection.getInetAddress().getHostName());
		 
	}
	// set up Stream to send and receive messaging  
	private void setUpStream() throws IOException {	
		output = new ObjectOutputStream(connection.getOutputStream());
		output.flush();
		input = new ObjectInputStream(connection.getInputStream());
		showMessage(" \n Streams Setted Up To Go ");
		
	}
	
	// while Messaging 
	
	private void messaging() throws IOException {
	
		String message = "You  Connected To The Server Successfully !";
		sendMessage (message);
		ableToTypeIntoTextArea(true);
		
		do {
			try {
				message = (String) input.readObject();
				showMessage(" \n "+message);

	 		}catch (ClassNotFoundException classNotFoundException){
				showMessage(" \n error .....");

			}
			
			
		}while(!message.equals("CLIENT - END"));
		
	}
	

	// clearing everything by closing streams and socket ...
	private void flushing() {
		showMessage("\n Closing Connection...");
		ableToTypeIntoTextArea(false);
		try {
			output.close();
			input.close();
			connection.close();
		} catch (IOException ioException){
			ioException.printStackTrace();
		}
			
	}
	
	

	// send message to the client side ...
	private void sendMessage(String message) {
		try {
			output.writeObject("SERVER - "+message);
			output.flush();
			showMessage("\nSERVER - " + message);

		} catch (IOException ioException){
			chatWindow.append("Erro In sending Process ! ");
		}
		
	}
	
	// Update TextArea on chat window 
	
	private void showMessage(String text) {
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				chatWindow.append(text);
				
			}
		});
	}
	// allow the user to type text into textArea 
	
	private void ableToTypeIntoTextArea(boolean status ) {
SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				userText.setEditable(status);
				
			}
		});
		
	}
	
	
	

}
