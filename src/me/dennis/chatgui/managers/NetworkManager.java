package me.dennis.chatgui.managers;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;

public class NetworkManager implements Runnable {

	public static Socket connection;
	public static DataInputStream input;
	public static DataOutputStream output;
	
	public static boolean connect() {
		try {
			connection = new Socket(InetAddress.getLocalHost(), 8231);
			input = new DataInputStream(connection.getInputStream());
			output = new DataOutputStream(new BufferedOutputStream(connection.getOutputStream()));
			new Thread(new NetworkManager()).start();
			return true;
		} catch (ConnectException ex) {
			return false;
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return false;
	}
	
	public void sendMessage(String msg) {
		try {
			output.writeUTF(msg);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		while (true) {
			try {
				Protocol.parsePacket(NetworkManager.input.readUTF());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
}
